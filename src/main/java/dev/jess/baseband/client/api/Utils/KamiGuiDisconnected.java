package dev.jess.baseband.client.api.Utils;

import dev.jess.baseband.client.impl.Modules.Misc.AutoReconnect;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.GuiConnecting;

public class KamiGuiDisconnected extends GuiDisconnected {


	int millis = 5 * 1000;
	long cTime;

	public KamiGuiDisconnected(GuiDisconnected disconnected) {
		super(disconnected.parentScreen, disconnected.reason, disconnected.message);
		cTime = System.currentTimeMillis();
	}

	@Override
	public void updateScreen() {
		if (millis <= 0)
			mc.displayGuiScreen(new GuiConnecting(parentScreen, mc, AutoReconnect.serverData));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		long a = System.currentTimeMillis();
		millis -= a - cTime;
		cTime = a;

		String s = "Reconnecting in " + Math.max(0, Math.floor((double) millis / 100) / 10) + "s";
		fontRenderer.drawString(s, width / 2 - fontRenderer.getStringWidth(s) / 2, height - 16, 0xffffff, true);
	}

}
