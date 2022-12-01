package dev.jess.baseband.client.api.Registry;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Utils.Wrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FriendRegistry {

	private static final String fileName = "BaseBand/";


	public static ArrayList<String> friends = new ArrayList<>(Arrays.asList("1"));
	public File bb;

	public void save() throws FileNotFoundException {
		String moduleLocation = fileName + "Friends.txt";
		PrintWriter pw = new PrintWriter(new FileOutputStream(moduleLocation));
		for (String friends : friends) {
			pw.println(friends);
			BaseBand.log.debug("Saved Friend: " + friends);
		}
		pw.close();
	}

	public void load() throws FileNotFoundException {
		bb = new File(Wrapper.mc.gameDir + File.separator + "BaseBand");
		if (! bb.exists()) {
			bb.mkdirs();
		}
		try {
			File file = new File(bb.getAbsolutePath(), "Friends.txt");
			FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				friends.add(line);
				BaseBand.log.debug("Added Friend: " + line);
			}
			br.close();
		} catch (IOException e) {
			throw new FileNotFoundException();
		}
	}
}
