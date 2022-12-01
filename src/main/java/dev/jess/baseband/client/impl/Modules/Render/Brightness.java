package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class Brightness extends Module {
	public Brightness() {
		super("Brightness", "Increase Gamma", Category.RENDER, new Color(0, 124, 109, 255).getRGB());
	}


	public void enable() {
		mc.gameSettings.gammaSetting = 100.0f;
	}

	public void disable() {
		mc.gameSettings.gammaSetting = 1.0f;
	}

	public void tick() {
		mc.gameSettings.gammaSetting = 100.0f;
	}


}
