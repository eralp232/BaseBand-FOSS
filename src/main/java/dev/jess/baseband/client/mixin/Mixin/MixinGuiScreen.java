package dev.jess.baseband.client.mixin.Mixin;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {


	public boolean shadow;


	@Inject(method = "drawScreen", at = @At(value = "RETURN"))
	public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo callback) {
		try {
			ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
			if (BaseBand.unload) {
				shadow = ! shadow;
				Wrapper.fr.drawString("!!THIS IS AN UNLICENSED COPY OF BASEBAND!! !!THIS IS AN UNLICENSED COPY OF BASEBAND!!!!THIS IS AN UNLICENSED COPY OF BASEBAND!! !!THIS IS AN UNLICENSED COPY OF BASEBAND!!", new Random().nextInt(sr.getScaledWidth()), new Random().nextInt(sr.getScaledHeight()), 0xffff0000, shadow);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
