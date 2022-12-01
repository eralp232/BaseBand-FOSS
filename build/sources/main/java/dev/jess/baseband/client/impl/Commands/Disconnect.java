package dev.jess.baseband.client.impl.Commands;

import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;

public class Disconnect extends Command {
	public Disconnect() {
		super("Disconnect", "Disconnect with a given message.", "dc");
	}

	@Override
	public void execute(String[] args) {
		Wrapper.mc.getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString("Internal Exception: java.lang.NullPointerException")));
	}
}
