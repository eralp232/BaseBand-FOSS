package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class NoUnicode extends Module {
	public NoUnicode() {
		super("NoUnicode", "description", Category.RENDER, new Color(212, 222, 22, 255).getRGB());
	}
}
