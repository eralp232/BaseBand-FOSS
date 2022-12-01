package dev.jess.baseband.client.impl.GUI;


import dev.jess.baseband.client.api.Utils.Wrapper;

import java.text.DecimalFormat;

public class HungerStatsTab extends PinableFrame {

	public DecimalFormat dc = new DecimalFormat("#.##");

	public HungerStatsTab() {
		super("Hunger Stats", new String[]{}, 125);
	}

	public void renderFrame() {

		this.text = new String[]{"Hunger: " + Wrapper.mc.player.getFoodStats().getFoodLevel(), "Saturation: " + dc.format(Wrapper.mc.player.getFoodStats().getSaturationLevel()), "Exhaustion: " + dc.format((int) Wrapper.mc.player.getFoodStats().getSaturationLevel())};

		super.renderFrame();
	}

}

