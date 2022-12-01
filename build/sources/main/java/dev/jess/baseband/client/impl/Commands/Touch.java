package dev.jess.baseband.client.impl.Commands;

import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class Touch extends Command {
	public Touch() {
		super("Touch", "Touch Things from Far Away.", "touch");
	}

	@Override
	public void execute(String[] args) {

		if (args.length == 0) {
			ChatUtil.sendChatMsg("Usage: =touch XCoord ZCoord ListenBool");
			return;
		}
		int x, z;
		boolean listen;
		try {
			x = Integer.parseInt(args[0]);
			z = Integer.parseInt(args[1]);
		} catch (Exception e) {
			return;
		}

		try {
			listen = Boolean.parseBoolean(args[2]);
		} catch (Exception e) {
			listen = false;
		}

		if (! listen) {
			Wrapper.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(x, 0, z), EnumFacing.UP));
			ChatUtil.sendChatMsg("Touched X:" + x + " Y:0 Z:" + z);
		}
		if (listen) {
			int finalX = x;
			int finalZ = z;
			new Thread(() -> {
				Objects.requireNonNull(ModuleRegistry.getModule("NoCom-Master")).setToggled(true);
				Wrapper.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(finalX, 0, finalZ), EnumFacing.UP));
				ChatUtil.sendChatMsg("Touched X:" + finalX + " Y:0 Z:" + finalZ);
				try {
					Thread.sleep(Objects.requireNonNull(Wrapper.mc.getConnection()).getPlayerInfo(Wrapper.mc.getConnection().getGameProfile().getId()).getResponseTime() * 2L + 100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Objects.requireNonNull(ModuleRegistry.getModule("NoCom-Master")).setToggled(false);
			}, "NoCom-Touch").start();
		}
	}
}
