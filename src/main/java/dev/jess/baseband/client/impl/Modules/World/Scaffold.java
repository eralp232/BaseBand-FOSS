package dev.jess.baseband.client.impl.Modules.World;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Utils.BlockInteractionHelper;
import dev.jess.baseband.client.api.Utils.TimerUtil;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class Scaffold extends Module {
	TimerUtil timer = new TimerUtil();

	public Scaffold() {
		super("Scaffold", "b8!", Category.WORLD, new Color(213, 222, 22, 255).getRGB());
	}

	public static boolean isNull(final ItemStack stack) {
		return stack == null || stack.getItem() instanceof ItemAir;
	}

	public void tick() {
		timer.reset();
		BlockPos playerBlock = new BlockPos(
				Wrapper.mc.player.getRidingEntity() != null ? Wrapper.mc.player.getRidingEntity().posX
						: Wrapper.mc.player.posX,
				Wrapper.mc.player.getRidingEntity() != null ? Wrapper.mc.player.getRidingEntity().posY
						: Wrapper.mc.player.posY,
				Wrapper.mc.player.getRidingEntity() != null ? Wrapper.mc.player.getRidingEntity().posZ
						: Wrapper.mc.player.posZ);

		// timer.setCurrentMS();
		if (Wrapper.mc.world.isAirBlock(playerBlock.add(0, - 1, 0))
				|| Wrapper.mc.world.getBlockState(playerBlock.add(0, - 1, 0)).getBlock() == Blocks.SNOW_LAYER
				|| Wrapper.mc.world.getBlockState(playerBlock.add(0, - 1, 0)).getBlock() == Blocks.TALLGRASS
				|| Wrapper.mc.world.getBlockState(playerBlock.add(0, - 1, 0)).getBlock() instanceof BlockLiquid) {
			if (this.isValidBlock(playerBlock.add(0, - 2, 0))) {
				this.place(playerBlock.add(0, - 1, 0), EnumFacing.UP);
			} else if (this.isValidBlock(playerBlock.add(- 1, - 1, 0))) {
				this.place(playerBlock.add(0, - 1, 0), EnumFacing.EAST);
			} else if (this.isValidBlock(playerBlock.add(1, - 1, 0))) {
				this.place(playerBlock.add(0, - 1, 0), EnumFacing.WEST);
			} else if (this.isValidBlock(playerBlock.add(0, - 1, - 1))) {
				this.place(playerBlock.add(0, - 1, 0), EnumFacing.SOUTH);
			} else if (this.isValidBlock(playerBlock.add(0, - 1, 1))) {
				this.place(playerBlock.add(0, - 1, 0), EnumFacing.NORTH);
			} else if (this.isValidBlock(playerBlock.add(1, - 1, 1))) {
				if (this.isValidBlock(playerBlock.add(0, - 1, 1))) {
					this.place(playerBlock.add(0, - 1, 1), EnumFacing.NORTH);
				}
				this.place(playerBlock.add(1, - 1, 1), EnumFacing.EAST);
			} else if (this.isValidBlock(playerBlock.add(- 1, - 1, 1))) {
				if (this.isValidBlock(playerBlock.add(- 1, - 1, 0))) {
					this.place(playerBlock.add(0, - 1, 1), EnumFacing.WEST);
				}
				this.place(playerBlock.add(- 1, - 1, 1), EnumFacing.SOUTH);
			} else if (this.isValidBlock(playerBlock.add(1, - 1, 1))) {
				if (this.isValidBlock(playerBlock.add(0, - 1, 1))) {
					this.place(playerBlock.add(0, - 1, 1), EnumFacing.SOUTH);
				}
				this.place(playerBlock.add(1, - 1, 1), EnumFacing.WEST);
			} else if (this.isValidBlock(playerBlock.add(1, - 1, 1))) {
				if (this.isValidBlock(playerBlock.add(0, - 1, 1))) {
					this.place(playerBlock.add(0, - 1, 1), EnumFacing.EAST);
				}
				this.place(playerBlock.add(1, - 1, 1), EnumFacing.NORTH);
			}
		}

	}

	@SuppressWarnings("deprecation")
	private boolean isValidBlock(BlockPos pos) {
		Block b = Wrapper.mc.world.getBlockState(pos).getBlock();
		return ! (b instanceof BlockLiquid) && b.getMaterial(null) != Material.AIR;
	}

	public void place(BlockPos pos, EnumFacing face) {
		if (face == EnumFacing.UP) {
			pos = pos.add(0, - 1, 0);
		} else if (face == EnumFacing.NORTH) {
			pos = pos.add(0, 0, 1);
		} else if (face == EnumFacing.SOUTH) {
			pos = pos.add(0, 0, - 1);
		} else if (face == EnumFacing.EAST) {
			pos = pos.add(- 1, 0, 0);
		} else if (face == EnumFacing.WEST) {
			pos = pos.add(1, 0, 0);
		}
		final int oldSlot = Wrapper.mc.player.inventory.currentItem;
		int newSlot = - 1;
		for (int i = 0; i < 9; ++ i) {
			final ItemStack stack = Wrapper.mc.player.inventory.getStackInSlot(i);
			if (! isNull(stack) && stack.getItem() instanceof ItemBlock
					&& Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock()) {
				newSlot = i;
				break;
			}
		}
		if (newSlot == - 1) {
			return;
		}

		if (Wrapper.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock) {

		} else {
			Wrapper.mc.player.connection.sendPacket(new CPacketHeldItemChange(newSlot));
			Wrapper.mc.player.inventory.currentItem = newSlot;
			Wrapper.mc.playerController.updateController();
		}
		if (Wrapper.mc.gameSettings.keyBindJump.isPressed()) {
			Wrapper.mc.player.motionX *= 0.3;
			Wrapper.mc.player.motionZ *= 0.3;
			Wrapper.mc.player.jump();
			if (timer.passedMs(1500) && Wrapper.mc.gameSettings.keyBindJump.isPressed()) {
				Wrapper.mc.player.motionY = - 0.28;
				timer.reset();
			}
			if (! Wrapper.mc.gameSettings.keyBindJump.isPressed()) {
				timer.reset();
			}
		}

		BlockInteractionHelper.faceVectorPacketInstant(new Vec3d(pos));
		Wrapper.mc.playerController.processRightClickBlock(Wrapper.mc.player, Wrapper.mc.world, pos, face,
				new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
		Wrapper.mc.player.swingArm(EnumHand.MAIN_HAND);
		Wrapper.mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
		Wrapper.mc.player.inventory.currentItem = oldSlot;
		Wrapper.mc.playerController.updateController();
		// this.delay = 0;
	}


}
