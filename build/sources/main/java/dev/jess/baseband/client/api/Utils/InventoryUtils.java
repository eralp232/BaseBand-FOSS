package dev.jess.baseband.client.api.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

import java.util.ArrayList;

public class InventoryUtils {
	public static int getItemHotbar(Item item) {
		for (int i = 0; i < 9; i++) {
			if (Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem() == item) {
				return i;
			}
		}
		return - 1;
	}

	public static int getItemInventory(Item item, boolean hotbar) {
		for (int i = (hotbar ? 0 : 9); i < 36; i++) {
			if (Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem() == item) {
				return i;
			}
		}
		return - 1;
	}

	public static int getTotalAmountOfItem(Item item) {
		int amountOfItem = 0;
		for (int i = 0; i < 36; i++) {
			ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
			if (stack.getItem() == item)
				amountOfItem += stack.getCount();
		}
		if (Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == item)
			amountOfItem += Minecraft.getMinecraft().player.getHeldItemOffhand().getCount();
		return amountOfItem;
	}

	public static void moveItemToSlot(Integer startSlot, Integer endSlot) {
		Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
		Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, endSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
		Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
	}

	public static void silentSwitchToSlot(int slot) {
		Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(slot));
		Minecraft.getMinecraft().playerController.updateController();
	}

	public static void switchToSlot(int slot) {
		Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(slot));
		Minecraft.getMinecraft().player.inventory.currentItem = slot;
		Minecraft.getMinecraft().playerController.updateController();
	}

	public static void swapSlot(int slot) {
		Minecraft.getMinecraft().player.inventory.currentItem = slot;
	}

	public static int findWindowItem(Item item, int minimum, int maximum) {
		for (int i = minimum; i <= maximum; i++) {
			ItemStack stack = Wrapper.mc.player.inventoryContainer.getSlot(i).getStack();
			if (stack.getItem() == item) {
				return i;
			}
		}

		return - 1;
	}

	public static int findWindowItem(Item item, int count, int minimum, int maximum) {
		for (int i = minimum; i <= maximum; i++) {
			ItemStack stack = Wrapper.mc.player.inventoryContainer.getSlot(i).getStack();
			if (stack.getItem() == item && stack.getCount() >= count) {
				return i;
			}
		}

		return - 1;
	}

	public ArrayList<Integer> getSlots(int min, int max, int itemID) {
		ArrayList<Integer> slots = new ArrayList<Integer>();
		for (int i = min; i <= max; ++ i) {
			if (Item.getIdFromItem(Wrapper.mc.player.inventory.getStackInSlot(i).getItem()) != itemID) continue;
			slots.add(i);
		}
		if (! slots.isEmpty()) {
			return slots;
		}
		return null;
	}

	public ArrayList<Integer> getSlotsHotbar(int itemId) {
		return this.getSlots(0, 8, itemId);
	}

	public ArrayList<Integer> getSlotsNoHotbar(int itemId) {
		return this.getSlots(9, 35, itemId);
	}

	public ArrayList<Integer> getSlotsFullInv(int min, int max, int itemId) {
		ArrayList<Integer> slots = new ArrayList<Integer>();
		for (int i = min; i < max; ++ i) {
			if (Item.getIdFromItem(Wrapper.mc.player.inventoryContainer.getInventory().get(i).getItem()) != itemId)
				continue;
			slots.add(i);
		}
		if (slots.isEmpty()) {
			return slots;
		}
		return null;
	}

	public ArrayList<Integer> getSlotsFullInvHotbar(int itemId) {
		return this.getSlots(36, 44, itemId);
	}

	public ArrayList<Integer> getSlotsFullInvNoHotbar(int itemId) {
		return this.getSlots(9, 35, itemId);
	}

	public int countItem(int min, int max, int itemId) {
		ArrayList<Integer> itemList = this.getSlots(min, max, itemId);
		int currentCount = 0;
		if (itemList != null) {
			for (int i : itemList) {
				currentCount += Wrapper.mc.player.inventory.getStackInSlot(i).getCount();
			}
		}
		return currentCount;
	}

	public void swapSlotToItem(int itemID) {
		if (this.getSlotsHotbar(itemID) != null) {
			InventoryUtils.swapSlot(this.getSlotsHotbar(itemID).get(0));
		}
	}
}
