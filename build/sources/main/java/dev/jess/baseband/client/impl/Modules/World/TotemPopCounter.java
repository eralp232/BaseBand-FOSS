package dev.jess.baseband.client.impl.Modules.World;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.awt.*;
import java.util.HashMap;

public class TotemPopCounter extends Module {


	private final Setting chat = new Setting("Public", this, false);
	private final Setting range = new Setting("Range", this, 5, 2, 15, true);
	private HashMap<String, Integer> popList = new HashMap();

	public TotemPopCounter() {
		super("TotemPopCounter", "Count Totem Pops", Category.WORLD, new Color(199, 0, 0, 255).getRGB());
		BaseBand.settingsManager.rSetting(chat);
		BaseBand.settingsManager.rSetting(range);
	}

	public void disable() {
		popList.clear();
	}


	public void tick() {
		for (EntityPlayer player : mc.world.playerEntities) {
			if (player.getHealth() <= 0) {
				if (popList.containsKey(player.getName())) {
					if (chat.getValBoolean() && mc.player.getDistance(player) <= range.getValDouble() && player != mc.player) {
						mc.player.connection.sendPacket(new CPacketChatMessage("BaseBand on Top, " + player.getName() + "!"));
					}

					ChatUtil.sendChatMsg(ChatFormatting.GREEN + player.getName() + " died after popping " + popList.get(player.getName()) + " totems EZ");
					popList.remove(player.getName(), popList.get(player.getName()));
				}
			}
		}
	}


	public void onPop(Entity e) {
		if (popList == null) {
			popList = new HashMap<>();
		}


		if (popList.get(e.getName()) == null) {
			popList.put(e.getName(), 1);
			if (chat.getValBoolean() && mc.player.getDistance(e) <= range.getValDouble() && e != mc.player) {
				mc.player.connection.sendPacket(new CPacketChatMessage("EZ Pop, " + e.getName()));
			}
			ChatUtil.sendChatMsg(ChatFormatting.GREEN + e.getName() + " popped " + 1 + " totem");
		} else if (! (popList.get(e.getName()) == null)) {
			int popCounter = popList.get(e.getName());
			int newPopCounter = popCounter += 1;
			popList.put(e.getName(), newPopCounter);
			if (chat.getValBoolean() && mc.player.getDistance(e) <= range.getValDouble() && e != mc.player) {
				mc.player.connection.sendPacket(new CPacketChatMessage("EZ, You Popped " + newPopCounter + " Times Thanks to BaseBand, " + e.getName()));
			}
			ChatUtil.sendChatMsg(ChatFormatting.GREEN + e.getName() + " popped " + newPopCounter + " totems");
		}
	}


	public boolean onPacketReceive(Packet<?> packet) {
		if (packet instanceof SPacketEntityStatus) {
			SPacketEntityStatus packet2 = (SPacketEntityStatus) packet;
			if (packet2.getOpCode() == 35) {
				Entity entity = packet2.getEntity(mc.world);
				onPop(entity);
			}
		}

		return false;
	}
}
