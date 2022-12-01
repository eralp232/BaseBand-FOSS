package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class EntitySpeed extends Module {


	private final Setting speed = new Setting("Speed", this, 2, 0, 5, false);

	public EntitySpeed() {
		super("EntitySpeed", "Bruuh", Category.MOVEMENT, new Color(128, 8, 8, 255).getRGB());
		BaseBand.settingsManager.rSetting(speed);
	}

	public static double[] forward(final double speed) {
		float forward = Minecraft.getMinecraft().player.movementInput.moveForward;
		float side = Minecraft.getMinecraft().player.movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().player.prevRotationYaw + (Minecraft.getMinecraft().player.rotationYaw - Minecraft.getMinecraft().player.prevRotationYaw) * Minecraft.getMinecraft().getRenderPartialTicks();
		if (forward != 0.0f) {
			if (side > 0.0f) yaw += ((forward > 0.0f) ? - 45 : 45);
			else if (side < 0.0f) yaw += ((forward > 0.0f) ? 45 : - 45);
			side = 0.0f;
			if (forward > 0.0f) forward = 1.0f;
			else if (forward < 0.0f) forward = - 1.0f;
		}
		final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
		final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
		final double posX = forward * speed * cos + side * speed * sin;
		final double posZ = forward * speed * sin - side * speed * cos;
		return new double[]{posX, posZ};
	}

	public void tick() {
		if (mc.player.ridingEntity != null) {

			double[] dir = forward(speed.getValDouble());
			mc.player.ridingEntity.motionX = dir[0];
			mc.player.ridingEntity.motionZ = dir[1];

		}
	}

}
