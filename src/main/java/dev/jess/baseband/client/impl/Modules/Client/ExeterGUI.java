package dev.jess.baseband.client.impl.Modules.Client;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.impl.FutureGUI.ClickGui;

import java.awt.*;

public class ExeterGUI extends Module {
	public ExeterGUI() {
		super("ExeterGUI", "MFW!", Category.CLIENT, new Color(255, 255, 255, 255).getRGB());
	}

	public void tick() {
		mc.displayGuiScreen(ClickGui.getClickGui());
		this.setToggled(false);
	}
}
