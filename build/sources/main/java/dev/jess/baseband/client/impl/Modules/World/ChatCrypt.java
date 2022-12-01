package dev.jess.baseband.client.impl.Modules.World;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChatCrypt extends Module {


	static ChatCrypt instance;
	static int nonce2 = 0;
	private final Pattern CHAT_PATTERN = Pattern.compile("<.*?> ");
	//private static final char[] ORIGIN_CHARS = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '-', '_', '/', ';', '=', '?', '+', '\u00B5', '\u00A3', '*', '^', '\u00F9', '$', '!', '{', '}', '\'', '"', '|', '&'};
	private final Setting randomness = new Setting("Random Iterations", this, 2, 1, 100, true);
	private final Setting nonce = new Setting("Nonce", this, true);
	private final Setting noncereset = new Setting("Nonce Reset", this, false);
	private final Setting key = new Setting("Key 1", this, 0, - 50, 50, true);
	private final Setting key2 = new Setting("Key 2", this, 0, - 50, 50, true);
	private final Setting key3 = new Setting("Key 3", this, 0, - 50, 50, true);
	private final Setting key4 = new Setting("Key 4", this, 0, - 50, 50, true);
	private final Setting key5 = new Setting("Key 5", this, 0, - 50, 50, true);
	private final Setting key6 = new Setting("Key 6", this, 0, - 50, 50, true);
	private final Setting key7 = new Setting("Key 7", this, 0, - 50, 50, true);
	private final Setting key8 = new Setting("Key 8", this, 0, - 50, 50, true);
	private final Setting key9 = new Setting("Key 9", this, 0, - 50, 50, true);
	private final Setting key10 = new Setting("Key 10", this, 0, - 50, 50, true);
	public Setting dec = new Setting("Decrypt", this, false);
	public Setting ec = new Setting("Encrypt", this, false);
	public Setting reversekey = new Setting("Reverse Key", this, false);

	public ChatCrypt() {
		super("ChatCrypt", "Encrypt chat messages", Category.WORLD, new Color(144, 2, 248, 255).getRGB());
		BaseBand.settingsManager.rSetting(dec);
		BaseBand.settingsManager.rSetting(ec);
		BaseBand.settingsManager.rSetting(nonce);
		BaseBand.settingsManager.rSetting(reversekey);
		BaseBand.settingsManager.rSetting(randomness);
		BaseBand.settingsManager.rSetting(key);
		BaseBand.settingsManager.rSetting(key2);
		BaseBand.settingsManager.rSetting(key3);
		BaseBand.settingsManager.rSetting(key4);
		BaseBand.settingsManager.rSetting(key5);
		BaseBand.settingsManager.rSetting(key6);
		BaseBand.settingsManager.rSetting(key7);
		BaseBand.settingsManager.rSetting(key8);
		BaseBand.settingsManager.rSetting(key9);
		BaseBand.settingsManager.rSetting(key10);
		instance = this;
	}

	private static SecretKey getKeyfromSettings() {


		int key1 = (int) instance.key.getValDouble();
		int key2 = (int) instance.key2.getValDouble();
		int key3 = (int) instance.key3.getValDouble();
		int key4 = (int) instance.key4.getValDouble();
		int key5 = (int) instance.key5.getValDouble();
		int key6 = (int) instance.key6.getValDouble();
		int key7 = (int) instance.key7.getValDouble();
		int key8 = (int) instance.key8.getValDouble();
		int key9 = (int) instance.key9.getValDouble();
		int key10 = (int) instance.key10.getValDouble();


		String finalstr = "";

		finalstr += DigestUtils.sha256Hex(String.valueOf(key1));
		finalstr += DigestUtils.sha256Hex(String.valueOf(key2));
		finalstr += DigestUtils.sha256Hex(String.valueOf(key3));
		finalstr += DigestUtils.sha256Hex(String.valueOf(key4));
		finalstr += DigestUtils.sha256Hex(String.valueOf(key5));
		finalstr += DigestUtils.sha256Hex(String.valueOf(key6));
		finalstr += DigestUtils.sha256Hex(String.valueOf(key7));
		finalstr += DigestUtils.sha256Hex(String.valueOf(key8));
		finalstr += DigestUtils.sha256Hex(String.valueOf(key9));
		finalstr += DigestUtils.sha256Hex(String.valueOf(key10));
		if (instance.nonce.getValBoolean()) {
			nonce2++;
			finalstr += DigestUtils.sha384Hex(String.valueOf(nonce2));
		}


		if (instance.reversekey.getValBoolean()) {
			finalstr = new StringBuilder(finalstr).reverse().toString();
		}


		SecretKey test = null;

		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(finalstr.toCharArray(), finalstr.getBytes(), (int) instance.randomness.getValDouble(), 128);
			SecretKey tmp = factory.generateSecret(spec);
			test = new SecretKeySpec(tmp.getEncoded(), "AES");
		} catch (Exception ignored) {

		}


		return test;
	}

	public static String encrypt(SecretKey key, String initVector, String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
			//SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted));

			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static String decrypt(SecretKey key, String initVector, String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
			//SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);

			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public void tick() {
		if (noncereset.getValBoolean()) {
			noncereset.setValBoolean(false);
			ChatUtil.sendChatMsg(ChatFormatting.GREEN + "[ChatCrypt]" + " Nonce Reset!");
			nonce2 = 0;
		}
	}

	public void enable() {
		nonce2 = 0;
	}

	public void disable() {
		nonce2 = 0;
	}

	public boolean onPacketSend(Packet<?> packet) {


		if (packet instanceof CPacketChatMessage && ec.getValBoolean()) {
			String s = ((CPacketChatMessage) packet).getMessage();


			s = encrypt(getKeyfromSettings(), "RandomInitVector", s) +
					"\uD83D\uDE48";
			if (s.length() > 256) {
				ChatUtil.sendChatMsg("Encrypted message length was too long, couldn't send!");
				return true;
			}
			((CPacketChatMessage) packet).message = s;
		}


		return false;
	}


	public boolean onPacketReceive(Packet<?> packet) {


		if (packet instanceof SPacketChat && dec.getValBoolean()) {
			String s = ((SPacketChat) packet).getChatComponent().getUnformattedText();

			Matcher matcher = CHAT_PATTERN.matcher(s);
			String username = "unnamed";
			if (matcher.find()) {
				username = matcher.group();
				username = username.substring(1, username.length() - 2);
				s = matcher.replaceFirst("");
			}

			//StringBuilder builder = new StringBuilder();

			if (! s.endsWith("\uD83D\uDE48")) {
				return false;
			}  // s.replace("\uD83D\uDE48","");


			s = s.substring(0, s.length() - 2);

			s = decrypt(getKeyfromSettings(), "RandomInitVector", s);
			//s.chars().forEachOrdered(value -> builder.append((char) (value + (ChatAllowedCharacters.isAllowedCharacter((char) value) ? -key.getValDouble() : 0))));


			String message = s;

			//message.replace("*"," ");

			ChatUtil.sendChatMsg("[ChatCrypt] " + username + ": " + message);
			//mc.player.sendMessage(new TextComponentString('\u00A7' + "b" + username + '\u00A7' + "r: " + message));
		}


		return false;
	}
}
