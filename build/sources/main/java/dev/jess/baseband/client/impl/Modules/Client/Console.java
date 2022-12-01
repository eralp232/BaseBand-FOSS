package dev.jess.baseband.client.impl.Modules.Client;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.impl.Console.GuiConsole;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Console extends Module {
	GuiConsole console = new GuiConsole();


	public Console() {
		super("Console", "Rusherhack B8 console.", Category.CLIENT, new Color(159, 0, 147, 224).getRGB());
		this.setKey(Keyboard.KEY_MINUS);
	}

	@Override
	public void enable() {
		mc.displayGuiScreen(console);
		this.setToggled(false);
	}


	@Override
	public void tick() {
		this.setToggled(false);
	}
}
