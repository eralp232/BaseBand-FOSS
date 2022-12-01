package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class CrystalRender extends Module {


	public static CrystalRender INSTANCE;
	public final Setting r = new Setting("Red", this, 2, 0, 255, true);
	public final Setting g = new Setting("Green", this, 2, 0, 255, true);
	public final Setting b = new Setting("Blue", this, 2, 0, 255, true);
	public final Setting a = new Setting("Alpha", this, 2, 0, 255, true);

	public CrystalRender() {
		super("CrystalRender", "ket", Category.RENDER, new Color(253, 131, 8, 255).getRGB());
		BaseBand.settingsManager.rSetting(r);
		BaseBand.settingsManager.rSetting(g);
		BaseBand.settingsManager.rSetting(b);
		BaseBand.settingsManager.rSetting(a);
		INSTANCE = this;
	}


	public static void drawWireframe(final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleIn) {
		GL11.glPushMatrix();
		GL11.glPushAttrib(1048575);
		GL11.glPolygonMode(1032, 6913);
		GL11.glDisable(3553);
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		GL11.glEnable(2848);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glColor3f((float) INSTANCE.r.getValDouble() / 255.0f, (float) INSTANCE.g.getValDouble() / 255.0f, (float) INSTANCE.b.getValDouble() / 255.0f);
		GL11.glLineWidth(1.0f);
		model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleIn);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
}
