package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ArmourHUD extends Module {
	private static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
	private final Setting damage = new Setting("Damage", this, false);

	public ArmourHUD() {
		super("ArmourHUD", "YIKES", Category.RENDER, new Color(124, 90, 208, 255).getRGB());
		BaseBand.settingsManager.rSetting(damage);
	}

	public static int toHex(final int r, final int g, final int b) {
		return 0xFF000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
	}

	public void renderText() {
		GlStateManager.enableTexture2D();

		ScaledResolution resolution = new ScaledResolution(mc);
		int i = resolution.getScaledWidth() / 2;
		int iteration = 0;
		int y = resolution.getScaledHeight() - 55 - (mc.player.isInWater() ? 10 : 0);
		for (ItemStack is : mc.player.inventory.armorInventory) {
			iteration++;
			if (is.isEmpty()) continue;
			int x = i - 90 + (9 - iteration) * 20 + 2;
			GlStateManager.enableDepth();

			itemRender.zLevel = 200F;
			itemRender.renderItemAndEffectIntoGUI(is, x, y);
			itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
			itemRender.zLevel = 0F;

			GlStateManager.enableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();

			String s = is.getCount() > 1 ? is.getCount() + "" : "";
			mc.fontRenderer.drawStringWithShadow(s, x + 19 - 2 - mc.fontRenderer.getStringWidth(s), y + 9, 0xffffff);

			if (damage.getValBoolean()) {
				float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
				float red = 1 - green;
				int dmg = 100 - (int) (red * 100);
				mc.fontRenderer.drawStringWithShadow(dmg + "", x + 8 - mc.fontRenderer.getStringWidth(dmg + "") / 2, y - 11, toHex((int) (red * 255), (int) (green * 255), 0));
			}
		}

		GlStateManager.enableDepth();
		GlStateManager.disableLighting();
	}


}
