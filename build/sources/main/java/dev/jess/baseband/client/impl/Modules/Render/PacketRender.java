package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class PacketRender extends Module {
	private static float yaw = 0;
	private static float pitch = 0;

	public PacketRender() {
		super("PacketRender","", Category.RENDER, new Color(232,11,22,255).getRGB());
	}



	public static float getYaw() {
		return yaw;
	}

	public static float getPitch() {
		return pitch;
	}

	public static void setYaw(float yaw) {
		PacketRender.yaw = yaw;
	}

	public static void setPitch(float pitch) {
		PacketRender.pitch = pitch;
	}
}
