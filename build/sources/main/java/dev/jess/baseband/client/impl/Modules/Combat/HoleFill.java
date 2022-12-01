package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.BlockInteractionHelper;
import dev.jess.baseband.client.api.Utils.EntityUtil;
import dev.jess.baseband.client.api.Utils.Friends;
import dev.jess.baseband.client.api.Utils.SecondCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HoleFill extends Module {
	private final Setting smart = new Setting("Smart", this, true);
	private final Setting rotate = new Setting("Rotate", this, true);

	private final Setting web = new Setting("Web", this, false);
	private final Setting range = new Setting("Range", this, 4, 1, 6, false);
	private final Setting tickdelay = new Setting("TickDelay", this, 2, 1, 10, true);

	private int delayStep = 0;
	private EntityPlayer closestTarget;

	public HoleFill() {
		super("HoleFill", "Fill holes", Category.COMBAT, new Color(221, 2, 2, 255).getRGB());
		BaseBand.settingsManager.rSetting(smart);
		BaseBand.settingsManager.rSetting(rotate);
		BaseBand.settingsManager.rSetting(web);
		BaseBand.settingsManager.rSetting(range);
		BaseBand.settingsManager.rSetting(tickdelay);
	}

	public static BlockPos getPlayerPos() {
		return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
	}

	public void enable() {
		delayStep = 0;
	}

	public void disable() {
		closestTarget = null;
	}

	public void renderWorld() {
		this.setMcHudMeta(web.getValBoolean()?"Web":"Obsidian");
	}

	public void tick() {
		if (mc.world == null) {
			return;
		}


		if (delayStep < tickdelay.getValDouble()) {
			delayStep++;
			return;
		} else {
			delayStep = 0;
		}


		if (smart.getValBoolean()) {
			findClosestTarget();
		}
		NonNullList<BlockPos> blocks = findCrystalBlocks();
		BlockPos q = null;
		Item ob;

		if(web.getValBoolean()){
			ob=Item.getItemFromBlock(Blocks.WEB);
		}else{
			ob=Item.getItemFromBlock(Blocks.OBSIDIAN);
		}

		int obsidianSlot = mc.player.getHeldItemMainhand().getItem() == ob ? mc.player.inventory.currentItem : - 1;
		if (obsidianSlot == - 1) {
			for (int l = 0; l < 9; ++ l) {
				if (mc.player.inventory.getStackInSlot(l).getItem() == ob) {
					obsidianSlot = l;
					break;
				}
			}
		}
		if (obsidianSlot == - 1) {
			return;
		}
		if(web.getValBoolean()){
			for (BlockPos blockPos : blocks) {
					if (smart.getValBoolean() && isInRange(blockPos))
						q = blockPos;
					else
						q = blockPos;
			}
		}else {
			for (BlockPos blockPos : blocks) {
				if (mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos)).isEmpty())
					if (smart.getValBoolean() && isInRange(blockPos))
						q = blockPos;
					else
						q = blockPos;
			}
		}
		BlockPos render = q;
		if (q != null && mc.player.onGround) {
			int oldSlot = mc.player.inventory.currentItem;
			if (mc.player.inventory.currentItem != obsidianSlot)
				mc.player.inventory.currentItem = obsidianSlot;
			//lookAtPacket(q.x + .5, q.y - .5, q.z + .5, mc.player);
			BlockInteractionHelper.faceVectorPacketInstant(new Vec3d(q));
			BlockInteractionHelper.placeBlockScaffold(render);
			mc.player.swingArm(EnumHand.MAIN_HAND);
			mc.player.inventory.currentItem = oldSlot;
		}
	}

	private boolean IsHole(BlockPos blockPos) {
		BlockPos boost = blockPos.add(0, 1, 0);
		BlockPos boost2 = blockPos.add(0, 0, 0);
		BlockPos boost3 = blockPos.add(0, 0, - 1);
		BlockPos boost4 = blockPos.add(1, 0, 0);
		BlockPos boost5 = blockPos.add(- 1, 0, 0);
		BlockPos boost6 = blockPos.add(0, 0, 1);
		BlockPos boost7 = blockPos.add(0, 2, 0);
		BlockPos boost8 = blockPos.add(0.5, 0.5, 0.5);
		BlockPos boost9 = blockPos.add(0, - 1, 0);
		return mc.world.getBlockState(boost).getBlock() == Blocks.AIR
				&& (mc.world.getBlockState(boost2).getBlock() == Blocks.AIR)
				&& (mc.world.getBlockState(boost7).getBlock() == Blocks.AIR)
				&& ((mc.world.getBlockState(boost3).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK))
				&& ((mc.world.getBlockState(boost4).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK))
				&& ((mc.world.getBlockState(boost5).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK))
				&& ((mc.world.getBlockState(boost6).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK))
				&& (mc.world.getBlockState(boost8).getBlock() == Blocks.AIR)
				&& ((mc.world.getBlockState(boost9).getBlock() == Blocks.OBSIDIAN) || (mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK));
	}

	public BlockPos getClosestTargetPos() {
		if (closestTarget != null) {
			return new BlockPos(Math.floor(closestTarget.posX), Math.floor(closestTarget.posY), Math.floor(closestTarget.posZ));
		} else {
			return null;
		}
	}

	private void findClosestTarget() {

		ArrayList<EntityPlayer> playerList = (ArrayList<EntityPlayer>) mc.world.playerEntities;

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

	private boolean isInRange(BlockPos blockPos) {
		NonNullList<BlockPos> positions = NonNullList.create();
		positions.addAll(
				getSphere(getPlayerPos(), (float) range.getValDouble(), (int) range.getValDouble(), false, true, 0)
						.stream().filter(this::IsHole).collect(Collectors.toList()));
		return positions.contains(blockPos);
	}

	private NonNullList<BlockPos> findCrystalBlocks() {
		NonNullList<BlockPos> positions = NonNullList.create();
		if (smart.getValBoolean() && closestTarget != null)
			positions.addAll(
					getSphere(getClosestTargetPos(), (float) range.getValDouble(), (int) range.getValDouble(), false, true, 0)
							.stream().filter(this::IsHole).filter(this::isInRange).collect(Collectors.toList()));
		else if (! smart.getValBoolean())
			positions.addAll(
					getSphere(getPlayerPos(), (float) range.getValDouble(), (int) range.getValDouble(), false, true, 0)
							.stream().filter(this::IsHole).collect(Collectors.toList()));
		return positions;
	}

	public ArrayList<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
		ArrayList<BlockPos> circleblocks = new ArrayList<>();
		int cx = loc.getX();
		int cy = loc.getY();
		int cz = loc.getZ();
		for (int x = cx - (int) r; x <= cx + r; x++) {
			for (int z = cz - (int) r; z <= cz + r; z++) {
				for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
					if (dist < r * r && ! (hollow && dist < (r - 1) * (r - 1))) {
						BlockPos l = new BlockPos(x, y + plus_y, z);
						circleblocks.add(l);
					}
				}
			}
		}
		return circleblocks;
	}


}
