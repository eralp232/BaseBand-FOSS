package dev.jess.baseband.client.impl.Modules.Client;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class CatGUI extends Module {
	public CatGUI() {
		super("CatGUI", "Old stuff", Category.CLIENT, new Color(223, 22, 255, 255).getRGB());
	}

	public void tick() {
		mc.displayGuiScreen(BaseBand.catGui);
		this.setToggled(false);
	}
}
