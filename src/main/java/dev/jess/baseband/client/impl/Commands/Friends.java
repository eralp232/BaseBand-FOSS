package dev.jess.baseband.client.impl.Commands;

import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Registry.FriendRegistry;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import dev.jess.baseband.client.api.Utils.Wrapper;
import dev.jess.baseband.client.impl.Modules.Client.FriendSystem;

public class Friends extends Command {

	public Friends() {
		super("Friends", "Like TTC", "f", "friend");
	}

	@Override
	public void execute(String[] args) {
		String action;
		String name;

		try {
			action = args[0];
			name = args[1];
		} catch (Exception e) {
			return;
		}

		switch (action) {
			case ("add"): {
				FriendRegistry.friends.add(name);
				if (FriendSystem.INSTANCE.notify2 && Wrapper.getWorld().isRemote == true && Wrapper.mc.getConnection() != null) {
					Wrapper.getMinecraft().player.sendChatMessage("/w You've just been friended thanks to BaseBand! {" + name + "}");
				}
				ChatUtil.sendChatMsg("Added Friend: " + name);
				break;
			}
			case ("del"): {
				FriendRegistry.friends.remove(name);
				if (FriendSystem.INSTANCE.notify2 && Wrapper.getWorld().isRemote == true && Wrapper.mc.getConnection() != null) {
					Wrapper.getMinecraft().player.sendChatMessage("/w You've just been unfriended thanks to BaseBand! {" + name + "}");
				}
				ChatUtil.sendChatMsg("Removed Friend: " + name);
				break;
			}
			default: {
				ChatUtil.sendChatMsg("Friend List:\n");
				ChatUtil.sendChatMsg(getFriends());
				break;
			}
		}


	}


	public String getFriends() {
		String list = "";
		for (int i = 0; i < FriendRegistry.friends.size(); i++) {
			list += FriendRegistry.friends.get(i) + "\n";
		}
		return list;
	}

}
