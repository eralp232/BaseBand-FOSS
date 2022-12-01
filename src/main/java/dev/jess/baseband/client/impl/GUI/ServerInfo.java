package dev.jess.baseband.client.impl.GUI;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Listeners.PacketListener;
import dev.jess.baseband.client.api.Utils.RenderingMethods;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;


public class ServerInfo extends PinableFrame {


	public ServerInfo() {
		super("Server Info", new String[]{}, 275);
	}

	public void renderFrameText() {

		if (this.open) {
			ArrayList<String> list = new ArrayList<>();
			list.add("IP: " + (Wrapper.mc.isSingleplayer() ? "Single Player" : Wrapper.mc.getCurrentServerData().serverIP));
			list.add("Players: " + (Wrapper.mc.isSingleplayer() ? "IDK?" : TextFormatting.getTextWithoutFormattingCodes(Wrapper.mc.getCurrentServerData().populationInfo)));
			list.add(generateTickRateText());
			list.add("Ping: " + (Wrapper.mc.isSingleplayer() ? "0" : Wrapper.mc.getCurrentServerData().pingToServer));
			list.add("FPS: " + (Minecraft.getDebugFPS()));

			this.setWidth(Wrapper.fr.getStringWidth(BaseBand.getLongestStringArray(list)));

			RenderingMethods.drawBorderedRectReliantGui(this.x, this.y + this.barHeight + 1, this.x + this.getWidth(), this.y + this.barHeight + 3 + 50, 1, new Color(25, 25, 25, 128).getRGB(), Color.gray.getRGB());
			Wrapper.fr.drawString("IP: " + (Wrapper.mc.isSingleplayer() ? "Single Player" : Wrapper.mc.getCurrentServerData().serverIP), x, this.y + this.barHeight + 3, - 1);
			Wrapper.fr.drawString("Players: " + (Wrapper.mc.isSingleplayer() ? "IDK?" : TextFormatting.getTextWithoutFormattingCodes(Wrapper.mc.getCurrentServerData().populationInfo)), x, this.y + this.barHeight + 3 + 10, - 1);
			Wrapper.fr.drawString(generateTickRateText(), x, this.y + this.barHeight + 3 + 20, - 1);
			Wrapper.fr.drawString("Ping: " + (Wrapper.mc.isSingleplayer() ? "0" : Wrapper.mc.getCurrentServerData().pingToServer), x, this.y + this.barHeight + 3 + 30, - 1);
			Wrapper.fr.drawString("FPS: " + (Minecraft.getDebugFPS()), x, this.y + this.barHeight + 3 + 40, - 1);
		}

	}

	private String generateTickRateText() {
		StringBuilder builder = new StringBuilder("Tick-rate: ");

		builder.append(String.format("%.2f", PacketListener.tps));

		return builder.toString();
	}


}

