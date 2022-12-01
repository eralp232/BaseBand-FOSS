package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.EntityUtil;
import dev.jess.baseband.client.api.Utils.RotationUtil;
import dev.jess.baseband.client.api.Utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec2f;

import java.awt.*;

public class AutoHit extends Module {

	private final Setting hitRange = new Setting("Range", this, 5, 1, 6, true);
	private final Setting rotate = new Setting("Rotate", this, false);
	private final Setting rotatePacket = new Setting("Rotate Spoof", this, false);
	private final Setting delay = new Setting("Delay", this, 30, 1, 1000, true);
	private final Setting packetHit = new Setting("PacketHit", this, false);
	private final Setting silentSwing = new Setting("PacketSwing", this, false);
	private final Setting offhandSwing = new Setting("OffhandSwing", this, false);
	private final Setting swapBypass = new Setting("Swap", this, false);


	public TimerUtil timer = new TimerUtil();


	public AutoHit() {
		super("AutoHit", "Hit Automatically", Category.COMBAT, new Color(231, 212, 255, 255).getRGB());
		BaseBand.settingsManager.rSetting(hitRange);
		BaseBand.settingsManager.rSetting(rotate);
		BaseBand.settingsManager.rSetting(rotatePacket);
		BaseBand.settingsManager.rSetting(delay);
		BaseBand.settingsManager.rSetting(packetHit);
		BaseBand.settingsManager.rSetting(silentSwing);
		BaseBand.settingsManager.rSetting(swapBypass);
		BaseBand.settingsManager.rSetting(offhandSwing);
	}


	public void enable() {
		//timer.reset();
	}


	public void disable() {
		// timer.reset();

	}


	public void tick() {
		if (mc.player.isDead) {
			return;
		}

		if (timer.passedMs((long) delay.getValDouble())) {
			//timer.reset();

			for (Entity target : Minecraft.getMinecraft().world.loadedEntityList) {
				if (! EntityUtil.isLiving(target)) {
					continue;
				}
				if (! EntityUtil.isPlayer(target)) {
					//continue;
				}

				if (target.getClass() == EntityItem.class) {
					continue;
				}

				if (target.getClass() == EntityItemFrame.class) {
					continue;
				}

				if (target == mc.player) {
					continue;
				}
				if (mc.player.getDistance(target) > hitRange.getValDouble()) {
					continue;
				}
				if (((EntityLivingBase) target).getHealth() <= 0) {
					continue;
				}


				if (swapBypass.getValBoolean()) {
					itemSwitch(36);
					itemSwitch(37);
					itemSwitch(36);
				}


				swingItem();
				attack(target);

			}
			timer.reset();
		}
	}

	public void itemSwitch(int slot) {
		mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.SWAP, mc.player);
	}

	public void attack(Entity entity) {
		Vec2f rot = RotationUtil.getRotationTo(entity.getPositionVector());
		if (rotate.getValBoolean() && ! rotatePacket.getValBoolean()) {
			mc.player.rotationPitch = rot.y;
			mc.player.rotationYaw = rot.x;
		} else if (rotatePacket.getValBoolean()) {
			mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rot.x, rot.y, mc.player.onGround));
		}

		if (packetHit.getValBoolean()) {
			mc.player.connection.sendPacket(new CPacketUseEntity(entity));
		} else {
			mc.playerController.attackEntity(mc.player, entity);
		}
	}

	public void swingItem() {
		if (silentSwing.getValBoolean()) {
			if (offhandSwing.getValBoolean()) {
				mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.OFF_HAND));
			} else {
				mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
			}
		} else {
			if (offhandSwing.getValBoolean()) {
				mc.player.swingArm(EnumHand.OFF_HAND);
			} else {
				mc.player.swingArm(EnumHand.MAIN_HAND);
			}
		}
	}


}
