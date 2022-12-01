package dev.jess.baseband.client.mixin.Mixin;

import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(Entity.class)
public class MixinEntity {

	@Redirect(method = "applyEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
	public void velocity(Entity entity, double x, double y, double z) {
		if (ModuleRegistry.getModule("NoPush").isToggled() && entity.equals(Minecraft.getMinecraft().player)) {
			//empty if statement to cancel Collision
		} else {
			entity.motionX += x;
			entity.motionY += y;
			entity.motionZ += z;
			entity.isAirBorne = true;
		}
	}


}
