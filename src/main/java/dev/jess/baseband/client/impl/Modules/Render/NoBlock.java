package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class NoBlock extends Module {
	public NoBlock() {
		super("NoBlock", "LOL", Category.RENDER, new Color(120, 26, 208, 255).getRGB());
	}
}
