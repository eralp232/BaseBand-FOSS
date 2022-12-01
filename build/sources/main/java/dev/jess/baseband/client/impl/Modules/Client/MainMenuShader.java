package dev.jess.baseband.client.impl.Modules.Client;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenuShader extends Module {


	public Setting shader = new Setting("Shader: ", this, "Minecraft", new ArrayList<>(Arrays.asList("BaseBand", "Blue Hole", "Cube", "Cubic Pulse", "Dalek", "Dj", "DNA", "Doom", "Flappy Bird", "Gas", "Leafy", "Minecraft", "Nebula", "Steam", "TudbuT", "Ukraine")));

	public MainMenuShader() {
		super("MainMenuShader", "Rusherhack moment", Category.CLIENT, new Color(191, 135, 252, 233).getRGB());
		BaseBand.settingsManager.rSetting(shader);
	}


	public void tick() {
		setMcHudMeta(shader.getValString());
	}

}
