package dev.jess.baseband.client.mixin.Mixin;

import dev.jess.baseband.client.impl.Modules.Render.CrystalRender;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderEnderCrystal.class)
public class MixinRenderEnderCrystal {
	@Redirect(method = {"doRender"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
	public void doRender(final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
		final CrystalRender chams = CrystalRender.INSTANCE;
		if (CrystalRender.INSTANCE.isToggled()) {
			CrystalRender.drawWireframe(modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			//final Colorr color = Colors.getColor();
			GL11.glPushMatrix();
			GL11.glPushAttrib(1048575);
			GL11.glDisable(3008);
			GL11.glDisable(3553);
			GL11.glDisable(2896);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			GL11.glLineWidth(2.0f);
			GL11.glDisable(2929);
			GL11.glDepthMask(false);
			GL11.glEnable(2848);
			GL11.glHint(3154, 4354);
			GL11.glColor4f((float) chams.r.getValDouble() / 255.0f, (float) chams.g.getValDouble() / 255.0f, (float) chams.b.getValDouble() / 255.0f, (float) chams.a.getValDouble() / 255.0f);
			modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			GL11.glDisable(2848);
			GL11.glEnable(2929);
			GL11.glDepthMask(true);
			GL11.glEnable(3042);
			GL11.glEnable(2896);
			GL11.glEnable(3553);
			GL11.glEnable(3008);
			GL11.glPopAttrib();
			GL11.glPopMatrix();
		} else {
			modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}
}
