package dev.jess.baseband.client.mixin.Mixin;


import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Listeners.UpdateListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinMainMenu extends GuiScreen {
	@Inject(method = {"drawScreen"}, at = {@At("TAIL")})
	public void renderWatermark(final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo info) {
		final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(TextFormatting.GREEN + UpdateListener.CHANGEABLE_NAME + "-" + BaseBand.VERSION + "-" + BaseBand.hash, 2.0f, (float) (sr.getScaledHeight() - 60), 0xFFFFFFFF);
	}

}

