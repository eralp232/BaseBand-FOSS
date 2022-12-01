package dev.jess.baseband.client.api.Listeners;

import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketTimeUpdate;

import java.util.Date;

public class PacketListener {
	private static final long joinTime = 0;
	public static float tps = 20.0f;
	private static long lastTick = - 1;

	public static boolean onPacketSend(Packet<?> packet) {
		boolean b = false;
		for (int i = 0; i < ModuleRegistry.MODULES.size(); i++) {
			if (ModuleRegistry.INSTANCE.getModuleList().get(i).isToggled() && packet != null)
				try {
					if (ModuleRegistry.INSTANCE.getModuleList().get(i).onPacketSend(packet))
						b = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

		return b;
	}

	public static boolean onPacketReceive(Packet<?> packet) {
		boolean b = false;

		if (packet instanceof SPacketTimeUpdate) {
			long time = System.currentTimeMillis();
			if (lastTick != - 1 && new Date().getTime() - joinTime > 5000) {
				long diff = time - lastTick;
				if (diff > 50) {
					tps = (tps + ((1000f / diff) * 20f)) / 2;
				}
			} else {
				tps = 20.0f;
			}
			lastTick = time;
		}

		for (int i = 0; i < ModuleRegistry.MODULES.size(); i++) {
			if (ModuleRegistry.INSTANCE.getModuleList().get(i).isToggled() && packet != null)
				try {
					if (ModuleRegistry.INSTANCE.getModuleList().get(i).onPacketReceive(packet))
						b = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

		return b;
	}
}
