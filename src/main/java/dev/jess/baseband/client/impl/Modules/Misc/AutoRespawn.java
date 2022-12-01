package dev.jess.baseband.client.impl.Modules.Misc;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;

import java.awt.*;

public class AutoRespawn extends Module {
	public AutoRespawn() {
		super("AutoRespawn", "breh", Category.MISC, new Color(22, 255, 25, 255).getRGB());
	}

	public void tick() {
		if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().player.getUniqueID() == null)
			return;

		if (Minecraft.getMinecraft().player.isDead || Minecraft.getMinecraft().player.getHealth() < 0) {
			Minecraft.getMinecraft().displayGuiScreen(null);
			Minecraft.getMinecraft().player.respawnPlayer();
		}
		if (Minecraft.getMinecraft().currentScreen instanceof GuiGameOver) {
			Minecraft.getMinecraft().displayGuiScreen(null);
			Minecraft.getMinecraft().player.respawnPlayer();
		}
	}

}
