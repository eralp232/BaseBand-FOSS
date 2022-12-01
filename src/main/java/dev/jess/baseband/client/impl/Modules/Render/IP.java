package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class IP extends Module {
	public IP() {
		super("IP", "~nyaaaaaaaa", Category.RENDER, new Color(222, 22, 222, 255).getRGB());
	}

	public void tick() {
		this.setMcHudMeta(mc.isSingleplayer() ? "127.0.0.1" : mc.currentServerData.serverIP);
	}

}
