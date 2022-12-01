package dev.jess.baseband.client.impl.Modules.Movement;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

import java.awt.*;

public class AntiVelocity extends Module {
	public AntiVelocity() {
		super("AntiVelocity", "Stops You from being Moved.", Category.WORLD, new Color(255, 84, 244, 255).getRGB());
	}


	public void tick() {
		Blocks.ICE.setDefaultSlipperiness(0.6f);
		Blocks.FROSTED_ICE.setDefaultSlipperiness(0.6f);
		Blocks.PACKED_ICE.setDefaultSlipperiness(0.6f);
	}


	public boolean onPacketSend(Packet<?> packet) {
		return false;
	}


	public boolean onPacketReceive(Packet<?> packet) {
		if (packet instanceof SPacketExplosion) {
			return true;
		} else return packet instanceof SPacketEntityVelocity;
	}
}
