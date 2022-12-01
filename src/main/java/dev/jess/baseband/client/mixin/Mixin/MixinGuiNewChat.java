package dev.jess.baseband.client.mixin.Mixin;

import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = GuiNewChat.class, priority = 9999)
public class MixinGuiNewChat extends Gui {
	@Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
	private void drawRectHook(int left, int top, int right, int bottom, int color) {
		Gui.drawRect(left, top, right, bottom, ModuleRegistry.getModule("ClearChat").isToggled() ? 0 : color);
	}

	@Redirect(method = "setChatLine", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0, remap = false))
	public int drawnChatLinesSize(List<ChatLine> list) {
		return ModuleRegistry.getModule("InfiniteChat").isToggled() ? - 2147483647 : list.size();
	}

	@Redirect(method = "setChatLine", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2, remap = false))
	public int chatLinesSize(List<ChatLine> list) {
		return ModuleRegistry.getModule("InfiniteChat").isToggled() ? - 2147483647 : list.size();
	}
}
