package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.BlockInteractionHelper;
import dev.jess.baseband.client.api.Utils.EntityUtil;
import dev.jess.baseband.client.api.Utils.Friends;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.jess.baseband.client.api.Utils.BlockInteractionHelper.canBeClicked;
import static dev.jess.baseband.client.api.Utils.BlockInteractionHelper.faceVectorPacketInstant;

public class AutoTrap extends Module {
	private final Setting range = new Setting("Range", this, 5, 1, 6, true);
	private final Setting tickDelay = new Setting("Shift Delay", this, 1, 1, 6, true);
	private final Setting blocksPerTick = new Setting("Shift Tick", this, 2, 1, 6, true);
	private final Setting rotate = new Setting("Rotate", this, false);
	private EntityPlayer closestTarget;
	private String lastTickTargetName;
	private int playerHotbarSlot = - 1;
	private int lastHotbarSlot = - 1;
	private int delayStep = 0;
	private boolean isSneaking = false;
	private int offsetStep = 0;
	private boolean firstRun;

	public AutoTrap() {
		super("AutoTrap", "furryware port", Category.COMBAT, new Color(222, 12, 255, 255).getRGB());
		BaseBand.settingsManager.rSetting(range);
		BaseBand.settingsManager.rSetting(tickDelay);
		BaseBand.settingsManager.rSetting(blocksPerTick);
		BaseBand.settingsManager.rSetting(rotate);
	}

	public void enable() {

		if (mc.player == null) {
			this.disable();
			return;
		}

		firstRun = true;

		// save initial player hand
		lastHotbarSlot = - 1;

	}


	public void disable() {

		if (mc.player == null) {
			return;
		}

		if (lastHotbarSlot != playerHotbarSlot && playerHotbarSlot != - 1) {
			mc.player.inventory.currentItem = playerHotbarSlot;
		}

		if (isSneaking) {
			mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
			isSneaking = false;
		}

		playerHotbarSlot = - 1;
		lastHotbarSlot = - 1;

		//if (announceUsage.getValue()) {
		//	Command.sendChatMessage("[AutoTrap] " + ChatFormatting.RED.toString() + "Disabled" + ChatFormatting.RESET.toString() + "!");
		//}

	}


	public void tick() {

		if (mc.player == null) {
			return;
		}

		if (closestTarget != null) {
			this.setMcHudMeta(closestTarget.getName().toUpperCase());
		}


		if (! firstRun) {
			if (delayStep < tickDelay.getValDouble()) {
				delayStep++;
				return;
			} else {
				delayStep = 0;
			}
		}

		findClosestTarget();

		if (closestTarget == null) {
			if (firstRun) {
				firstRun = false;
				//if (announceUsage.getValue()) {
				//	Command.sendChatMessage("[AutoTrap] " + ChatFormatting.GREEN.toString() + "Enabled" + ChatFormatting.RESET.toString() + ", waiting for target.");
				//}
			}
			return;
		}

		if (firstRun) {
			firstRun = false;
			lastTickTargetName = closestTarget.getName();
			//if (announceUsage.getValue()) {
			//	Command.sendChatMessage("[AutoTrap] " + ChatFormatting.GREEN.toString() + "Enabled" + ChatFormatting.RESET.toString() + ", target: " + lastTickTargetName);
			//}
		} else if (! lastTickTargetName.equals(closestTarget.getName())) {
			lastTickTargetName = closestTarget.getName();
			offsetStep = 0;
			//if (announceUsage.getValue()) {
			//	Command.sendChatMessage("[AutoTrap] New target: " + lastTickTargetName);
			//}
		}

		List<Vec3d> placeTargets = new ArrayList<>();

		//if (cage.getValue().equals(Cage.TRAP)) {
		//	Collections.addAll(placeTargets, Offsets.TRAP);
		//}

		//if (cage.getValue().equals(Cage.TRAPFULLROOF)) {
		Collections.addAll(placeTargets, Offsets.TRAPFULLROOF);
		//}

		//if (cage.getValue().equals(Cage.CRYSTALEXA)) {
		//	Collections.addAll(placeTargets, Offsets.CRYSTALEXA);
		//}

		//if (cage.getValue().equals(Cage.CRYSTAL)) {
		//	Collections.addAll(placeTargets, Offsets.CRYSTAL);
		//}

		//if (cage.getValue().equals(Cage.CRYSTALFULLROOF)) {
		//	Collections.addAll(placeTargets, Offsets.CRYSTALFULLROOF);
		//}

		// TODO: dont use static bridging in offset but calculate them on the fly
		//  based on view direction (or relative direction of target to player)
		//  (add full/half n/e/s/w patterns to append dynamically)

		// TODO: sort offsetList by optimal caging success factor ->
		// sort them by pos y up AND start building behind target

		int blocksPlaced = 0;

		while (blocksPlaced < blocksPerTick.getValDouble()) {

			if (offsetStep >= placeTargets.size()) {
				offsetStep = 0;
				break;
			}

			BlockPos offsetPos = new BlockPos(placeTargets.get(offsetStep));
			BlockPos targetPos = new BlockPos(closestTarget.getPositionVector()).down().add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());

			if (placeBlockInRange(targetPos, range.getValDouble())) {
				blocksPlaced++;
			}

			offsetStep++;

		}


		if (blocksPlaced > 0) {

			if (lastHotbarSlot != playerHotbarSlot && playerHotbarSlot != - 1) {
				mc.player.inventory.currentItem = playerHotbarSlot;
				lastHotbarSlot = playerHotbarSlot;
			}

			if (isSneaking) {
				mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
				isSneaking = false;
			}

		}

	}

	private boolean placeBlockInRange(BlockPos pos, double range) {

		// check if block is already placed
		Block block = mc.world.getBlockState(pos).getBlock();
		if (! (block instanceof BlockAir) && ! (block instanceof BlockLiquid)) {
			return false;
		}

		// check if entity blocks placing
		for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
			if (! (entity instanceof EntityItem) && ! (entity instanceof EntityXPOrb)) {
				return false;
			}
		}

		EnumFacing side = BlockInteractionHelper.getPlaceableSide(pos);

		// check if we have a block adjacent to blockpos to DeltaGUI at
		if (side == null) {
			return false;
		}

		BlockPos neighbour = pos.offset(side);
		EnumFacing opposite = side.getOpposite();

		// check if neighbor can be right clicked
		if (! canBeClicked(neighbour)) {
			return false;
		}

		Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
		Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();

		if (mc.player.getPositionVector().distanceTo(hitVec) > range) {
			return false;
		}

		int obiSlot = findObiInHotbar();

		if (obiSlot == - 1) {
			this.disable();
		}
		playerHotbarSlot = mc.player.inventory.currentItem;
		if (lastHotbarSlot != obiSlot) {
			mc.player.inventory.currentItem = obiSlot;
			lastHotbarSlot = obiSlot;
		}

		if (! isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
			mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
			isSneaking = true;
		}

		if (rotate.getValBoolean()) {
			faceVectorPacketInstant(hitVec);
		}

		mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
		mc.player.swingArm(EnumHand.MAIN_HAND);
		mc.rightClickDelayTimer = 4;

		return true;

	}

	private int findObiInHotbar() {

		// search blocks in hotbar
		int slot = - 1;
		for (int i = 0; i < 9; i++) {

			// filter out non-block items
			ItemStack stack = mc.player.inventory.getStackInSlot(i);

			if (stack == ItemStack.EMPTY || ! (stack.getItem() instanceof ItemBlock)) {
				continue;
			}

			Block block = ((ItemBlock) stack.getItem()).getBlock();
			if (block instanceof BlockObsidian) {
				slot = i;
				break;
			}

		}

		return slot;

	}

	private void findClosestTarget() {

		List<EntityPlayer> playerList = mc.world.playerEntities;

		closestTarget = null;

		for (EntityPlayer target : playerList) {

			if (target == mc.player) {
				continue;
			}

			if (Friends.isFriend(target.getName())) {
				continue;
			}

			if (! EntityUtil.isLiving(target)) {
				continue;
			}

			if ((target).getHealth() <= 0) {
				continue;
			}

			if (closestTarget == null) {
				closestTarget = target;
				continue;
			}

			if (mc.player.getDistance(target) < mc.player.getDistance(closestTarget)) {
				closestTarget = target;
			}

		}

	}


	private enum Cage {
		TRAP, TRAPFULLROOF, CRYSTALEXA, CRYSTAL, CRYSTALFULLROOF
	}

	private static class Offsets {

		private static final Vec3d[] TRAP = {
				new Vec3d(0, 0, - 1),
				new Vec3d(1, 0, 0),
				new Vec3d(0, 0, 1),
				new Vec3d(- 1, 0, 0),
				new Vec3d(0, 1, - 1),
				new Vec3d(1, 1, 0),
				new Vec3d(0, 1, 1),
				new Vec3d(- 1, 1, 0),
				new Vec3d(0, 2, - 1),
				new Vec3d(1, 2, 0),
				new Vec3d(0, 2, 1),
				new Vec3d(- 1, 2, 0),
				new Vec3d(0, 3, - 1),
				new Vec3d(0, 3, 0)
		};

		private static final Vec3d[] TRAPFULLROOF = {
				new Vec3d(0, 0, - 1),
				new Vec3d(1, 0, 0),
				new Vec3d(0, 0, 1),
				new Vec3d(- 1, 0, 0),
				new Vec3d(0, 1, - 1),
				new Vec3d(1, 1, 0),
				new Vec3d(0, 1, 1),
				new Vec3d(- 1, 1, 0),
				new Vec3d(0, 2, - 1),
				new Vec3d(1, 2, 0),
				new Vec3d(0, 2, 1),
				new Vec3d(- 1, 2, 0),
				new Vec3d(0, 3, - 1),
				new Vec3d(1, 3, 0),
				new Vec3d(0, 3, 1),
				new Vec3d(- 1, 3, 0),
				new Vec3d(0, 3, 0)
		};

		private static final Vec3d[] CRYSTALEXA = {
				new Vec3d(0, 0, - 1),
				new Vec3d(0, 1, - 1),
				new Vec3d(0, 2, - 1),
				new Vec3d(1, 2, 0),
				new Vec3d(0, 2, 1),
				new Vec3d(- 1, 2, 0),
				new Vec3d(- 1, 2, - 1),
				new Vec3d(1, 2, 1),
				new Vec3d(1, 2, - 1),
				new Vec3d(- 1, 2, 1),
				new Vec3d(0, 3, - 1),
				new Vec3d(0, 3, 0)
		};

		private static final Vec3d[] CRYSTAL = {
				new Vec3d(0, 0, - 1),
				new Vec3d(1, 0, 0),
				new Vec3d(0, 0, 1),
				new Vec3d(- 1, 0, 0),
				new Vec3d(- 1, 0, 1),
				new Vec3d(1, 0, - 1),
				new Vec3d(- 1, 0, - 1),
				new Vec3d(1, 0, 1),
				new Vec3d(- 1, 1, - 1),
				new Vec3d(1, 1, 1),
				new Vec3d(- 1, 1, 1),
				new Vec3d(1, 1, - 1),
				new Vec3d(0, 2, - 1),
				new Vec3d(1, 2, 0),
				new Vec3d(0, 2, 1),
				new Vec3d(- 1, 2, 0),
				new Vec3d(- 1, 2, 1),
				new Vec3d(1, 2, - 1),
				new Vec3d(0, 3, - 1),
				new Vec3d(0, 3, 0)
		};

		private static final Vec3d[] CRYSTALFULLROOF = {
				new Vec3d(0, 0, - 1),
				new Vec3d(1, 0, 0),
				new Vec3d(0, 0, 1),
				new Vec3d(- 1, 0, 0),
				new Vec3d(- 1, 0, 1),
				new Vec3d(1, 0, - 1),
				new Vec3d(- 1, 0, - 1),
				new Vec3d(1, 0, 1),
				new Vec3d(- 1, 1, - 1),
				new Vec3d(1, 1, 1),
				new Vec3d(- 1, 1, 1),
				new Vec3d(1, 1, - 1),
				new Vec3d(0, 2, - 1),
				new Vec3d(1, 2, 0),
				new Vec3d(0, 2, 1),
				new Vec3d(- 1, 2, 0),
				new Vec3d(- 1, 2, 1),
				new Vec3d(1, 2, - 1),
				new Vec3d(0, 3, - 1),
				new Vec3d(1, 3, 0),
				new Vec3d(0, 3, 1),
				new Vec3d(- 1, 3, 0),
				new Vec3d(0, 3, 0)
		};

	}


}
