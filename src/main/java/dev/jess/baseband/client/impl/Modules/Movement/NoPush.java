package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class NoPush extends Module {
	public NoPush() {
		super("NoPush", "Mmmmm QOL", Category.MOVEMENT, new Color(255, 182, 206, 255).getRGB());
	}
}
