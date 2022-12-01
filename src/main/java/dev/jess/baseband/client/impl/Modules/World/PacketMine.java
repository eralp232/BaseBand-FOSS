package dev.jess.baseband.client.impl.Modules.World;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class PacketMine extends Module {
	public PacketMine() {
		super("PacketMine", "*Yoink!*", Category.WORLD, new Color(31, 100, 100, 255).getRGB());
	}

	public void tick() {
		mc.playerController.blockHitDelay = 0;
	}

	public void disable() {
		mc.playerController.blockHitDelay = 4;
	}
}
