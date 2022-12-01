package dev.jess.baseband.client.impl.Modules.Client;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;

import java.awt.*;

public class FriendSystem extends Module {


	public static FriendSystem INSTANCE;
	private final Setting notify = new Setting("NotifyFriended", this, false);
	private final Setting disable3 = new Setting("DisableGlobally", this, false);
	public boolean notify2 = false;
	public boolean disable2 = false;


	public FriendSystem() {
		super("FriendSystem", "Yawn", Category.CLIENT, new Color(2, 24, 255, 255).getRGB());
		this.visible = false;
		this.setToggled(true);
		INSTANCE = this;
		BaseBand.settingsManager.rSetting(notify);
		BaseBand.settingsManager.rSetting(disable3);
	}


	@Override
	public void disable() {
		this.setToggled(true);
	}

}
