package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class Anchor extends Module {
	private static final BlockPos[] surroundOffset = {
			new BlockPos(0, - 1, 0), // down
			new BlockPos(0, 0, - 1), // north
			new BlockPos(1, 0, 0), // east
			new BlockPos(0, 0, 1), // south
			new BlockPos(- 1, 0, 0) // west
	};
	public Setting height = new Setting("Height", this, 4, 1, 8, true);

	public Anchor() {
		super("Anchor", "s", Category.COMBAT, new Color(231, 222, 211, 255).getRGB());
		BaseBand.settingsManager.rSetting(height);
	}

	public static boolean getUnsafeSides(BlockPos pos) {
		// block gotta be air
		if (! mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
			return false;
		}

		// block 1 above gotta be air
		if (! mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
			return false;
		}

		// block 2 above gotta be air
		if (! mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
			return false;
		}


		boolean isSafe = true;
		boolean isBedrock = true;

		for (BlockPos offset : surroundOffset) {
			Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
			if (block != Blocks.BEDROCK) {
				isBedrock = false;
			}
			if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN) {
				isSafe = false;
				break;
			}
		}

		return isSafe;
	}

	public void tick() {
		if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null)
			return;

		double blockX = Math.floor(mc.player.posX);
		double blockZ = Math.floor(mc.player.posZ);

		double offsetX = Math.abs(mc.player.posX - blockX);
		double offsetZ = Math.abs(mc.player.posZ - blockZ);


		if (offsetX < 0.3f || offsetX > 0.7f || offsetZ < 0.3f || offsetZ > 0.7f) return;

		BlockPos playerPos = new BlockPos(blockX, mc.player.posY, blockZ);

		if (mc.world.getBlockState(playerPos).getBlock() != Blocks.AIR) return;

		BlockPos currentBlock = playerPos.down();

		for (int i = 0; i <= height.getValDouble(); i++) {
			currentBlock = currentBlock.down();
			if (mc.world.getBlockState(currentBlock).getBlock() != Blocks.AIR) {
				if (getUnsafeSides(currentBlock.up())) {
					mc.player.motionX = 0f;
					mc.player.motionY = -0.5f;
					mc.player.motionZ = 0f;
				}
			}
		}
	}

}
