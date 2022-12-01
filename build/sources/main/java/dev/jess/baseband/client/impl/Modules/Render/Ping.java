package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class Ping extends Module {
	public Ping() {
		super("Ping", "~Nyaaa", Category.RENDER, new Color(111, 22, 222, 255).getRGB());
	}

	public void tick() {
		this.setMcHudMeta(String.valueOf(getPing()));
	}

	private int getPing() {
		if (mc.getConnection() == null) {
			return 1;
		} else if (mc.player == null) {
			return - 1;
		} else {
			try {
				return mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
			} catch (NullPointerException ignored) {
			}
			return - 1;
		}
	}

}
