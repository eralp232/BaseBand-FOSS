package dev.jess.baseband.client.impl.Modules.World;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class ChatTimeStamps extends Module {
	public ChatTimeStamps() {
		super("ChatTimeStamps", "Timestamps in Chat", Category.WORLD, new Color(22, 111, 222, 255).getRGB());
	}
}
