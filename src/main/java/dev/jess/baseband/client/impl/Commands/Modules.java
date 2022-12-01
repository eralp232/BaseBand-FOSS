package dev.jess.baseband.client.impl.Commands;

import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.ChatUtil;

public class Modules extends Command {
	public Modules() {
		super("Modules", "Displays modules.", "Mods", "Modlist");
	}

	@Override
	public void execute(String[] args) {
		ChatUtil.sendChatMsg("Modules:\n");
		ChatUtil.sendChatMsg("\n" + getModules());
	}

	public String getModules() {
		String list = "";
		for (int i = 0; i < ModuleRegistry.getAll().size(); i++) {
			list += ModuleRegistry.getAll().get(i).getName() + " <- " + ModuleRegistry.getAll().get(i).getCategory() + " <- " + ModuleRegistry.getAll().get(i).isToggled() + "\n";
		}
		return list;
	}
}
