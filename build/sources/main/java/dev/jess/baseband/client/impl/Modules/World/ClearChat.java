package dev.jess.baseband.client.impl.Modules.World;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class ClearChat extends Module {
	public ClearChat() {
		super("ClearChat", "description", Category.WORLD, new Color(222, 11, 122, 255).getRGB());
	}
}
