package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class NoPotionIcon extends Module {
	public NoPotionIcon() {
		super("NoPotionIcon", "Custom.", Category.RENDER, new Color(50, 70, 200, 255).getRGB());
	}
}
