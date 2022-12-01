package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

public class TriggerBot extends Module {


	private final Setting time = new Setting("Timing", this, 0.5, 0, 1, false);
	private Entity entity;


	public TriggerBot() {
		super("TriggerBot", "bruuh", Category.COMBAT, new Color(123, 22, 255, 255).getRGB());
		BaseBand.settingsManager.rSetting(time);
	}

	public void tick() {
		RayTraceResult objectMouseOver = Minecraft.getMinecraft().objectMouseOver;

		if (objectMouseOver != null) {

			if (objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {

				entity = objectMouseOver.entityHit;

				if (entity instanceof EntityPlayer) {

					if (Minecraft.getMinecraft().player.getCooledAttackStrength(0) >= time.getValDouble()) {

						Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().player, entity);

						Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);

						Minecraft.getMinecraft().player.resetCooldown();

					}

				}

			}

		}
	}
}
