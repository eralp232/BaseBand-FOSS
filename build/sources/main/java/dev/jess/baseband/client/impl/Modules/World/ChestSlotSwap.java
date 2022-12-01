package dev.jess.baseband.client.impl.Modules.World;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ChestSlotSwap extends Module {
	boolean disable = false;


	public ChestSlotSwap() {
		super("ChestSlotSwap", "", Category.WORLD, new Color(122, 152, 251, 255).getRGB());
	}

	public void enable() {
		if (Wrapper.mc.player != null) {
			disable = false;
			final InventoryPlayer items = Wrapper.mc.player.inventory;
			final ItemStack body = items.armorItemInSlot(2);
			final String body2 = body.getItem().getItemStackDisplayName(body);
			if (body2.equals("Air")) {
				int t = 0;
				int c = 0;
				for (int i = 9; i < 45; ++ i) {
					if (Wrapper.mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA) {
						t = i;
						break;
					}
				}
				if (t != 0) {
					ChatUtil.sendChatMsg("Equipping Elytra.");
					Wrapper.mc.playerController.windowClick(0, t, 0, ClickType.PICKUP, Wrapper.mc.player);
					Wrapper.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, Wrapper.mc.player);
				}
				if (t == 0) {
					for (int i = 9; i < 45; ++ i) {
						if (Wrapper.mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_CHESTPLATE) {
							c = i;
							break;
						}
					}
					if (c != 0) {
						ChatUtil.sendChatMsg("Equipping Chestplate.");
						Wrapper.mc.playerController.windowClick(0, c, 0, ClickType.PICKUP, Wrapper.mc.player);
						Wrapper.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, Wrapper.mc.player);
					}
				}
				if (c == 0 && t == 0) {
					ChatUtil.sendChatMsg("You do not have an Elytra or a Chestplate in your inventory. Disabling");
				}

				disable = true;

			}
			if (body2.equals("Elytra")) {
				int t = 0;
				for (int j = 9; j < 45; ++ j) {
					if (Wrapper.mc.player.inventory.getStackInSlot(j).getItem() == Items.DIAMOND_CHESTPLATE) {
						t = j;
						break;
					}
				}
				if (t != 0) {
					int l = 0;
					ChatUtil.sendChatMsg("Equipping Chestplate");
					Wrapper.mc.playerController.windowClick(0, t, 0, ClickType.PICKUP, Wrapper.mc.player);
					Wrapper.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, Wrapper.mc.player);
					for (int i = 9; i < 45; ++ i) {
						if (Wrapper.mc.player.inventory.getStackInSlot(i).getItem() == Items.AIR) {
							l = i;
							break;
						}
					}
					Wrapper.mc.playerController.windowClick(0, l, 0, ClickType.PICKUP, Wrapper.mc.player);
				}
				if (t == 0) {
					ChatUtil.sendChatMsg("Failed to Swap!");
				}
				disable = true;
			}
			if (body2.equals("Diamond Chestplate")) {
				int t = 0;
				for (int j = 9; j < 45; ++ j) {
					if (Wrapper.mc.player.inventory.getStackInSlot(j).getItem() == Items.ELYTRA) {
						t = j;
						break;
					}
				}
				if (t != 0) {
					int u = 0;
					ChatUtil.sendChatMsg("Equipping Elytra");
					Wrapper.mc.playerController.windowClick(0, t, 0, ClickType.PICKUP, Wrapper.mc.player);
					Wrapper.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, Wrapper.mc.player);
					for (int i = 9; i < 45; ++ i) {
						if (Wrapper.mc.player.inventory.getStackInSlot(i).getItem() == Items.AIR) {
							u = i;
							break;
						}
					}
					Wrapper.mc.playerController.windowClick(0, u, 0, ClickType.PICKUP, Wrapper.mc.player);
				}
				if (t == 0) {
					ChatUtil.sendChatMsg("Failed to Swap!");
				}
				disable = true;
			}
		}
	}


	public void tick() {
		if (disable) {
			this.disable();
			this.setToggled(false);
		}
	}


}
