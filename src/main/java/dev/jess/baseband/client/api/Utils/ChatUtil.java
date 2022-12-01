package dev.jess.baseband.client.api.Utils;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Listeners.UpdateListener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {
	public static void sendChatMsg(String e) {
		if (BaseBand.isIngame()) {
			try {
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString("[" + "§a" + UpdateListener.CHANGEABLE_NAME + "§r" + "]" + " " + e));
			} catch (Exception ee) {
				ee.printStackTrace();
				BaseBand.log.info(e);
			}
		} else {
			BaseBand.log.info(e);
		}
	}

	public static void print(String e) {
		if (BaseBand.isIngame()) {
			try {
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString(e));
			} catch (Exception ee) {
				ee.printStackTrace();
				BaseBand.log.info(e);
			}
		} else {
			BaseBand.log.info(e);
		}
	}
}

