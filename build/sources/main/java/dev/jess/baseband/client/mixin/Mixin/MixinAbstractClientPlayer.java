package dev.jess.baseband.client.mixin.Mixin;

import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.CapeManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(value = {AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer {
	@Shadow
	@Nullable
	protected abstract NetworkPlayerInfo getPlayerInfo();

	@Inject(method = {"getLocationCape"}, at = {@At(value = "HEAD")}, cancellable = true)
	public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
		if (ModuleRegistry.getInstance().getByName("Capes").isToggled()) {
			NetworkPlayerInfo info = this.getPlayerInfo();
			UUID uuid = null;
			if (info != null) {
				uuid = this.getPlayerInfo().getGameProfile().getId();
			}
			if (uuid != null && CapeManager.hasCape(uuid)) {
				if (CapeManager.isOg(uuid)) {
					callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/cape.png"));
				} else {
					callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/cape.png"));
				}
			}
		}
	}
}

