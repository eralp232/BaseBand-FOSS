package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BoatFly extends Module {
	private final Setting speed = new Setting("Speed", this, 2, 0, 10, true);
	private final Setting verticalSpeed = new Setting("Vertical Speed", this, 1, 0, 10, true);
	private final Setting downKey = new Setting("Down", this, true);
	private final Setting glideSpeed = new Setting("Glide Speed", this, 0, - 10, 10, true);
	private final Setting staticY = new Setting("Static Y", this, true);
	private final Setting hover = new Setting("Hover", this, false);
	private final Setting yawFix = new Setting("YawFix", this, false);
	private final Setting bypass = new Setting("Remount", this, false);
	private final Setting bypassSpeed = new Setting("RemountSpeed", this, 4, 1, 10, true);
	private final Setting constrict = new Setting("RemountConstrict", this, false);
	private final Setting packetbypass = new Setting("RemountPacket", this, false);
	private final Setting allowSPacketMoveVehicle = new Setting("SPacketMoveVehicle", this, true);
	private final Setting allowCPacketPlayer = new Setting("CPacketPlayer", this, true);
	private final Setting allowCSteerBoat = new Setting("CPacketSteerBoat", this, true);
	private final Setting bounds = new Setting("Bounds", this, false);
	private final Setting extraCalc = new Setting("ExtraCalc", this, false);
	public Setting getBoundSet = new Setting("Bounds: ", this, "Up", new ArrayList<>(Arrays.asList("Up", "Down", "Zero", "Min", "Alternate", "Forward", "Flat", "Constrict")));

	public BoatFly() {
		super("BoatFly", "Is you ready?", Category.MOVEMENT, new Color(21, 111, 111, 255).getRGB());

		BaseBand.settingsManager.rSetting(speed);
		BaseBand.settingsManager.rSetting(verticalSpeed);
		BaseBand.settingsManager.rSetting(downKey);
		BaseBand.settingsManager.rSetting(glideSpeed);
		BaseBand.settingsManager.rSetting(staticY);
		BaseBand.settingsManager.rSetting(hover);
		BaseBand.settingsManager.rSetting(yawFix);
		BaseBand.settingsManager.rSetting(bypass);
		BaseBand.settingsManager.rSetting(bypassSpeed);
		BaseBand.settingsManager.rSetting(constrict);
		BaseBand.settingsManager.rSetting(packetbypass);
		BaseBand.settingsManager.rSetting(allowSPacketMoveVehicle);
		BaseBand.settingsManager.rSetting(allowCPacketPlayer);
		BaseBand.settingsManager.rSetting(allowCSteerBoat);
		BaseBand.settingsManager.rSetting(bounds);
		BaseBand.settingsManager.rSetting(getBoundSet);
		BaseBand.settingsManager.rSetting(extraCalc);
	}


	public static float getDirection2() {
		float var1 = mc.player.rotationYaw;
		if (mc.player.moveForward < 0.0F) var1 += 180.0F;
		float forward = 1.0F;
		if (mc.player.moveForward < 0.0F) forward = - 0.5F;
		else if (mc.player.moveForward > 0.0F) forward = 0.5F;
		if (mc.player.moveStrafing > 0.0F) var1 -= 90.0F * forward;
		if (mc.player.moveStrafing < 0.0F) var1 += 90.0F * forward;
		var1 *= 0.017453292F;
		return var1;
	}

	public static boolean isMoving() {
		if (mc.player == null) return false;
		if (mc.player.movementInput.moveForward != 0.0f) return true;
		return mc.player.movementInput.moveStrafe != 0.0f;
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

	public static double[] forward2(final double speed) {
		float forward = Minecraft.getMinecraft().player.movementInput.moveForward;
		float side = Minecraft.getMinecraft().player.movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().player.prevRotationYaw + (Minecraft.getMinecraft().player.rotationYaw - Minecraft.getMinecraft().player.prevRotationYaw) * Minecraft.getMinecraft().getRenderPartialTicks();
		if (forward != 0.0f) {
			if (side > 0.0f) {
				yaw += ((forward > 0.0f) ? - 45 : 45);
			} else if (side < 0.0f) {
				yaw += ((forward > 0.0f) ? 45 : - 45);
			}
			side = 0.0f;
			if (forward > 0.0f) {
				forward = 1.0f;
			} else if (forward < 0.0f) {
				forward = - 1.0f;
			}
		}
		final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
		final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
		final double posX = forward * speed * cos + side * speed * sin;
		final double posZ = forward * speed * sin - side * speed * cos;
		return new double[]{posX, posZ};
	}

	public static void doBounds(String mode) {

		double[] dir;
		CPacketPlayer packet = new CPacketPlayer.PositionRotation(0, 0, 0, 0, 0, false);

		switch (mode) {

			case "Up":
				packet = new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY + 69420, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, false);
				break;
			case "Down":
				packet = new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY - 69420, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, false);
				break;
			case "Zero":
				packet = new CPacketPlayer.PositionRotation(mc.player.posX, 0, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, false);
				break;
			case "Min":
				packet = new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY + 100, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, false);
				break;
			case "Alternate":
				if (mc.player.ticksExisted % 2 == 0)
					packet = new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY + 69420, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, false);
				else
					packet = new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY - 69420, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, false);
				break;
			case "Forward":
				dir = forward2(67);
				packet = new CPacketPlayer.PositionRotation(mc.player.posX + dir[0], mc.player.posY + 33.4, mc.player.posZ + dir[1], mc.player.rotationYaw, mc.player.rotationPitch, false);
				break;
			case "Flat":
				dir = forward2(100);
				packet = new CPacketPlayer.PositionRotation(mc.player.posX + dir[0], mc.player.posY, mc.player.posZ + dir[1], mc.player.rotationYaw, mc.player.rotationPitch, false);
				break;
			case "Constrict":
				dir = forward2(67);
				packet = new CPacketPlayer.PositionRotation(mc.player.posX + dir[0], mc.player.posY + (mc.player.posY > 64 ? - 33.4 : 33.4), mc.player.posZ + dir[1], mc.player.rotationYaw, mc.player.rotationPitch, false);
				break;
		}
		mc.player.connection.sendPacket(packet);
	}

	public void tick() {
		if (mc.player == null || mc.world == null || mc.player.ridingEntity == null) return;
		this.setMcHudMeta(mc.player.ticksExisted % bypassSpeed.getValDouble() == 0 ? "1" : "0" + "-" + speed.getValDouble());
		Entity e = mc.player.ridingEntity;

		if (yawFix.getValBoolean()) {
			e.rotationYaw = RotationUtil.normalizeAngle(mc.player.rotationYaw);
		}
		if (mc.gameSettings.keyBindJump.isKeyDown()) e.motionY = verticalSpeed.getValDouble();
		else if (downKey.getValBoolean() && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			e.motionY = - verticalSpeed.getValDouble();
		else if (staticY.getValBoolean()) e.motionY = 0;
		else
			e.motionY = hover.getValBoolean() && mc.player.ticksExisted % 2 == 0 ? glideSpeed.getValDouble() : - glideSpeed.getValDouble();
		if (isMoving()) {
			if (extraCalc.getValBoolean()) {
				double[] dir = forward(speed.getValDouble());
				e.motionX = dir[0];
				e.motionZ = dir[1];
			} else {
				float dir = getDirection2();
				mc.player.motionX -= (MathHelper.sin(dir) * speed.getValDouble());
				mc.player.motionZ += (MathHelper.cos(dir) * speed.getValDouble());
			}

		} else {
			e.motionX = 0;
			e.motionZ = 0;
		}
		if (bounds.getValBoolean()) {
			doBounds(getBoundSet.getValString());
		}
		if (bypass.getValBoolean() && mc.player.ticksExisted % bypassSpeed.getValDouble() == 0)
			if (mc.player.ridingEntity instanceof EntityBoat) {
				if (packetbypass.getValBoolean()) {
					mc.player.connection.sendPacket(new CPacketUseEntity(mc.player.ridingEntity, constrict.getValBoolean() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
				} else {
					mc.playerController.interactWithEntity(mc.player, mc.player.ridingEntity, constrict.getValBoolean() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
				}

			}
	}

	@Override
	public boolean onPacketSend(Packet<?> packet) {
		if (packet.getClass() == CPacketSteerBoat.class && ! allowCSteerBoat.getValBoolean() || mc.player.ridingEntity != null) {
			return true;
		}

		return packet instanceof CPacketPlayer && ! allowCPacketPlayer.getValBoolean() || mc.player.ridingEntity != null;
	}

	public boolean onPacketReceive(Packet<?> packet) {
		return packet.getClass() == SPacketMoveVehicle.class && ! allowSPacketMoveVehicle.getValBoolean() || mc.player.ridingEntity != null;
	}

}
