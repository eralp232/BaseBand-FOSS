package dev.jess.baseband.client.impl.Commands;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Registry.CommandRegistry;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.ChatUtil;

public class Help extends Command {
	public Help() {
		super("Help", "Displays Help.", "help", "Commands");
	}

	@Override
	public void execute(String[] args) {
		ChatUtil.sendChatMsg("BaseBand Client " + BaseBand.VERSION);
		//ChatUtil.sendChatMsg("Modules:\n");
		//ChatUtil.sendChatMsg("\n" + getModules());
		ChatUtil.sendChatMsg("Commands:" +
				"\n" + getCommands());
		ChatUtil.sendChatMsg("Current Module Count: " + ModuleRegistry.MODULES.size() + "\n");
		ChatUtil.sendChatMsg("Copyright 2022 rd_132211, TudbuT©");
	}

	public String getCommands() {
		String list = "";
		for (int i = 0; i < CommandRegistry.getAll().size(); i++) {
			list += CommandRegistry.getAll().get(i).getName() + " <- " + CommandRegistry.getAll().get(i).getDescription() + "\n";
		}
		return list;
	}

	public String getModules() {
		String list = "";
		for (int i = 0; i < ModuleRegistry.getAll().size(); i++) {
			list += ModuleRegistry.getAll().get(i).getName() + " <- " + ModuleRegistry.getAll().get(i).getCategory() + " <- " + ModuleRegistry.getAll().get(i).isToggled() + "\n";
		}
		return list;
	}


}
