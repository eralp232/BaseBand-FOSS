package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Utils.SecondCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;

import java.awt.*;

public class CPS extends Module {
	SecondCounter count = new SecondCounter();

	public CPS() {
		super("CPS", "Crystals!", Category.RENDER, new Color(222, 125, 255, 255).getRGB());
	}

	public void tick() {
		this.setMcHudMeta("" + count.getCount());
	}


	public boolean onPacketSend(Packet<?> packet) {

		if (packet.getClass() == CPacketUseEntity.class) {
			Packet packet2 = packet;
			CPacketUseEntity cPacketUseEntity = (CPacketUseEntity) packet2;
			Entity entity = cPacketUseEntity.getEntityFromWorld(mc.player.world);
			if (entity instanceof EntityEnderCrystal && cPacketUseEntity.getAction() == CPacketUseEntity.Action.ATTACK) {
				count.increment();
			}
		}
		return false;
	}
}
