package dev.jess.baseband.client.impl.Modules.World;

import dev.jess.baseband.client.api.Listeners.UpdateListener;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.awt.*;
import java.util.Objects;

public class ChatSuffix extends Module {


	public ChatSuffix() {
		super("ChatSuffix", "Adds a Suffix to Your Chat Messages.", Category.WORLD, new Color(84, 42, 190, 255).getRGB());
	}


	private String toUnicode(String s) {
		return s.toLowerCase()
				.replace("a", "\u1d00")
				.replace("b", "\u0299")
				.replace("c", "\u1d04")
				.replace("d", "\u1d05")
				.replace("e", "\u1d07")
				.replace("f", "\ua730")
				.replace("g", "\u0262")
				.replace("h", "\u029c")
				.replace("i", "\u026a")
				.replace("j", "\u1d0a")
				.replace("k", "\u1d0b")
				.replace("l", "\u029f")
				.replace("m", "\u1d0d")
				.replace("n", "\u0274")
				.replace("o", "\u1d0f")
				.replace("p", "\u1d18")
				.replace("q", "\u01eb")
				.replace("r", "\u0280")
				.replace("s", "\ua731")
				.replace("t", "\u1d1b")
				.replace("u", "\u1d1c")
				.replace("v", "\u1d20")
				.replace("w", "\u1d21")
				.replace("x", "\u02e3")
				.replace("y", "\u028f")
				.replace("z", "\u1d22");
	}

	public boolean onPacketSend(Packet<?> packet) {

		if (packet instanceof CPacketChatMessage) {
			String s = ((CPacketChatMessage) packet).getMessage();

			if (! s.startsWith("/") && ! s.startsWith("!") && ! s.startsWith(".") && ! s.startsWith(",") && ! s.startsWith("*") && ! s.startsWith("=") && ! Objects.requireNonNull(ModuleRegistry.getModule("ChatCrypt")).isToggled()) {
				String originalmessage = s;
				s = originalmessage + " " + "\u226a " + toUnicode(UpdateListener.CHANGEABLE_NAME) + "™";
			}

			if (s.length() > 256) {
				ChatUtil.sendChatMsg("Message too long, couldn't send!");
				return true;
			}
			((CPacketChatMessage) packet).message = s;
		}

		return false;
	}


}
