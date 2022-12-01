package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

import java.awt.*;

public class AutoXP extends Module {

	private final Setting lookPitch = new Setting("Look Pitch", this, 90, 0, 100, false);


	private final Setting activatePitch = new Setting("Activate Pitch", this, 70, - 80, 90, false);

	private final Setting packet = new Setting("Packet", this, false);


	public AutoXP() {
		super("AutoXP", "Automatically Experience", Category.COMBAT, new Color(0, 176, 255, 255).getRGB());
		BaseBand.settingsManager.rSetting(lookPitch);
		BaseBand.settingsManager.rSetting(activatePitch);
		BaseBand.settingsManager.rSetting(packet);
	}


	public static int getHotbarItemSlot(final Item item) {
		for (int i = 0; i < 9; ++ i) {
			if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
				return i;
			}
		}
		return - 1;
	}


	public void tick() {
		if (mc.currentScreen == null && mc.player != null && mc.world != null && mc.player.rotationPitch >= activatePitch.getValDouble()) {
			int oldPitch = (int) mc.player.rotationPitch;
			int oldSlot = mc.player.inventory.currentItem;

			if (getHotbarItemSlot(Items.EXPERIENCE_BOTTLE) == - 1) return;

			if (packet.getValBoolean()) {
				mc.player.connection.sendPacket(new CPacketHeldItemChange(getHotbarItemSlot(Items.EXPERIENCE_BOTTLE)));
			} else {
				mc.player.inventory.currentItem = getHotbarItemSlot(Items.EXPERIENCE_BOTTLE);
			}

			mc.player.rotationPitch = (float) lookPitch.getValDouble();
			mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, (float) lookPitch.getValDouble(), true));
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
			mc.player.rotationPitch = oldPitch;

			if (packet.getValBoolean()) {
				mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
			} else {
				mc.player.inventory.currentItem = oldSlot;
			}
		}
	}


}
