package dev.jess.baseband.client.impl.Modules.Client;


import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ClickGUI extends Module {


	public ClickGUI() {
		super("ClickGUI", "ClickGUI.", Category.CLIENT, new Color(13, 91, 199, 255).getRGB());
		this.setKey(Keyboard.KEY_EQUALS);
	}


	public void enable() {
		mc.displayGuiScreen(BaseBand.gui);
	}


	public void tick() {
		this.setToggled(false);
	}


}
