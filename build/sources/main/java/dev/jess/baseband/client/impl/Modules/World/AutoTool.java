package dev.jess.baseband.client.impl.Modules.World;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.world.GameType;

import java.awt.*;

public class AutoTool extends Module {
	public int slot = - 1;
	public int currentSlot = - 1;

	public AutoTool() {
		super("AutoTool", "Satisfy the lust", Category.WORLD, new Color(126, 123, 255, 255).getRGB());
	}

	public static int findTool(Block block) {
		float best = - 1.0F;
		int index = - 1;
		for (int i = 0; i < 9; i++) {
			ItemStack itemStack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
			if (itemStack != null) {
				float str = itemStack.getItem().getDestroySpeed(itemStack, block.getDefaultState());
				if (str > best) {
					best = str;
					index = i;
				}
			}
		}
		return index;
	}

	public void tick() {
		if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().player.getUniqueID() == null)
			return;

		if (! Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown() && currentSlot != - 1) {
			Minecraft.getMinecraft().player.inventory.currentItem = currentSlot;
			currentSlot = - 1;

		}
	}

	public boolean onPacketSend(Packet<?> packet) {

		Packet pack2 = null;

		try {
			pack2 = packet;
		} catch (Exception e) {
			return false;
		}

		if (pack2 != null && pack2 instanceof CPacketPlayerDigging) {
			CPacketPlayerDigging pck = (CPacketPlayerDigging) pack2;
			if (Minecraft.getMinecraft().playerController.getCurrentGameType() != GameType.CREATIVE && pck.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
				if (currentSlot == - 1) {
					currentSlot = Minecraft.getMinecraft().player.inventory.currentItem;
				}
				int bestIndex = findTool(Minecraft.getMinecraft().world.getBlockState(pck.getPosition()).getBlock());
				if (bestIndex != - 1) {
					if (slot == - 1) {
						slot = Minecraft.getMinecraft().player.inventory.currentItem;
					}
					Minecraft.getMinecraft().player.inventory.currentItem = bestIndex;
				}

			}
		}
		return false;
	}
}
