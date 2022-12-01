package dev.jess.baseband.client.impl.Commands;


import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.ChatUtil;

public class Drawn extends Command {
	boolean visible = false;
	private String module;

	public Drawn() {
		super("Drawn", "Set Module Visibility.", "Visible");
	}

	@Override
	public void execute(String[] args) {

		try {
			module = args[0];
		} catch (Exception e) {
			ChatUtil.sendChatMsg("Incorrect Usage! \n" +
					"Correct Usage: =visible ModuleName True/False");
			return;
		}


		try {
			visible = Boolean.parseBoolean(args[1]);
		} catch (Exception e) {
			ChatUtil.sendChatMsg("Incorrect Usage! \n" +
					"Correct Usage: =visible ModuleName True/False");
			return;
		}

		try {
			ModuleRegistry.getModule(module).visible = visible;
		} catch (NullPointerException e) {
			ChatUtil.sendChatMsg("Cannot Find Module \"" + module + "\"!");
			return;
		}

		ChatUtil.sendChatMsg("Module: " + module + " Visibility set to " + visible);

	}
}

