package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Utils.SecondCounter;

import java.awt.*;

public class UPS extends Module {
	SecondCounter count = new SecondCounter();

	public UPS() {
		super("UPS", "Count updates per second", Category.RENDER, new Color(170, 225, 255, 255).getRGB());
	}

	public void tick() {
		count.increment();
		this.setMcHudMeta("" + count.getCount());
	}

}
