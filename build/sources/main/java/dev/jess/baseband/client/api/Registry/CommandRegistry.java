package dev.jess.baseband.client.api.Registry;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.impl.Commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandRegistry {
	static List<Command> COMMANDS = new ArrayList<>();
	private static CommandRegistry INSTANCE = null;

	public CommandRegistry() {
		if (INSTANCE != null) {
			throw new IllegalStateException("Command Registry already exists!");
		}
		INSTANCE = this;
		registerCommands();
	}

	public static void register(Command command) {
		COMMANDS.add(command);
	}


	public static List<Command> getAll() {
		return COMMANDS;
	}

	public static CommandRegistry getInstance() {
		if (INSTANCE == null) new CommandRegistry();
		return INSTANCE;
	}

	void registerCommands() {
		register(new Toggle());
		register(new Help());
		register(new Modules());
		register(new FakePlayer());
		register(new Touch());
		register(new Bind());
		register(new Drawn());
		register(new Disconnect());
		register(new Friends());
		register(new LoadSong());
	}

	public Command getByName(String name) {
		for (Command command : COMMANDS) {
			if (command.getName().equalsIgnoreCase(name)) return command;
		}
		return null;
	}

	public Command getByAlias(String search) {
		for (Command command : COMMANDS) {
			if (Arrays.stream(command.aliases).anyMatch(s -> s.equalsIgnoreCase(search))) return command;
		}
		return null;
	}
}
