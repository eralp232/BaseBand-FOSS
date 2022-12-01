package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Listeners.PacketListener;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class TPS extends Module {
	public TPS() {
		super("TPS", "~Nyaaa", Category.RENDER, new Color(22, 220, 200, 255).getRGB());
	}


	public void tick() {
		this.setMcHudMeta(String.format("%.4f", PacketListener.tps));
	}
}
