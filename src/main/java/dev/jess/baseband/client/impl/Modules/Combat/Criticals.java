package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

import java.awt.*;

public class Criticals extends Module {


	public Criticals() {
		super("Criticals", "Always hit critical", Category.COMBAT, new Color(81, 142, 155, 255).getRGB());
	}


	public boolean onPacketSend(Packet<?> packet) {
		if (packet instanceof CPacketUseEntity) {
			CPacketUseEntity packet2 = (CPacketUseEntity) packet;
			if (packet2.action.equals(CPacketUseEntity.Action.ATTACK) && mc.player.onGround && ! mc.player.isInLava() && ! mc.player.isInWater() && ! mc.player.isInWeb) {

				Entity entity = packet2.getEntityFromWorld(mc.world);

				if (entity instanceof EntityLivingBase) {
					Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.1625, Criticals.mc.player.posZ, false));
					Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
					Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 4.0E-6, Criticals.mc.player.posZ, false));
					Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
					Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.0E-6, Criticals.mc.player.posZ, false));
					Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
					Criticals.mc.player.connection.sendPacket(new CPacketPlayer());
					Criticals.mc.player.onCriticalHit(entity);
				}
			}
		}

		return false;
	}


}
