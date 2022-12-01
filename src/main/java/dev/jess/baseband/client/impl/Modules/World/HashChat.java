package dev.jess.baseband.client.impl.Modules.World;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class HashChat extends Module {


	private final Setting mode = new Setting("Mode: ", this, "SHA-1", new ArrayList<>(Arrays.asList("SHA-1", "SHA-256", "SHA-384", "SHA-512", "MD-2", "MD-5")));

	private final Setting salt = new Setting("Salt", this, false);

	public HashChat() {
		super("HashChat", "Hash Chat Messages", Category.WORLD, new Color(187, 111, 192, 255).getRGB());
		BaseBand.settingsManager.rSetting(mode);
		BaseBand.settingsManager.rSetting(salt);

	}

	public void disable() {
		this.setMcHudMeta("");
	}

	public void tick() {
		this.setMcHudMeta(mode.getValString());
	}

}
