package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Listeners.UpdateListener;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityStatus;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TotemPopChams extends Module {


	private static final HashMap<EntityOtherPlayerMP, Long> popFakePlayerMap = new HashMap<>();
	private final Setting self = new Setting("Render Self", this, false);
	private final Setting rainbow = new Setting("Rainbow", this, false);
	private final Setting rainbowSpeed = new Setting("Rainbow Speed", this, 2, 1, 10, true);
	private final Setting fadeAlpha = new Setting("Fade Alpha", this, 50, 1, 255, true);
	private final Setting alpha = new Setting("Alpha", this, 45, 1, 255, true);
	private final Setting red = new Setting("Red", this, 50, 1, 255, true);
	private final Setting green = new Setting("Green", this, 50, 1, 255, true);
	private final Setting blue = new Setting("Blue", this, 50, 1, 255, true);
	private final Setting timeToRemove = new Setting("Remove Time", this, 1000, 1, 3000, true);
	private final Setting lineWidth = new Setting("Line Width", this, 2, 1, 4, true);


	public TotemPopChams() {
		super("TotemPopChams", "GeraldHack moment", Category.RENDER, new Color(75, 164, 66, 255).getRGB());
		BaseBand.settingsManager.rSetting(self);
		BaseBand.settingsManager.rSetting(rainbow);
		BaseBand.settingsManager.rSetting(rainbowSpeed);
		BaseBand.settingsManager.rSetting(fadeAlpha);
		BaseBand.settingsManager.rSetting(alpha);
		BaseBand.settingsManager.rSetting(red);
		BaseBand.settingsManager.rSetting(green);
		BaseBand.settingsManager.rSetting(blue);
		BaseBand.settingsManager.rSetting(timeToRemove);
		BaseBand.settingsManager.rSetting(lineWidth);
	}

	public static Color genRainbow(int delay) {
		double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
		return Color.getHSBColor((float) (rainbowState % 360.0 / 360.0), 1f, 1f);
	}

	public void disable() {
		popFakePlayerMap.clear();
	}

	public void renderWorld() {
		for (Map.Entry<EntityOtherPlayerMP, Long> entry : new HashMap<>(popFakePlayerMap).entrySet()) {
			float factor = ((float) timeToRemove.getValDouble() - entry.getValue()) / (float) timeToRemove.getValDouble();
			float r = (rainbow.getValBoolean() ? genRainbow((int) rainbowSpeed.getValDouble()).getRed() : (int) red.getValDouble()) / 255f;
			float g = (rainbow.getValBoolean() ? genRainbow((int) rainbowSpeed.getValDouble()).getGreen() : (int) green.getValDouble()) / 255f;
			float b = (rainbow.getValBoolean() ? genRainbow((int) rainbowSpeed.getValDouble()).getBlue() : (int) blue.getValDouble()) / 255f;
			float a = (clamp((int) ((factor) * fadeAlpha.getValDouble()), 0, 255)) / 255f;
			if (System.currentTimeMillis() - entry.getValue() > (long) timeToRemove.getValDouble()) {
				popFakePlayerMap.remove(entry.getKey());
				continue;
			}
			GL11.glPushMatrix();
			GL11.glDepthRange(0.01, 1.0f);

			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glLineWidth((float) lineWidth.getValDouble());
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
			GL11.glColor4f(r, g, b, 1f);
			renderEntityStatic(entry.getKey(), UpdateListener.partialrenderticks, true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glColor4f(1f, 1f, 1f, 1f);
			GL11.glEnable(GL11.GL_TEXTURE_2D);

			GL11.glPushAttrib(GL11.GL_ALL_CLIENT_ATTRIB_BITS);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GL11.glLineWidth(1.5f);
			GL11.glColor4f(r, g, b, (int) alpha.getValDouble() / 255f);
			renderEntityStatic(entry.getKey(), UpdateListener.partialrenderticks, true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1f, 1f, 1f, 1f);
			GL11.glPopAttrib();
			GL11.glDepthRange(0.0, 1.0f);
			GL11.glPopMatrix();
		}
	}


	public void onPop(Entity e) {
		if (mc.world.getEntityByID(e.getEntityId()) != null) {
			final Entity entity = mc.world.getEntityByID(e.getEntityId());
			if (entity instanceof EntityPlayer) {
				final EntityPlayer player = (EntityPlayer) entity;
				if (entity == mc.player && ! self.getValBoolean()) return;
				final EntityOtherPlayerMP fakeEntity = new EntityOtherPlayerMP(mc.world, player.getGameProfile());
				fakeEntity.copyLocationAndAnglesFrom(player);
				fakeEntity.rotationYawHead = player.rotationYawHead;
				fakeEntity.prevRotationYawHead = player.rotationYawHead;
				fakeEntity.rotationYaw = player.rotationYaw;
				fakeEntity.prevRotationYaw = player.prevRotationYaw;
				fakeEntity.rotationPitch = player.rotationPitch;
				fakeEntity.prevRotationPitch = player.prevRotationPitch;
				fakeEntity.cameraYaw = fakeEntity.rotationYaw;
				fakeEntity.cameraPitch = fakeEntity.rotationPitch;
				fakeEntity.setSneaking(player.isSneaking());
				popFakePlayerMap.put(fakeEntity, System.currentTimeMillis());
			}
		}
	}


	public void renderEntityStatic(Entity entityIn, float partialTicks, boolean p_188388_3_) {
		if (entityIn.ticksExisted == 0) {
			entityIn.lastTickPosX = entityIn.posX;
			entityIn.lastTickPosY = entityIn.posY;
			entityIn.lastTickPosZ = entityIn.posZ;
		}

		double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
		double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
		double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
		float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
		int i = entityIn.getBrightnessForRender();

		if (entityIn.isBurning()) {
			i = 15728880;
		}

		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
		mc.getRenderManager().renderEntity(entityIn, d0 - mc.getRenderManager().viewerPosX, d1 - mc.getRenderManager().viewerPosY, d2 - mc.getRenderManager().viewerPosZ, f, partialTicks, p_188388_3_);
	}

	public int clamp(final int num, final int min, final int max) {
		return (num < min) ? min : Math.min(num, max);
	}


	public boolean onPacketReceive(Packet<?> packet) {
		if (packet instanceof SPacketEntityStatus) {
			SPacketEntityStatus packet2 = (SPacketEntityStatus) packet;
			if (packet2.getOpCode() == 35) {
				Entity entity = packet2.getEntity(mc.world);
				onPop(entity);
			}
		}
		return false;
	}
}
