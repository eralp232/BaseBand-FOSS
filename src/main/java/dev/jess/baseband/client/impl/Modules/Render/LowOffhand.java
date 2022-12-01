package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;

import java.awt.*;

public class LowOffhand extends Module {
	public final Setting r = new Setting("Height", this, 1, 0.1, 1.0, false);


	public LowOffhand() {
		super("LowOffhand", "Minecraft screwery", Category.RENDER, new Color(222, 150, 100, 255).getRGB());
		BaseBand.settingsManager.rSetting(r);
	}

	public void tick() {
		mc.entityRenderer.itemRenderer.equippedProgressOffHand = (float) r.getValDouble();
	}

}
