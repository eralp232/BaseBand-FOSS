package dev.jess.baseband.client.impl.Modules.Misc;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class BaritoneClick extends Module {

	public BaritoneClick() {
		super("BaritoneClick", "", Category.MISC, new Color(232, 122, 124, 255).getRGB());
	}

	public void enable() {
		baritone.api.BaritoneAPI.getProvider().getPrimaryBaritone().openClick();
	}

	public void tick() {
		this.setToggled(false);
	}
}
