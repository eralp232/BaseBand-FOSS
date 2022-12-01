package dev.jess.baseband.client.impl.Modules.Client;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;

public class Colorr extends Module {


	public static Colorr INSTANCE;


	public final Setting hue = new Setting("Hue", this, 30, 1, 356, true);
	public final Setting saturation = new Setting("Saturation", this, 30, 1, 100, true);
	public final Setting brightness = new Setting("Brightness", this, 30, 1, 100, true);

	public Colorr() {
		super("Color", "Yikes.", Category.CLIENT, new java.awt.Color(22, 112, 255, 255).getRGB());
		INSTANCE = this;
		BaseBand.settingsManager.rSetting(hue);
		BaseBand.settingsManager.rSetting(saturation);
		BaseBand.settingsManager.rSetting(brightness);
	}


}
