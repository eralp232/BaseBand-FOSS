package dev.jess.baseband.client.impl.Modules.Misc;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import net.minecraft.client.multiplayer.ServerData;

import java.awt.*;

public class AutoReconnect extends Module {
	public static ServerData serverData;


	public AutoReconnect() {
		super("AutoReconnect", "kami", Category.MISC, new Color(150, 222, 50, 255).getRGB());
	}


}


