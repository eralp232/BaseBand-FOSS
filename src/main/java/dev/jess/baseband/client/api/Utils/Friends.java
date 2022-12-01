package dev.jess.baseband.client.api.Utils;

import dev.jess.baseband.client.api.Registry.FriendRegistry;
import dev.jess.baseband.client.impl.Modules.Client.FriendSystem;

public class Friends {
	public static boolean isFriend(String name) {
		if (! FriendSystem.INSTANCE.disable2) {
			for (String friend : FriendRegistry.friends) {
				if (friend.equalsIgnoreCase(name)) {
					return true;
				}
			}
		}
		return false;
	}
}
