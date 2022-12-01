package dev.jess.baseband.client.mixin.Mixin;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Registry.ConfigRegistry;
import dev.jess.baseband.client.api.Registry.FriendRegistry;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.FileNotFoundException;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Inject(method = "getLimitFramerate", at = @At("HEAD"), cancellable = true)
	public void getLimitFramerate(CallbackInfoReturnable<Integer> info) {
		if (BaseBand.shaders.currentshader != null && Minecraft.getMinecraft().player == null)
			info.setReturnValue(60);
	}

	@Inject(method = "shutdown", at = @At("HEAD"))
	public void shutdown(CallbackInfo info) {
		save();
	}

	private void save() {
		ConfigRegistry.save();
		try {
			new FriendRegistry().save();
			System.out.println("Saved Friends List");
		} catch (FileNotFoundException e) {

		}
		BaseBand.log.info("Exiting");
	}
}
