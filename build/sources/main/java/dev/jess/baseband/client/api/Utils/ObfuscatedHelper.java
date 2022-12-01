package dev.jess.baseband.client.api.Utils;

public class ObfuscatedHelper {
	public static boolean check() {
		try {
			Class.forName("dev.jess.baseband.client.api.Main.BaseBand");
			return false;
		} catch (Exception e) {
			return true;
		}
	}
}
