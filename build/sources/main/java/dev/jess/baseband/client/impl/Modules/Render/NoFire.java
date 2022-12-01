package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class NoFire extends Module {

	public NoFire() {
		super("NoFire", "Yay", Category.RENDER, new Color(145, 30, 190).getRGB());
	}

	//See UpdateListener.class
}
