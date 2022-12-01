package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class CreativeFly extends Module {

	public CreativeFly() {
		super("CreativeFly", "Fly.", Category.MOVEMENT, new Color(110, 17, 115, 255).getRGB());
	}


	public void enable() {
		if (! mc.player.isElytraFlying()) {
			return;
		}
		if (mc.player.onGround) {
			return;
		}
		mc.player.capabilities.isFlying = true;
	}


	public void disable() {
		mc.player.capabilities.isFlying = false;
	}


	public void tick() {
		if (! mc.player.isElytraFlying()) {
			return;
		}
		if (mc.player.onGround) {
			return;
		}
		mc.player.capabilities.isFlying = true;
	}


}
