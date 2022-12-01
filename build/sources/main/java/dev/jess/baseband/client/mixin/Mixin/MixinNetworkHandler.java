package dev.jess.baseband.client.mixin.Mixin;

import dev.jess.baseband.client.api.Listeners.PacketListener;
import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.impl.Modules.Render.PacketRender;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkHandler {
	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void sendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
		try {
			if (packet != null && BaseBand.isIngame()) {
				if (packet instanceof CPacketPlayer.Rotation || packet instanceof CPacketPlayer.PositionRotation) {
					PacketRender.setYaw(((CPacketPlayer) packet).getYaw(0));
					PacketRender.setPitch(((CPacketPlayer) packet).getPitch(0));
				}
				if (PacketListener.onPacketSend(packet))
					callbackInfo.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
			BaseBand.log.warn("Packet Listener NULL!");
		}
	}


	@Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void channelRead0(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {

		try {
			if (packet != null && BaseBand.isIngame()) {
				if (PacketListener.onPacketReceive(packet))
					callbackInfo.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
			BaseBand.log.warn("Packet Listener NULL!");
		}
	}

}
