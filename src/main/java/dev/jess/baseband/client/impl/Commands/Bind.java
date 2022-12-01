package dev.jess.baseband.client.impl.Commands;


import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import org.lwjgl.input.Keyboard;

import java.util.Locale;


public class Bind extends Command {
	public Bind() {
		super("Bind", "Bind Modules", "Bind", "bind");
	}

	@Override
	public void execute(String[] args) {
		if (args.length == 0) {
			ChatUtil.sendChatMsg("Please Specify a Module Name.");
			return;
		}


		Module toToggle = ModuleRegistry.getInstance().getByName(args[0]);
		if (toToggle == null) {
			ChatUtil.sendChatMsg("Module Not Found.");
			return;
		}

		if (args.length == 1) {
			ChatUtil.sendChatMsg("Unbound Module " + toToggle.getName());
			toToggle.setKey(0);
			return;
		} else {
			try {
				toToggle.setKey(Keyboard.getKeyIndex(args[1].toUpperCase(Locale.ROOT)));
				ChatUtil.sendChatMsg("Set Key of " + toToggle.getName() + " to " + args[1].toUpperCase(Locale.ROOT));
			} catch (Exception e) {
				ChatUtil.sendChatMsg("Failed to set Keybind!");
			}
		}
	}
}

