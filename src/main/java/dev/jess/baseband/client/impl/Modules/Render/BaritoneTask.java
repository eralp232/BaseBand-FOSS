package dev.jess.baseband.client.impl.Modules.Render;

import baritone.api.BaritoneAPI;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class BaritoneTask extends Module {
	public BaritoneTask() {
		super("BaritoneTask", "Bruh", Category.RENDER, new Color(212, 159, 255, 255).getRGB());
	}


	public void tick() {
		this.setMcHudMeta(getTask());
	}

	public String getTask() {
		if (BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().getGoal() == null) {
			if (BaritoneAPI.getProvider().getPrimaryBaritone().getExploreProcess() == null) {
				if (BaritoneAPI.getProvider().getPrimaryBaritone().getFarmProcess() == null) {
					if (BaritoneAPI.getProvider().getPrimaryBaritone().getBuilderProcess() == null) {
						if (BaritoneAPI.getProvider().getPrimaryBaritone().getMineProcess() == null) {
							return "None";
						} else {
							return BaritoneAPI.getProvider().getPrimaryBaritone().getMineProcess().toString();
						}
					} else {
						return BaritoneAPI.getProvider().getPrimaryBaritone().getBuilderProcess().toString();
					}
				} else {
					return BaritoneAPI.getProvider().getPrimaryBaritone().getFarmProcess().toString();
				}
			} else {
				return BaritoneAPI.getProvider().getPrimaryBaritone().getExploreProcess().toString();
			}
		} else {
			return BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().getGoal().toString();
		}
	}
}
