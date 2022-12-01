package dev.jess.baseband.client.impl.Commands;

import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.ChatUtil;


public class Toggle extends Command {
	public Toggle() {
		super("Toggle", "toggles Modules", "toggle", "t");
	}

	@Override
	public void execute(String[] args) {
		if (args.length == 0) {
			ChatUtil.sendChatMsg("Please Specify a Module Name.");
			return;
		}
		Module toToggle = ModuleRegistry.getInstance().getByName(String.join(" ", args));
		if (toToggle == null) {
			ChatUtil.sendChatMsg("Module Not Found.");
			return;
		}
		toToggle.setToggled(! toToggle.isToggled());
		try {
			//BaseBand.clickGui.updateScreen(); //Eh?
		} catch (Exception e) {
			//E?
		}
	}
}
