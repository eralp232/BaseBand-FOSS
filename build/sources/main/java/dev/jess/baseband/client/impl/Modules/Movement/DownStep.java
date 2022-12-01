package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class DownStep extends Module {


	private final Setting height = new Setting("Height", this, 3, 0, 25, true);

	private final Setting motion = new Setting("Motion", this, 3, 0, 10, false);

	public DownStep() {
		super("DownStep", "Move like Ciruu", Category.MOVEMENT, new Color(255, 136, 94, 255).getRGB());
		BaseBand.settingsManager.rSetting(height);
		BaseBand.settingsManager.rSetting(motion);
	}


	public void tick() {
		if (mc.world == null || mc.player == null || mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder()
				|| mc.gameSettings.keyBindJump.isKeyDown()) {
			return;
		}

		if (mc.player != null && mc.player.onGround && ! mc.player.isInWater() && ! mc.player.isOnLadder()) {
			if (mc.world.isAirBlock(new BlockPos(mc.player.getPositionVector()))) {
				if (mc.player.onGround &&
						(! mc.player.isElytraFlying()
								|| mc.player.fallDistance < height.getValDouble()
								|| ! mc.player.capabilities.isFlying))
					mc.player.motionY -= motion.getValDouble();
			}
		}
	}
}
