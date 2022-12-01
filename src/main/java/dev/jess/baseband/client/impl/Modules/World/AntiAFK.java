package dev.jess.baseband.client.impl.Modules.World;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

import java.awt.*;
import java.util.Random;

public class AntiAFK extends Module {
	public AntiAFK() {
		super("AntiAFK", "Stop from being AFK kicked.", Category.WORLD, new Color(121, 11, 244).getRGB());
	}

	public void tick() {
		if (mc.player.ticksExisted % 40 == 0)
			mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
		if (mc.player.ticksExisted % 15 == 0)
			mc.player.rotationYaw = new Random().nextInt(360) - 180;

		if (mc.player.ticksExisted % 80 == 0) {
			mc.player.jump();
		}
	}
}
