package dev.jess.baseband.client.api.Command;

public abstract class Command {
	public String[] aliases;
	String name;
	String description;

	public Command(String name, String description, String... aliases) {
		this.name = name;
		this.description = description;
		this.aliases = aliases;
	}

	public abstract void execute(String[] args);

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String[] getAliases() {
		return aliases;
	}
}
