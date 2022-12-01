package dev.jess.baseband.client.api.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.world.World;

public class Wrapper {
	public static Minecraft mc = Minecraft.getMinecraft();
	public static FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

	public static Minecraft getMinecraft() {
		return mc;
	}

	public static EntityPlayerSP getPlayer() {
		return Wrapper.getMinecraft().player;
	}

	public static World getWorld() {
		return Wrapper.getMinecraft().world;
	}
}
