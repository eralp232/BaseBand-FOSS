package dev.jess.baseband.client.mixin.Mixin;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {


	@Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"))
	public void closeScreen(EntityPlayerSP entityPlayerSP) {
		if (ModuleRegistry.getModule("AntiGUIClose").isToggled()) return;
	}

	@Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
	public void noPushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
		if (ModuleRegistry.getModule("NoPush").isToggled()) {
			cir.cancel();
		}
	}

	@Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
	public void closeScreen(Minecraft minecraft, GuiScreen screen) {
		if (ModuleRegistry.getModule("AntiGUIClose").isToggled()) return;
	}

	@Inject(method = {"onUpdateWalkingPlayer"}, at = {@At(value = "HEAD")}, cancellable = true)
	private void preMotion(CallbackInfo info) {
		if (BaseBand.isIngame()) {
			for (int i = 0; i < ModuleRegistry.MODULES.size(); i++) {
				if (ModuleRegistry.INSTANCE.getModuleList().get(i).isToggled()) {
					ModuleRegistry.INSTANCE.getModuleList().get(i).updatePlayerMotion(0);
				}
			}
		}
	}


	@Inject(method = {"onUpdateWalkingPlayer"}, at = {@At(value = "RETURN")})
	private void postMotion(CallbackInfo info) {
		if (BaseBand.isIngame()) {
			for (int i = 0; i < ModuleRegistry.MODULES.size(); i++) {
				if (ModuleRegistry.INSTANCE.getModuleList().get(i).isToggled()) {
					ModuleRegistry.INSTANCE.getModuleList().get(i).updatePlayerMotion(1);
				}
			}
		}
	}

}
