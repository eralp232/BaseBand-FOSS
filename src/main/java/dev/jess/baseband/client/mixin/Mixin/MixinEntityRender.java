package dev.jess.baseband.client.mixin.Mixin;

import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.impl.Modules.Render.NoHurtCam;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRender {

	@Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;rayTraceBlocks(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/RayTraceResult;"))
	public RayTraceResult rayTraceBlocks(WorldClient world, Vec3d start, Vec3d end) {
		if (ModuleRegistry.getModule("CameraClip").isToggled())
			return null;
		else
			return world.rayTraceBlocks(start, end);
	}


	@Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
	public void hurtCameraEffect(float ticks, CallbackInfo info) {
		if (NoHurtCam.shouldDisable()) info.cancel();
	}
}
