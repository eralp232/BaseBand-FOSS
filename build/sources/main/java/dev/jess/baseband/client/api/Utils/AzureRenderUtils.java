package dev.jess.baseband.client.api.Utils;


import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

import static dev.jess.baseband.client.api.Utils.Wrapper.mc;

public class AzureRenderUtils {
	private static final Frustum frustum = new Frustum();

	public static void drawRect(float x, float y, float width, float height, int color) {
		float alpha = (color >> 24 & 0xFF) / 255.0f;
		float red = (color >> 16 & 0xFF) / 255.0f;
		float green = (color >> 8 & 0xFF) / 255.0f;
		float blue = (color & 0xFF) / 255.0f;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferBuilder.pos(x, height, 0.0).color(red, green, blue, alpha).endVertex();
		bufferBuilder.pos(width, height, 0.0).color(red, green, blue, alpha).endVertex();
		bufferBuilder.pos(width, y, 0.0).color(red, green, blue, alpha).endVertex();
		bufferBuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawGradientRect(float x, float y, float width, float height, int startColor, int endColor) {
		float startAlpha = (startColor >> 24 & 0xFF) / 255.0f;
		float startRed = (startColor >> 16 & 0xFF) / 255.0f;
		float startGreen = (startColor >> 8 & 0xFF) / 255.0f;
		float startBlue = (startColor & 0xFF) / 255.0f;

		float endAlpha = (endColor >> 24 & 0xFF) / 255.0f;
		float endRed = (endColor >> 16 & 0xFF) / 255.0f;
		float endGreen = (endColor >> 8 & 0xFF) / 255.0f;
		float endBlue = (endColor & 0xFF) / 255.0f;

		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		vertexBuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexBuffer.pos(x + width, y, 0.0).color(startRed, startGreen, startBlue, startAlpha).endVertex();
		vertexBuffer.pos(x, y, 0.0).color(startRed, startGreen, startBlue, startAlpha).endVertex();
		vertexBuffer.pos(x, y + height, 0.0).color(endRed, endGreen, endBlue, endAlpha).endVertex();
		vertexBuffer.pos(x + width, y + height, 0.0).color(endRed, endGreen, endBlue, endAlpha).endVertex();
		tessellator.draw();

		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static boolean isOnCamera(Entity entity) {
		return isOnCamera(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
	}

	public static boolean isOnCamera(AxisAlignedBB bb) {
		Entity viewEntity = mc.getRenderViewEntity();
		if (viewEntity != null) frustum.setPosition(viewEntity.posX, viewEntity.posY, viewEntity.posZ);
		return frustum.isBoundingBoxInFrustum(bb);
	}
}
