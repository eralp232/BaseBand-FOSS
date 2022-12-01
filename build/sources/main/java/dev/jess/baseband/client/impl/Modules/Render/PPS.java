package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Utils.SecondCounter;
import net.minecraft.network.Packet;

import java.awt.*;

public class PPS extends Module {
	SecondCounter rx = new SecondCounter();
	SecondCounter tx = new SecondCounter();

	public PPS() {
		super("PPS", "Packets Per Second", Category.RENDER, new Color(150, 250, 242, 255).getRGB());
	}

	public void tick() {
		this.setMcHudMeta("I:" + rx.getCount() + " O:" + tx.getCount());
	}

	public boolean onPacketSend(Packet<?> packet) {
		tx.increment();
		return false;
	}

	public boolean onPacketReceive(Packet<?> packet) {
		rx.increment();
		return false;
	}
}
