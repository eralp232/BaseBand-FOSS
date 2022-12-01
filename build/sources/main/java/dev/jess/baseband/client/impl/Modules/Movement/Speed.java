package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class Speed extends Module {
	int delay = 0;


	public Speed() {
		super("Speed", "Yyyy", Category.MOVEMENT, new Color(22, 111, 111, 255).getRGB());
	}

	public static float ny() {
		float v = Minecraft.getMinecraft().player.rotationYaw;
		if (Minecraft.getMinecraft().player.moveForward < 0.0f) {
			v += 180.0f;
		}
		float v2 = 1.0f;
		if (Minecraft.getMinecraft().player.moveForward < 0.0f) {
			v2 = - 0.5f;
		} else if (Minecraft.getMinecraft().player.moveForward > 0.0f) {
			v2 = 0.5f;
		}
		if (Minecraft.getMinecraft().player.moveStrafing > 0.0f) {
			v -= 90.0f * v2;
		}
		if (Minecraft.getMinecraft().player.moveStrafing < 0.0f) {
			v += 90.0f * v2;
		}
		v *= 0.017453292f;
		return v;
	}

	public void tick() {
		if (mc.player == null) return;
		delay++;
		if (delay >= 2) {
			this.a(0.405, 0.22f, 1.0064f);
			delay = 0;
		}
	}

	private void a(final double motionY, final float n, final double n2) {
		final boolean v2 = (Minecraft.getMinecraft().player.moveForward != 0.0f) || Minecraft.getMinecraft().player.moveForward > 0.0f;
		if (v2 || Minecraft.getMinecraft().player.moveStrafing != 0.0f) {
			Minecraft.getMinecraft().player.setSprinting(true);
			if (Minecraft.getMinecraft().player.onGround) {
				Minecraft.getMinecraft().player.motionY = motionY;
				final float a = ny();
				final EntityPlayerSP player = Minecraft.getMinecraft().player;
				player.motionX -= MathHelper.sin(a) * n;
				final EntityPlayerSP player2 = Minecraft.getMinecraft().player;
				player2.motionZ += MathHelper.cos(a) * n;
			} else {
				final double sqrt = Math.sqrt(Minecraft.getMinecraft().player.motionX * Minecraft.getMinecraft().player.motionX + Minecraft.getMinecraft().player.motionZ * Minecraft.getMinecraft().player.motionZ);
				final double n3 = ny();
				Minecraft.getMinecraft().player.motionX = - Math.sin(n3) * n2 * sqrt;
				Minecraft.getMinecraft().player.motionZ = Math.cos(n3) * n2 * sqrt;
			}
		}
	}

	public void a(final double n, final double n2, final EntityPlayerSP entityPlayerSP) {
		final MovementInput movementInput = Minecraft.getMinecraft().player.movementInput;
		float moveForward = movementInput.moveForward;
		float moveStrafe = movementInput.moveStrafe;
		float rotationYaw = Minecraft.getMinecraft().player.rotationYaw;
		if (moveForward != 0.0) {
			if (moveStrafe > 0.0) {
				rotationYaw += ((moveForward > 0.0) ? - 45 : 45);
			} else if (moveStrafe < 0.0) {
				rotationYaw += ((moveForward > 0.0) ? 45 : - 45);
			}
			moveStrafe = 0.0f;
			if (moveForward > 0.0) {
				moveForward = 1.0f;
			} else if (moveForward < 0.0) {
				moveForward = - 1.0f;
			}
		}
		if (moveStrafe > 0.0) {
			moveStrafe = 1.0f;
		} else if (moveStrafe < 0.0) {
			moveStrafe = - 1.0f;
		}
		entityPlayerSP.motionX = n + (moveForward * 0.2 * Math.cos(Math.toRadians(rotationYaw + 90.0f)) + moveStrafe * 0.2 * Math.sin(Math.toRadians(rotationYaw + 90.0f)));
		entityPlayerSP.motionZ = n2 + (moveForward * 0.2 * Math.sin(Math.toRadians(rotationYaw + 90.0f)) - moveStrafe * 0.2 * Math.cos(Math.toRadians(rotationYaw + 90.0f)));
	}


}
