package dev.jess.baseband.client.impl.Commands;

import com.mojang.authlib.GameProfile;
import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.GameType;

import java.util.UUID;

public class FakePlayer extends Command {
	boolean bool = false;
	private String name;

	public FakePlayer() {
		super("FakePlayer", "Like Future.", "fakeplayer");
	}

	@Override
	public void execute(String[] args) {
		if (! bool) {
			bool = true;

			name = "rd_132211";

			if (Wrapper.mc.player == null && Wrapper.mc.world == null) {
				return;
			}


			EntityOtherPlayerMP clonedPlayer = new EntityOtherPlayerMP(Wrapper.mc.world, new GameProfile(UUID.fromString("bfe75cb4-ea3f-4791-a058-3f04aa5e839a"), name));
			clonedPlayer.copyLocationAndAnglesFrom(Wrapper.mc.player);
			clonedPlayer.rotationYawHead = Wrapper.mc.player.rotationYawHead;
			clonedPlayer.rotationYaw = Wrapper.mc.player.rotationYaw;
			clonedPlayer.rotationPitch = Wrapper.mc.player.rotationPitch;
			clonedPlayer.setGameType(GameType.SURVIVAL);
			clonedPlayer.setHealth(20);
			Wrapper.mc.world.addEntityToWorld(Integer.MAX_VALUE - 1337, clonedPlayer);


			clonedPlayer.onLivingUpdate();

			ChatUtil.sendChatMsg("FakePlayer Spawned.");
		} else {
			bool = false;
			try {
				Wrapper.mc.world.removeEntityFromWorld(Integer.MAX_VALUE - 1337);
			} catch (Exception e) {

			}
			ChatUtil.sendChatMsg("FakePlayer Despawned.");
		}
	}
}
