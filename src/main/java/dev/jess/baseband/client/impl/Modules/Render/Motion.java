package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class Motion extends Module {
	public Motion() {
		super("Motion", "ORIGINAL FEATURE?????", Category.RENDER, new Color(222, 206, 124, 255).getRGB());
	}

	public void tick() {
		this.setMcHudMeta("X:" + String.format("%.2f", Minecraft.getMinecraft().player.motionX) + " Y:" + String.format("%.2f", Minecraft.getMinecraft().player.motionY) + " Z:" + String.format("%.2f", Minecraft.getMinecraft().player.motionZ));
	}
}
