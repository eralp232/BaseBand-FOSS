package dev.jess.baseband.client.impl.Modules.Client;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Management extends Module {

	public Setting cname = new Setting("Client Name: ", this, "BaseBand", new ArrayList<>(Arrays.asList("BadDragonHack", "BaseBand", "e621hack", "e926hack", "FurryWare", "FurryWare+", "GlutamateWare", "nhack", "OwOHack", "Tatsu", "TransWare", "TTC+", "TTCp+")));


	public Management() {
		super("Management", "Manage client settings", Category.CLIENT, new Color(231, 252, 122, 255).getRGB());
		this.visible = false;
		BaseBand.settingsManager.rSetting(cname);
		this.setToggled(true);
	}
}
