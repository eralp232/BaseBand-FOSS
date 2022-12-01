package dev.jess.baseband.client.impl.AzureGUI.font;


import static dev.jess.baseband.client.api.Utils.Wrapper.mc;

public class FontManager {
	public static void drawString(String text, float x, float y, int color) {
		mc.fontRenderer.drawStringWithShadow(text, x, y, color);
	}

	public static void drawCenteredString(String text, float x, float y, int color) {
		mc.fontRenderer.drawStringWithShadow(text, x - getStringWidth(text) / 2.0f, y, color);
	}

	public static float getStringWidth(String text) {
		return mc.fontRenderer.getStringWidth(text);
	}

	public static int getHeight() {
		return mc.fontRenderer.FONT_HEIGHT;
	}
}
