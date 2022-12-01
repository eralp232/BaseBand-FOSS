package dev.jess.baseband.client.impl.Modules.Client;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class AzureGUI extends Module {

	public AzureGUI() {
		super("AzureGUI", "math so stupid", Category.CLIENT, new Color(122, 222, 22, 255).getRGB());
	}

	public void tick() {
		mc.displayGuiScreen(BaseBand.azureGUI);
		this.setToggled(false);
	}
}
