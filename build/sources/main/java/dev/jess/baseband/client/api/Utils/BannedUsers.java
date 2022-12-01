package dev.jess.baseband.client.api.Utils;

import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class BannedUsers {
	private final static ArrayList<String> bannedUsers = new ArrayList<>(Arrays.asList(
			"e68804c5-bb87-48b6-9d06-6c3158cc9540",
			"f84e53c5-9143-4934-860c-ea44c9ad0e9f",
			"82d5e11c-8892-4013-92bf-c04756128997",
			"e6595045-2bd5-4ea4-8d2e-dafc997ad43b"));
	//Diowz, SoberShulker, Pr3roxDLC, Bush


	public static void check(String uuid) {
		for (String bannedUser : bannedUsers) {
			if (bannedUser.equalsIgnoreCase(uuid)) {
				FMLCommonHandler.instance().exitJava(0, true);
			}
		}
	}

}
