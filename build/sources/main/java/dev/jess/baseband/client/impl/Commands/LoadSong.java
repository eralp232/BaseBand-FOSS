package dev.jess.baseband.client.impl.Commands;

import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import dev.jess.baseband.client.api.Utils.ObfuscatedHelper;
import dev.jess.baseband.client.api.Utils.Wrapper;
import dev.jess.baseband.client.impl.Modules.World.NoteBot;

import java.io.File;

public class LoadSong extends Command {

	public LoadSong() {
		super("LoadSong", "Load notebot songs", "ls", "song", "notebot");
	}

	@Override
	public void execute(String[] args) {
		String s = String.join(" ", args);
		try {
			NoteBot.registers = NoteBot.createRegister(new File(Wrapper.mc.gameDir + File.separator + "BaseBand" + File.separator + "notebot" + File.separator + s), s);
			ChatUtil.sendChatMsg("Song Loaded! (" + s + ")");
		} catch (Exception e) {
			if (! ObfuscatedHelper.check()) {
				e.printStackTrace();
			}
			ChatUtil.sendChatMsg("Song Not Found! (" + s + ")");
		}
	}

}
