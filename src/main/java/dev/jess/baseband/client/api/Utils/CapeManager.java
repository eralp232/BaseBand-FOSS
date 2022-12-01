package dev.jess.baseband.client.api.Utils;

import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class CapeManager {
	private static final String users = "https://pastebin.com/raw/XLtznBje";
	private static final List<String> staticUsers = Arrays.asList("bfe75cb4-ea3f-4791-a058-3f04aa5e839a", "2e4b5395-6b08-418f-96db-5e23a462446c", Minecraft.getMinecraft().getSession().getPlayerID());
	private static HashMap<String, Boolean> capeUsers;

	public CapeManager() {
		capeUsers = new HashMap();
	}

	public static boolean hasCape(UUID uuid) {
		return capeUsers.containsKey(uuid.toString());
	}

	public static boolean isOg(UUID uuid) {
		if (capeUsers.containsKey(uuid.toString())) {
			return capeUsers.get(uuid.toString());
		}
		return false;
	}

	public void initializeCapes() {
		staticUsers.forEach(uuid -> capeUsers.put(uuid, true));
		this.getFromPastebin(users).forEach(uuid -> capeUsers.put(uuid, true));
	}

	private List<String> getFromPastebin(String urlString) {
		URL url;
		BufferedReader bufferedReader;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
		ArrayList<String> uuidList = new ArrayList<String>();
		do {
			String line;
			try {
				line = bufferedReader.readLine();
				if (line == null) {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return new ArrayList<String>();
			}
			uuidList.add(line);
		} while (true);
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
		return uuidList;
	}
}
