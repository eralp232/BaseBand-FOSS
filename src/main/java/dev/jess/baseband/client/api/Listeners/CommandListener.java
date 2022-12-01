package dev.jess.baseband.client.api.Listeners;

import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Registry.CommandRegistry;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class CommandListener {

	@SubscribeEvent
	public void onCommandSend(ClientChatEvent e) {
		String message = e.getOriginalMessage();
		if (message.startsWith("=")) {
			Wrapper.mc.ingameGUI.getChatGUI().addToSentMessages(message);
			e.setCanceled(true);
			String messageWithoutPrefix = message.substring(1);
			String[] cmdAndArgs = messageWithoutPrefix.split(" +");
			String command = cmdAndArgs[0].toLowerCase();
			String[] args = Arrays.copyOfRange(cmdAndArgs, 1, cmdAndArgs.length);
			Command toExecute = CommandRegistry.getInstance().getByAlias(command);
			if (toExecute == null) {
				toExecute = CommandRegistry.getInstance().getByName(command);
				if (toExecute == null) {
					ChatUtil.sendChatMsg("Command Not Found.");
				} else toExecute.execute(args);
			} else toExecute.execute(args);
		}
	}
}
