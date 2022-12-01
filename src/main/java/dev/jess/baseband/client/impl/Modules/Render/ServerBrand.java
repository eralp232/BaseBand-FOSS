package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class ServerBrand extends Module {
	public ServerBrand() {
		super("ServerBrand", "~nyaaaa", Category.RENDER, new Color(222, 111, 111, 255).getRGB());
	}

	public void tick() {
		if (mc.player != null) {
			String brand = (mc.isIntegratedServerRunning() ? "Singleplayer" : (mc.player.getServerBrand() == null ? "Unknown" : mc.player.getServerBrand()));
			this.setMcHudMeta(brand);
		}
	}

}
