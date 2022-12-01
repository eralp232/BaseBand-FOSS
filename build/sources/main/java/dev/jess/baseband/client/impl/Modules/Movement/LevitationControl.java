package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;

import java.awt.*;
import java.util.Objects;

public class LevitationControl extends Module {
	private final Setting upAmplifier = new Setting("Up Amplifier", this, 1, 0.5, 3, false);
	private final Setting downAmplifier = new Setting("Down Amplifier", this, 1, 0.5, 3, false);

	public LevitationControl() {
		super("LevitationControl", "gsplusplus :D", Category.MOVEMENT, new Color(8, 8, 128, 255).getRGB());
		BaseBand.settingsManager.rSetting(upAmplifier);
		BaseBand.settingsManager.rSetting(downAmplifier);
	}

	public void tick() {

		if (mc.player.isPotionActive(MobEffects.LEVITATION)) {

			int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(25)))).getAmplifier();

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.player.motionY = ((0.05D * (double) (amplifier + 1) - mc.player.motionY) * 0.2D) * upAmplifier.getValDouble(); // reverse the levitation effect if not holding space
			} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.motionY = - (((0.05D * (double) (amplifier + 1) - mc.player.motionY) * 0.2D) * downAmplifier.getValDouble());
			} else {
				mc.player.motionY = 0;
			}

		}
	}
}
