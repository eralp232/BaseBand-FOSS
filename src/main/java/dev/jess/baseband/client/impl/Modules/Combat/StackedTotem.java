package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.InventoryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.Date;

public class StackedTotem extends Module {


	static final Minecraft mc = Minecraft.getMinecraft();
	public static boolean shouldPrepare = false;
	int totems;
	long selectLast = new Date().getTime();
	long swapLast = new Date().getTime();
	long replaceLast = new Date().getTime();

	public StackedTotem() {
		super("StackedTotem", "8b8t, and Co.", Category.COMBAT, new Color(125, 0, 255, 255).getRGB());
		BaseBand.settingsManager.rSetting(new Setting("Delay", this, 100, 10, 300, true));
		BaseBand.settingsManager.rSetting(new Setting("Second Delay", this, 100, 10, 300, true));
		BaseBand.settingsManager.rSetting(new Setting("Switch Count", this, 6, 3, 63, true));
		BaseBand.settingsManager.rSetting(new Setting("Prepare Count", this, 3, 0, 6, true));
		BaseBand.settingsManager.rSetting(new Setting("Minimum Count", this, 23, 1, 63, true));
	}



	public void disable() {
		this.setMcHudMeta("");
	}


	public void tick() {
		onUpdate();
	}


	public void onUpdate() {
		try {
			if (mc.player.getHeldItemOffhand().getCount() <= BaseBand.settingsManager.getSettingByName(this, "Switch Count").getValDouble() + BaseBand.settingsManager.getSettingByName(this, "Prepare Count").getValDouble()) {
				shouldPrepare = true;
			}

			if (mc.player.getHeldItemOffhand().getCount() > BaseBand.settingsManager.getSettingByName(this, "Switch Count").getValDouble() + BaseBand.settingsManager.getSettingByName(this, "Prepare Count").getValDouble()) {
				shouldPrepare = false;
			}
			totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
			this.setMcHudMeta("" + totems);
			if ((mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING && mc.player.getHeldItemOffhand().getCount() <= BaseBand.settingsManager.getSettingByName(this, "Switch Count").getValDouble()) || mc.player.getHeldItemOffhand().isEmpty()) {
				final int slot = InventoryUtils.findWindowItem(Items.TOTEM_OF_UNDYING, (int) BaseBand.settingsManager.getSettingByName(this, "Minimum Count").getValDouble(), 9, 44);

				if (slot == - 1) return;
				if (mc.currentScreen instanceof GuiContainer) mc.player.closeScreen();

				if (mc.player.inventoryContainer.getSlot(slot).getStack().getItem() != Items.TOTEM_OF_UNDYING) return;
				if (mc.player.inventoryContainer.getSlot(slot).getStack().getCount() <= (int) BaseBand.settingsManager.getSettingByName(this, "Minimum Count").getValDouble())
					return;

				if (new Date().getTime() >= selectLast + BaseBand.settingsManager.getSettingByName(this, "Delay").getValDouble()) {
					selectLast = new Date().getTime();
					mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 8, ClickType.SWAP, mc.player);
				}

				if (new Date().getTime() >= swapLast + BaseBand.settingsManager.getSettingByName(this, "Second Delay").getValDouble()) {
					swapLast = new Date().getTime();
					mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 8, ClickType.SWAP, mc.player);
				}

				if (new Date().getTime() >= replaceLast + BaseBand.settingsManager.getSettingByName(this, "Second Delay").getValDouble() / 3) {
					replaceLast = new Date().getTime();
					mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 8, ClickType.SWAP, mc.player);
				}

				shouldPrepare = false;
			}
		} catch (Exception e) {

		}
	}

}
