package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.ItemUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AutoTotem extends Module {




	public AutoTotem() {
		super("AutoTotem", "Auto Totem. Totem in OffHand.", Category.COMBAT, new Color(173, 234, 165, 255).getRGB());
		BaseBand.settingsManager.rSetting(mode);
		BaseBand.settingsManager.rSetting(minHealth);
		BaseBand.settingsManager.rSetting(faldistance);
		BaseBand.settingsManager.rSetting(lethal);
		BaseBand.settingsManager.rSetting(crapple);
		BaseBand.settingsManager.rSetting(swordGap);
	}



	public Setting mode = new Setting("Item: ", this, "Crystal", new ArrayList<>(Arrays.asList("Crystal", "Gapple", "Totem")));
	private final Setting minHealth = new Setting("Health", this, 5,4,18,false);
	private final Setting swordGap = new Setting("SwordGap", this, true);

	private final Setting lethal = new Setting("Lethal", this, true);

	private final Setting crapple = new Setting("Crapple", this, true);

	private final Setting faldistance = new Setting("FallDistance", this, 5,1,30,true);




	public void renderWorld(){
		int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum() + mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
		this.setMcHudMeta("" + totems);
	}



	public void tick(){
		if (mc.player == null || mc.world == null){
			return;
		}

		if(mc.currentScreen!=null && lethal.getValBoolean()){
			return;
		}

		float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();
		if (hp > minHealth.getValDouble() && !(mc.player.fallDistance >= faldistance.getValDouble())) {
			Item heldItem = mc.player.getHeldItemMainhand().getItem();
			if (swordGap.getValBoolean() && mc.gameSettings.keyBindUseItem.isKeyDown() && (heldItem instanceof ItemSword || heldItem instanceof ItemAxe) && mc.currentScreen == null) {
				if (mode.getValString() != "Gapple")
					ItemUtil.swapToOffhandSlot(getSlot("Gapple"));
			} else {
				ItemUtil.swapToOffhandSlot(getSlot(mode.getValString()));
			}
		} else {
			ItemUtil.swapToOffhandSlot(ItemUtil.getItemSlot(Items.TOTEM_OF_UNDYING));
		}

	}

	private int getSlot(String mode) {
		switch (mode) {
			case "Crystal":
				return ItemUtil.getItemSlot(Items.END_CRYSTAL);
			case "Gapple":
				return ItemUtil.getGappleSlot(crapple.getValBoolean());
			default:
				return ItemUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
		}
	}

}




