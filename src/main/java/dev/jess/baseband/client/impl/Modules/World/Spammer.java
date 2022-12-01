package dev.jess.baseband.client.impl.Modules.World;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import net.minecraft.network.play.client.CPacketChatMessage;
import org.apache.commons.lang3.RandomStringUtils;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.*;

public class Spammer extends Module {
	private static final String fileName = "BaseBand_Spammer.txt";
	//private static final String defaultMessage = "FurryWare on top!";
	private static final ArrayList<String> spamMessages = new ArrayList<>();
	private static final Random rnd = new Random();
	private static Timer timer;
	private final Setting delay = new Setting("Delay", this, 3000, 1, 5000, true);
	private final Setting random = new Setting("Random", this, false);
	private final Setting greentext = new Setting("Green Text", this, false);
	private final Setting antiantispam = new Setting("AntiAntiSpam", this, false);
	private final Setting antiantispamlength = new Setting("AntiAntiSpamLength", this, 8, 1, 16, true);

	public Spammer() {
		super("Spammer", "spam chat", Category.WORLD, new Color(122, 11, 11, 255).getRGB());
		BaseBand.settingsManager.rSetting(delay);
		BaseBand.settingsManager.rSetting(random);
		BaseBand.settingsManager.rSetting(greentext);
		BaseBand.settingsManager.rSetting(antiantispam);
		BaseBand.settingsManager.rSetting(antiantispamlength);
	}

	public static String cropMaxLengthMessage(String s, int i) {
		if (s.length() > 255 - i) {
			s = s.substring(0, 255 - i);
		}
		return s;
	}

	public static List<String> readTextFileAllLines(String file) {
		try {
			Path path = Paths.get(file);
			return Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("WARNING: Unable to read file, creating new file: " + file);
			appendTextFile("", file);
			return Collections.emptyList();
		}
	}

	public static boolean appendTextFile(String data, String file) {
		try {
			Path path = Paths.get(file);
			Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
		} catch (IOException e) {
			System.out.println("WARNING: Unable to write file: " + file);
			return false;
		}
		return true;
	}

	public void enable() {
		this.readSpamFile();
		timer = new Timer();
		if (Spammer.mc.player == null) {
			this.disable();
			return;
		}
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				Spammer.this.runCycle();
			}
		};
		timer.schedule(task, 0L, (long) this.delay.getValDouble());
	}

	public void disable() {
		try {
			timer.cancel();
			timer.purge();
			spamMessages.clear();
		} catch (Exception e) {
			//damnit
		}
	}

	private void runCycle() {
		if (Spammer.mc.player == null) {
			return;
		}
		/*
		if(read) {
			this.readSpamFile();
			read=false;
		}
		 */
		if (spamMessages.size() > 0) {
			String messageOut;
			if (random.getValBoolean()) {
				int index = rnd.nextInt(spamMessages.size());
				messageOut = spamMessages.get(index);
				spamMessages.remove(index);
			} else {
				messageOut = spamMessages.get(0);
				spamMessages.remove(0);
			}
			spamMessages.add(messageOut);
			if (greentext.getValBoolean()) {
				messageOut = "> " + messageOut;
			}
			int reserved = 0;
			ArrayList<String> messageAppendix = new ArrayList<>();
			if (antiantispam.getValBoolean()) {
				messageAppendix.add(RandomStringUtils.random((int) antiantispamlength.getValDouble(), true, true));
			}
			if (messageAppendix.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(" ");
				for (String msg : messageAppendix) {
					sb.append(msg);
				}
				messageOut = cropMaxLengthMessage(messageOut, sb.toString().length() + reserved);
				messageOut = messageOut + sb;
			}
			Spammer.mc.player.connection.sendPacket(new CPacketChatMessage(messageOut.replaceAll("\u00a7", "")));
		} else {
			this.setToggled(false);
		}
	}

	private void readSpamFile() {
		List<String> fileInput = readTextFileAllLines(fileName);
		if (fileInput.isEmpty()) {
			ChatUtil.sendChatMsg("BaseBand_Spammer.txt empty!");
			this.setToggled(false);
			return;
		}
		Iterator<String> i = fileInput.iterator();
		spamMessages.clear();
		while (i.hasNext()) {
			String s = i.next();
			if (s.replaceAll("\\s", "").isEmpty()) continue;
			spamMessages.add(s);
		}
		if (spamMessages.size() == 0) {
			ChatUtil.sendChatMsg("BaseBand_Spammer.txt empty!");
			this.setToggled(false);
			return;
		}
	}

}
