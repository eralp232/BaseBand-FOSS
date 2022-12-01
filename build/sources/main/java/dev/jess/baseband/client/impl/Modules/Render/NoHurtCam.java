package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class NoHurtCam extends Module {

	private static NoHurtCam INSTANCE;

	public NoHurtCam() {
		super("NoHurtCam", "KAMII", Category.RENDER, new Color(211, 22, 100, 255).getRGB());
		INSTANCE = this;
	}

	public static boolean shouldDisable() {
		return INSTANCE != null && INSTANCE.isToggled();
	}
}
