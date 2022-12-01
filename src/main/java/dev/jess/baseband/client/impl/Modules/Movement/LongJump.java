package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;

import java.awt.*;

public class LongJump extends Module {
	private int airTicks;
	private int groundTicks;

	public LongJump() {
		super("LongJump", "direkt", Category.MOVEMENT, new Color(21, 244, 244, 255).getRGB());
	}

	public void enable() {
		groundTicks = 0;
	}

	public void tick() {
		//if (PlayerHelper.isInLiquid() || PlayerHelper.isOnLiquid() || event.getTime() != MotionUpdateEvent.Time.BEFORE) break;
		float direction = Minecraft.getMinecraft().player.rotationYaw + (float) (Minecraft.getMinecraft().player.moveForward < 0.0f ? 180 : 0) + (Minecraft.getMinecraft().player.moveStrafing > 0.0f ? - 90.0f * (Minecraft.getMinecraft().player.moveForward < 0.0f ? - 0.5f : (Minecraft.getMinecraft().player.moveForward > 0.0f ? 0.5f : 1.0f)) : 0.0f) - (Minecraft.getMinecraft().player.moveStrafing < 0.0f ? - 90.0f * (Minecraft.getMinecraft().player.moveForward < 0.0f ? - 0.5f : (Minecraft.getMinecraft().player.moveForward > 0.0f ? 0.5f : 1.0f)) : 0.0f);
		float xDir = (float) Math.cos((double) (direction + 90.0f) * Math.PI / 180.0);
		float zDir = (float) Math.sin((double) (direction + 90.0f) * Math.PI / 180.0);
		if (! Minecraft.getMinecraft().player.collidedVertically) {
			airTicks++;
			if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isPressed()) {
				try {
					Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(Minecraft.getMinecraft().player.posX, 2.147483647E9, Minecraft.getMinecraft().player.posZ, Minecraft.getMinecraft().player.onGround));
				} catch (Exception e) {

				}
			}
			groundTicks = 0;
			if (! Minecraft.getMinecraft().player.collidedVertically) {
				if (Minecraft.getMinecraft().player.motionY == - 0.07190068807140403) {
					Minecraft.getMinecraft().player.motionY *= 0.35f;
				} else if (Minecraft.getMinecraft().player.motionY == - 0.10306193759436909) {
					Minecraft.getMinecraft().player.motionY *= 0.55f;
				} else if (Minecraft.getMinecraft().player.motionY == - 0.13395038817442878) {
					Minecraft.getMinecraft().player.motionY *= 0.67f;
				} else if (Minecraft.getMinecraft().player.motionY == - 0.16635183030382) {
					Minecraft.getMinecraft().player.motionY *= 0.69f;
				} else if (Minecraft.getMinecraft().player.motionY == - 0.19088711097794803) {
					Minecraft.getMinecraft().player.motionY *= 0.71f;
				} else if (Minecraft.getMinecraft().player.motionY == - 0.21121925191528862) {
					Minecraft.getMinecraft().player.motionY *= 0.2f;
				} else if (Minecraft.getMinecraft().player.motionY == - 0.11979897632390576) {
					Minecraft.getMinecraft().player.motionY *= 0.93f;
				} else if (Minecraft.getMinecraft().player.motionY == - 0.18758479151225355) {
					Minecraft.getMinecraft().player.motionY *= 0.72f;
				} else if (Minecraft.getMinecraft().player.motionY == - 0.21075983825251726) {
					Minecraft.getMinecraft().player.motionY *= 0.76f;
				}
				if (Minecraft.getMinecraft().player.motionY < - 0.2 && Minecraft.getMinecraft().player.motionY > - 0.24) {
					Minecraft.getMinecraft().player.motionY *= 0.7;
				}
				if (Minecraft.getMinecraft().player.motionY < - 0.25 && Minecraft.getMinecraft().player.motionY > - 0.32) {
					Minecraft.getMinecraft().player.motionY *= 0.8;
				}
				if (Minecraft.getMinecraft().player.motionY < - 0.35 && Minecraft.getMinecraft().player.motionY > - 0.8) {
					Minecraft.getMinecraft().player.motionY *= 0.98;
				}
				if (Minecraft.getMinecraft().player.motionY < - 0.8 && Minecraft.getMinecraft().player.motionY > - 1.6) {
					Minecraft.getMinecraft().player.motionY *= 0.99;
				}
			}
			Minecraft.getMinecraft().timer.tickLength = 71.4285714f;
			double[] speedVals = new double[]{0.420606, 0.417924, 0.415258, 0.412609, 0.409977, 0.407361, 0.404761, 0.402178, 0.399611, 0.39706, 0.394525, 0.392, 0.3894, 0.38644, 0.383655, 0.381105, 0.37867, 0.37625, 0.37384, 0.37145, 0.369, 0.3666, 0.3642, 0.3618, 0.35945, 0.357, 0.354, 0.351, 0.348, 0.345, 0.342, 0.339, 0.336, 0.333, 0.33, 0.327, 0.324, 0.321, 0.318, 0.315, 0.312, 0.309, 0.307, 0.305, 0.303, 0.3, 0.297, 0.295, 0.293, 0.291, 0.289, 0.287, 0.285, 0.283, 0.281, 0.279, 0.277, 0.275, 0.273, 0.271, 0.269, 0.267, 0.265, 0.263, 0.261, 0.259, 0.257, 0.255, 0.253, 0.251, 0.249, 0.247, 0.245, 0.243, 0.241, 0.239, 0.237};
			if (Minecraft.getMinecraft().gameSettings.keyBindForward.pressed) {
				try {
					Minecraft.getMinecraft().player.motionX = (double) xDir * speedVals[airTicks - 1] * 3.0;
					Minecraft.getMinecraft().player.motionZ = (double) zDir * speedVals[airTicks - 1] * 3.0;
				} catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
				}
				return;
			}
			Minecraft.getMinecraft().player.motionX = 0.0;
			Minecraft.getMinecraft().player.motionZ = 0.0;
			return;
		}
		Minecraft.getMinecraft().timer.tickLength = 50f;
		airTicks = 0;
		groundTicks++;
		Minecraft.getMinecraft().player.motionX /= 13.0;
		Minecraft.getMinecraft().player.motionZ /= 13.0;
		if (groundTicks == 1) {
			updatePosition(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ);
			updatePosition(Minecraft.getMinecraft().player.posX + 0.0624, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ);
			updatePosition(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY + 0.419, Minecraft.getMinecraft().player.posZ);
			updatePosition(Minecraft.getMinecraft().player.posX + 0.0624, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ);
			updatePosition(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY + 0.419, Minecraft.getMinecraft().player.posZ);
			return;
		}
		if (groundTicks <= 2) return;
		groundTicks = 0;
		Minecraft.getMinecraft().player.motionX = (double) xDir * 0.3;
		Minecraft.getMinecraft().player.motionZ = (double) zDir * 0.3;
		Minecraft.getMinecraft().player.motionY = 0.424f;
	}

	public void updatePosition(double x, double y, double z) {
		try {
			Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, Minecraft.getMinecraft().player.onGround));
		} catch (Exception e) {

		}
	}


}
