package dev.jess.baseband.client.mixin.Mixin;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

@Mixin(value = GuiEditSign.class, priority = 9998)
public class MixinGuiEditSign {

	@Final
	@Shadow
	private TileEntitySign tileSign;


	@Inject(method = "initGui", at = @At("RETURN"), cancellable = true)
	public void initGui(CallbackInfo callback) {
		try {
			if (Objects.requireNonNull(ModuleRegistry.getModule("BaseBandSign")).isToggled() && tileSign.signText[1] != null && tileSign.signText[2] != null && tileSign.signText[3] != null && tileSign.signText[0] != null) {
				ITextComponent[] signText2 = new ITextComponent[]{new TextComponentString("BaseBand"),
						new TextComponentString(BaseBand.VERSION),
						new TextComponentString(Minecraft.getMinecraft().session.username),
						new TextComponentString(new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()))
				};
				tileSign.signText[0] = signText2[0];
				tileSign.signText[1] = signText2[1];
				tileSign.signText[2] = signText2[2];
				tileSign.signText[3] = signText2[3];
			}
		} catch (Exception e) {
		}
	}

}

