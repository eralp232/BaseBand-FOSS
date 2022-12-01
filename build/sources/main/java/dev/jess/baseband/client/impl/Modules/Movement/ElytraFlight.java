package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ElytraFlight extends Module {
	public ElytraFlight() {
		super("ElytraFlight", "yum", Category.MOVEMENT, new Color(231,21,21,255).getRGB());
		BaseBand.settingsManager.rSetting(mode);
	}

	public Setting mode = new Setting("Mode: ", this, "Packet", new ArrayList<>(Arrays.asList("Creative","Factorize","Packet")));


	public void renderWorld(){
		this.setMcHudMeta(mode.getValString());
	}


	public void tick(){
		if (!mc.player.isElytraFlying()) return;
		final ItemStack chestplateSlot = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if (!(chestplateSlot.getItem() == Items.ELYTRA)) return;
		switch (mode.getValString()) {
			case "Factorize":
				if(mc.player.isInWater()) {
					mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
					return;
				}

				if(Wrapper.getMinecraft().player.rotationPitch<10){
					return;
				}

				if(mc.gameSettings.keyBindForward.isKeyDown()) {
					float yaw = (float)Math
							.toRadians(mc.player.rotationYaw);
					mc.player.motionX -= MathHelper.sin(yaw) * 0.05F;
					mc.player.motionZ += MathHelper.cos(yaw) * 0.05F;
				}else if(mc.gameSettings.keyBindBack.isKeyDown()) {
					float yaw = (float)Math
							.toRadians(mc.player.rotationYaw);
					mc.player.motionX += MathHelper.sin(yaw) * 0.05F;
					mc.player.motionZ -= MathHelper.cos(yaw) * 0.05F;
				}
				break;
			case "Creative":
				mc.player.capabilities.isFlying = true;
				break;
			case "Packet":
				if (mc.player.capabilities.isFlying || mc.player.isElytraFlying()) {
					mc.player.setSprinting(false);
				}

				if (mc.player.capabilities.isFlying) {
					mc.player.setVelocity(0.0, 0.0, 0.0);
					mc.player.setPosition(mc.player.posX, mc.player.posY - 5.0000002374872565E-5, mc.player.posZ);
					mc.player.capabilities.setFlySpeed(1);
					mc.player.setSprinting(false);
				}
				if (mc.player.onGround) {
					mc.player.capabilities.allowFlying = false;
				}
				if (mc.player.isElytraFlying()) {
					mc.player.capabilities.setFlySpeed(0.05f);
					mc.player.capabilities.isFlying = true;
					if (!mc.player.capabilities.isCreativeMode) {
						mc.player.capabilities.allowFlying = true;
					}
				}
				if (mc.gameSettings.keyBindJump.isKeyDown() || mc.gameSettings.keyBindSneak.isKeyDown()) {
					mc.player.capabilities.setFlySpeed(0.05f);
				}
		}
	}

	@Override
	public boolean onPacketSend(Packet<?> packet2) {
		if (mode.getValString().equalsIgnoreCase("packet") && mc.player.capabilities.isFlying)
			if (packet2 instanceof CPacketPlayer.PositionRotation) {
				CPacketPlayer.PositionRotation packet = (CPacketPlayer.PositionRotation) packet2;
				mc.player.connection.sendPacket(new CPacketPlayer.Position(packet.x, packet.y, packet.z, packet.onGround));
				mc.player.connection.sendPacket(new CPacketPlayer.Rotation(packet.yaw, 0, packet.onGround));
				return true;

			}
		return false;
	}

	public void disable() {
		if (mc.player.capabilities.isCreativeMode) return;
		mc.player.capabilities.isFlying = false;


		mc.player.capabilities.setFlySpeed(0.05f);
		if (mode.getValString().equalsIgnoreCase("packet")) {
			mc.player.capabilities.isFlying = false;
			mc.player.capabilities.setFlySpeed(0.05f);
			if (!mc.player.capabilities.isCreativeMode) {
				mc.player.capabilities.allowFlying = false;
			}
		}
	}


}
