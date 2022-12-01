package dev.jess.baseband.client.impl.Modules.Client;


public class ColorModule {
	public static float getHue() {
		return (float) Colorr.INSTANCE.hue.getValDouble() / 356.0f;
	}

	public static float getSaturation() {
		return (float) Colorr.INSTANCE.saturation.getValDouble() / 100.0f;
	}

	public static float getBrightness() {
		return (float) Colorr.INSTANCE.brightness.getValDouble() / 100.0f;
	}


	public static java.awt.Color getColor() {
		return new java.awt.Color(java.awt.Color.HSBtoRGB(getHue(), getSaturation(), getBrightness()));
	}

	public static java.awt.Color getColor(int alpha) {
		java.awt.Color color;

		color = new java.awt.Color(java.awt.Color.HSBtoRGB(getHue(), getSaturation(), getBrightness()));

		return new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}
}
