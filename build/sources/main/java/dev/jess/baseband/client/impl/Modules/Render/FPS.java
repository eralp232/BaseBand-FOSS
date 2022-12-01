package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Utils.SecondCounter;

import java.awt.*;

public class FPS extends Module {
	SecondCounter counter = new SecondCounter();


	public FPS() {
		super("FPS", "~Nyaaa", Category.RENDER, new Color(222, 22, 222, 255).getRGB());
	}

	public void renderWorld() {
		counter.increment();
	}


	public void tick() {
		this.setMcHudMeta("" + counter.getCount());
	}
}
