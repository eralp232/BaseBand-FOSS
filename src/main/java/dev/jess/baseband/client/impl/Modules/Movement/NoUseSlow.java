package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class NoUseSlow extends Module {
	public NoUseSlow() {
		super("NoUseSlow", "NoSlow", Category.MOVEMENT, new Color(4, 0, 178, 255).getRGB());
	}
}
