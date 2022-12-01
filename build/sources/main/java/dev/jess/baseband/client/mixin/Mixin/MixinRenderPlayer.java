package dev.jess.baseband.client.mixin.Mixin;

import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.impl.Modules.Render.PacketRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {

	@Inject(method = "renderEntityName", at = @At("HEAD"), cancellable = true)
	public void renderLivingLabel(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo info) {
		if (Objects.requireNonNull(ModuleRegistry.getModule("NameTags")).isToggled()) {
			info.cancel();
		}
	}


	private float
			renderPitch,
			renderYaw,
			renderHeadYaw,
			prevRenderHeadYaw,
			lastRenderHeadYaw = 0,
			prevRenderPitch,
			lastRenderPitch = 0;


	@Inject(method = "doRender", at = @At("HEAD"))
	private void rotateBegin(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
		if (ModuleRegistry.getModule("PacketRender").isToggled()
				&& entity == Minecraft.getMinecraft().player) {
			prevRenderHeadYaw = entity.prevRotationYawHead;
			prevRenderPitch = entity.prevRotationPitch;
			renderPitch = entity.rotationPitch;
			renderYaw = entity.rotationYaw;
			renderHeadYaw = entity.rotationYawHead;
			entity.rotationPitch = PacketRender.getPitch();
			entity.prevRotationPitch = lastRenderPitch;
			entity.rotationYaw = PacketRender.getYaw();
			entity.rotationYawHead = PacketRender.getYaw();
			entity.prevRotationYawHead = lastRenderHeadYaw;
		}
	}




	@Inject(method = "doRender", at = @At("RETURN"))
	private void rotateEnd(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
		if (ModuleRegistry.getModule("PacketRender").isToggled()
				&& entity == Minecraft.getMinecraft().player) {
			lastRenderHeadYaw = entity.rotationYawHead;
			lastRenderPitch = entity.rotationPitch;
			entity.rotationPitch = renderPitch;
			entity.rotationYaw = renderYaw;
			entity.rotationYawHead = renderHeadYaw;
			entity.prevRotationYawHead = prevRenderHeadYaw;
			entity.prevRotationPitch = prevRenderPitch;
		}
	}

}
