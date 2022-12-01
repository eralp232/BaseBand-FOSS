package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.BlockInteractionHelper;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import dev.jess.baseband.client.api.Utils.SecondCounter;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;

import java.awt.*;

public class AutoObsidian extends Module {


	private final Setting center = new Setting("Center", this, true);
	private final Setting rotate = new Setting("Rotate", this, true);
	private final Setting noglitchblocks = new Setting("NoGlitchBlocks", this, true);
	private final Setting silentSwap = new Setting("Silent Swap", this, false);
	private final Setting updateController = new Setting("UpdateController", this, false);
	private final Setting packet = new Setting("Packet-Place", this, false);
	private final Setting triggerable = new Setting("Triggerable", this, false);
	private final Setting disableOffground = new Setting("Disable On-Vertical Move", this, true);
	private final Setting timeoutTicks = new Setting("TimeoutTicks", this, 1, 1, 100, true);
	private final Setting tickDelay = new Setting("TickDelay", this, 0, 0, 10, true);
	private final Setting blocksPerTick = new Setting("BlocksPerTick", this, 5, 1, 10, true);
	SecondCounter bps = new SecondCounter();
	private int playerHotbarSlot = - 1;
	private int lastHotbarSlot = - 1;
	private int offsetStep = 0;
	private int delayStep = 0;
	private boolean firstRun;
	private boolean isSneaking = false;
	private Vec3d playerPos;

	public AutoObsidian() {
		super("AutoObsidian", "Trap Feet in Obsidian", Category.COMBAT, new Color(0, 126, 88, 255).getRGB());
		BaseBand.settingsManager.rSetting(center);
		BaseBand.settingsManager.rSetting(rotate);
		BaseBand.settingsManager.rSetting(noglitchblocks);
		BaseBand.settingsManager.rSetting(triggerable);
		BaseBand.settingsManager.rSetting(silentSwap);
		BaseBand.settingsManager.rSetting(updateController);
		BaseBand.settingsManager.rSetting(packet);
		BaseBand.settingsManager.rSetting(disableOffground);
		BaseBand.settingsManager.rSetting(timeoutTicks);
		BaseBand.settingsManager.rSetting(tickDelay);
		BaseBand.settingsManager.rSetting(blocksPerTick);
	}

	private void centerPlayer(double x, double y, double z) {
		mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
		mc.player.setPosition(x, y, z);
	}

	double getDst(Vec3d vec) {
		return this.playerPos.distanceTo(vec);
	}


	public void enable() {
		if (mc.player == null) {
			this.setToggled(false);
			return;
		}
		BlockPos centerPos = mc.player.getPosition();
		this.playerPos = mc.player.getPositionVector();
		double y = centerPos.getY();
		double x = centerPos.getX();
		double z = centerPos.getZ();
		Vec3d plusPlus = new Vec3d(x + 0.5, y, z + 0.5);
		Vec3d plusMinus = new Vec3d(x + 0.5, y, z - 0.5);
		Vec3d minusMinus = new Vec3d(x - 0.5, y, z - 0.5);
		Vec3d minusPlus = new Vec3d(x - 0.5, y, z + 0.5);
		if (center.getValBoolean()) {
			if (this.getDst(plusPlus) < this.getDst(plusMinus) && this.getDst(plusPlus) < this.getDst(minusMinus) && this.getDst(plusPlus) < this.getDst(minusPlus)) {
				x = (double) centerPos.getX() + 0.5;
				z = (double) centerPos.getZ() + 0.5;
				this.centerPlayer(x, y, z);
			}
			if (this.getDst(plusMinus) < this.getDst(plusPlus) && this.getDst(plusMinus) < this.getDst(minusMinus) && this.getDst(plusMinus) < this.getDst(minusPlus)) {
				x = (double) centerPos.getX() + 0.5;
				z = (double) centerPos.getZ() - 0.5;
				this.centerPlayer(x, y, z);
			}
			if (this.getDst(minusMinus) < this.getDst(plusPlus) && this.getDst(minusMinus) < this.getDst(plusMinus) && this.getDst(minusMinus) < this.getDst(minusPlus)) {
				x = (double) centerPos.getX() - 0.5;
				z = (double) centerPos.getZ() - 0.5;
				this.centerPlayer(x, y, z);
			}
			if (this.getDst(minusPlus) < this.getDst(plusPlus) && this.getDst(minusPlus) < this.getDst(plusMinus) && this.getDst(minusPlus) < this.getDst(minusMinus)) {
				x = (double) centerPos.getX() - 0.5;
				z = (double) centerPos.getZ() + 0.5;
				this.centerPlayer(x, y, z);
			}
		}
		this.firstRun = true;
		this.playerHotbarSlot = mc.player.inventory.currentItem;
		this.lastHotbarSlot = - 1;
	}


	public void disable() {
		if (mc.player == null) {
			return;
		}
		if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != - 1) {
			mc.player.inventory.currentItem = this.playerHotbarSlot;
		}
		if (this.isSneaking) {
			mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
			this.isSneaking = false;
		}
		this.playerHotbarSlot = - 1;
		this.lastHotbarSlot = - 1;
	}


	public void tick() {
		if (findObiInHotbar() == - 1) {
			this.setToggled(false);
			return;
		}

		if (mc.player == null) {
			return;
		}
		if (mc.player.motionY >= 0.2 && disableOffground.getValBoolean()) {
			this.setToggled(false);
			ChatUtil.sendChatMsg("Moved! Disabling AutoObsidian.");
			return;
		}


		if (this.triggerable.getValBoolean()) {
			this.setToggled(false);
		}
		if (! this.firstRun) {
			if (this.delayStep < this.tickDelay.getValDouble()) {
				++ this.delayStep;
				return;
			}
			this.delayStep = 0;
		}
		if (this.firstRun) {
			this.firstRun = false;
		}
		int blocksPlaced = 0;
		while (blocksPlaced < this.blocksPerTick.getValDouble()) {
			Vec3d[] offsetPattern;
			int maxSteps;

			offsetPattern = Offsets.FULL;
			maxSteps = Offsets.FULL.length;


			if (this.offsetStep >= maxSteps) {
				this.offsetStep = 0;
				break;
			}
			BlockPos offsetPos = new BlockPos(offsetPattern[this.offsetStep]);
			BlockPos targetPos = new BlockPos(mc.player.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
			int obiSlot = this.findObiInHotbar();
			if (obiSlot == - 1) {
				this.setToggled(false);
			}
			if (this.placeBlock(targetPos)) {
				++ blocksPlaced;
			}
			++ this.offsetStep;
		}
		if (blocksPlaced > 0) {

			if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != - 1) {
				mc.player.inventory.currentItem = this.playerHotbarSlot;
				this.lastHotbarSlot = this.playerHotbarSlot;
			}
			if (this.isSneaking) {
				mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
				this.isSneaking = false;
			}
		}
	}


	private boolean placeBlock(BlockPos pos) {
		this.playerHotbarSlot = mc.player.inventory.currentItem;
		Block block = mc.world.getBlockState(pos).getBlock();
		if (! (block instanceof BlockAir) && ! (block instanceof BlockLiquid)) {
			return false;
		}
		for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
			if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
			return false;
		}
		EnumFacing side = BlockInteractionHelper.getPlaceableSide(pos);
		if (side == null) {
			return false;
		}
		BlockPos neighbour = pos.offset(side);
		EnumFacing opposite = side.getOpposite();
		if (! BlockInteractionHelper.canBeClicked(neighbour)) {
			return false;
		}
		Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
		Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
		int obiSlot = this.findObiInHotbar();
		if (obiSlot == - 1) {
			this.setToggled(false);
			return false;
		}
		if (this.lastHotbarSlot != obiSlot) {

			Wrapper.mc.player.connection.sendPacket(new CPacketHeldItemChange(obiSlot));
			Wrapper.mc.player.inventory.currentItem = obiSlot;
			if (updateController.getValBoolean()) {
				mc.playerController.updateController();
			}

			this.lastHotbarSlot = obiSlot;
		}
		if (! this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
			mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
			this.isSneaking = true;
		}
		if (this.rotate.getValBoolean()) {
			BlockInteractionHelper.faceVectorPacketInstant(hitVec);
		}


		if (packet.getValBoolean()) {
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
		} else {
			mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
		}
		bps.increment();
		mc.player.swingArm(EnumHand.MAIN_HAND);
		mc.rightClickDelayTimer = 4; //OHGOD PLEASE DONT BE NEEDED.....
		if (noglitchblocks.getValBoolean() && ! mc.playerController.getCurrentGameType().equals(GameType.CREATIVE)) {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
		}

		if (silentSwap.getValBoolean()) {
			Wrapper.mc.player.connection.sendPacket(new CPacketHeldItemChange(playerHotbarSlot));
			Wrapper.mc.player.inventory.currentItem = playerHotbarSlot;
			if (updateController.getValBoolean()) {
				mc.playerController.updateController();
			}
		}
		return true;
	}

	public void renderWorld() {
		this.setMcHudMeta("" + bps.getCount());
	}

	private int findObiInHotbar() {
		int slot = - 1;
		for (int i = 0; i < 9; ++ i) {
			Block block;
			ItemStack stack = mc.player.inventory.getStackInSlot(i);
			if (stack == ItemStack.EMPTY || ! (stack.getItem() instanceof ItemBlock) || ! ((block = ((ItemBlock) stack.getItem()).getBlock()) instanceof BlockObsidian))
				continue;
			slot = i;
			break;
		}
		return slot;
	}

	private static class Offsets {
		private static final Vec3d[] SURROUND = new Vec3d[]{new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(- 1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, - 1.0), new Vec3d(1.0, - 1.0, 0.0), new Vec3d(0.0, - 1.0, 1.0), new Vec3d(- 1.0, - 1.0, 0.0), new Vec3d(0.0, - 1.0, - 1.0)};
		private static final Vec3d[] FULL = new Vec3d[]{new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(- 1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, - 1.0), new Vec3d(1.0, - 1.0, 0.0), new Vec3d(0.0, - 1.0, 1.0), new Vec3d(- 1.0, - 1.0, 0.0), new Vec3d(0.0, - 1.0, - 1.0), new Vec3d(0.0, - 1.0, 0.0)};
	}


}
