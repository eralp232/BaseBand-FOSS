package dev.jess.baseband.client.impl.Modules.Client;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Listeners.UpdateListener;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.util.Comparator;
import java.util.Random;

public class HUD extends Module {


	private final Setting moduleColor = new Setting("PerModuleColor", this, false);
	private final Setting rainbow2 = new Setting("Rainbow", this, false);
	private final Setting watermark = new Setting("Watermark", this, false);
	private final Setting versionSpoof = new Setting("Version Spoof", this, false);
	private final Setting getVersionSpoof = new Setting("Version Spoof Set", this, 1, 1, 6, true);
	private final Setting watermarkshadow = new Setting("WatermarkShadow", this, false);
	private final Setting coords = new Setting("Coordinates", this, false);
	private final Setting fakecoords = new Setting("FakeCoordinates", this, false);
	private final Setting customFont = new Setting("CFont", this, false);
	private final Setting moduleList = new Setting("ModuleList", this, false);
	private final Setting metaData = new Setting("MetaData", this, false);
	private final Setting metaDataColor = new Setting("MetaDataColor", this, false);
	private final Setting trans = new Setting("Cute", this, false);
	private final Setting renderUP = new Setting("HUDUP", this, false);
	private final Setting step = new Setting("Step", this, 10, 1, 20, true);
	private final Setting downRenderDebug = new Setting("PushUp", this, 6, 1, 30, true);
	private final Setting twoline = new Setting("Second Line", this, false);


	public HUD() {
		super("HUD", "Render HUD Elements", Category.CLIENT, new Color(232, 122, 11, 255).getRGB());
		BaseBand.settingsManager.rSetting(moduleColor);
		BaseBand.settingsManager.rSetting(rainbow2);
		BaseBand.settingsManager.rSetting(customFont);
		BaseBand.settingsManager.rSetting(renderUP);
		BaseBand.settingsManager.rSetting(metaData);
		BaseBand.settingsManager.rSetting(metaDataColor);
		BaseBand.settingsManager.rSetting(coords);
		BaseBand.settingsManager.rSetting(fakecoords);
		BaseBand.settingsManager.rSetting(downRenderDebug);
		BaseBand.settingsManager.rSetting(trans);
		BaseBand.settingsManager.rSetting(step);
		BaseBand.settingsManager.rSetting(moduleList);
		BaseBand.settingsManager.rSetting(watermark);
		BaseBand.settingsManager.rSetting(watermarkshadow);
		BaseBand.settingsManager.rSetting(twoline);
		BaseBand.settingsManager.rSetting(versionSpoof);
		BaseBand.settingsManager.rSetting(getVersionSpoof);
	}


	public static int rainbow(int delay) {
		double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
		rainbowState %= 360;
		return Color.getHSBColor((float) (rainbowState) / 360.0f, 0.5f, 1f).getRGB();
	}

	private int getCuteColor(int index) {

		int size = ModuleRegistry.toggledModules.size();

		int light_blue = new Color(91, 206, 250).getRGB();
		int white = Color.WHITE.getRGB();
		int pink = new Color(245, 169, 184).getRGB();

		int part = (int) ((float) index / size * 5);


		if (part == 0) {
			return light_blue;
		}
		if (part == 1) {
			return pink;
		}
		if (part == 2) {
			return white;
		}
		if (part == 3) {
			return pink;
		}
		if (part == 4) {
			return light_blue;
		}


		return light_blue; //fail
	}


	public void renderText() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int y = 2;

		int downy = sr.getScaledHeight() - (int) downRenderDebug.getValDouble();

		Module[] v;
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		boolean meta = false;


		if (customFont.getValBoolean()) {
			v = ModuleRegistry.toggledModules.stream()
					.sorted(Comparator.comparingDouble(value -> (value.getMcHudMeta().equals("") || ! metaData.getValBoolean()) ? BaseBand.cfr.getStringWidth(value.getName()) : BaseBand.cfr.getStringWidth(value.getName() + " " + "[" + value.getMcHudMeta() + "]"))) // I mean, it works?
					.toArray(Module[]::new);
		} else {
			v = ModuleRegistry.toggledModules.stream()
					.sorted(Comparator.comparingDouble(value -> (value.getMcHudMeta().equals("") || ! metaData.getValBoolean()) ? fr.getStringWidth(value.getName()) : fr.getStringWidth(value.getName() + " " + "[" + value.getMcHudMeta() + "]"))) // I mean, it works?
					.toArray(Module[]::new);
		}


		if (coords.getValBoolean()) {
			final int[] counter3 = {1};
			if (customFont.getValBoolean()) {
				if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
					BaseBand.cfr.drawStringWithShadow((! fakecoords.getValBoolean()) ? "X:" + String.format("%.2f", Minecraft.getMinecraft().player.posX) + " Y:" + String.format("%.2f", Minecraft.getMinecraft().player.posY) + " Z:" + String.format("%.2f", Minecraft.getMinecraft().player.posZ) : "X:" + String.format("%.2f", Minecraft.getMinecraft().player.posX + new Random().nextInt(100000)) + " Y:" + String.format("%.2f", Minecraft.getMinecraft().player.posY + new Random().nextInt(100000)) + " Z:" + String.format("%.2f", Minecraft.getMinecraft().player.posZ + new Random().nextInt(100000)), 2, downy, (rainbow2.getValBoolean()) ? counter3[0] * 300 : new Color(0, 255, 0).getRGB());
				}
			} else {
				if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
					Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((! fakecoords.getValBoolean()) ? "X:" + String.format("%.2f", Minecraft.getMinecraft().player.posX) + " Y:" + String.format("%.2f", Minecraft.getMinecraft().player.posY) + " Z:" + String.format("%.2f", Minecraft.getMinecraft().player.posZ) : "X:" + String.format("%.2f", Minecraft.getMinecraft().player.posX + new Random().nextInt(100000)) + " Y:" + String.format("%.2f", Minecraft.getMinecraft().player.posY + new Random().nextInt(100000)) + " Z:" + String.format("%.2f", Minecraft.getMinecraft().player.posZ + new Random().nextInt(100000)), 2, downy, (rainbow2.getValBoolean()) ? rainbow(counter3[0] * 300) : new Color(0, 255, 0).getRGB());
				}

			}
			counter3[0]++;
		}


		if (moduleList.getValBoolean()) {
			if (renderUP.getValBoolean()) {
				ArrayUtils.reverse(v);
				final int[] counter = {1};
				// intellij does this for you:
				// type `v.fori[enter]`
				for (int i = 0; i < v.length; i++) {
					Module mod = v[i];//m
					if (mod.visible) {
						meta = mod.getMcHudMeta().equals("");
						if (! metaData.getValBoolean() && ! meta) {
							meta = true;
						}

						if (metaDataColor.getValBoolean()) {
							if (trans.getValBoolean()) {
								if (meta) {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName()) - 2, y, getCuteColor(i));
									} else {
										fr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - fr.getStringWidth(mod.getName()) - 2, y, getCuteColor(i));
									}
								} else {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]", sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, y, getCuteColor(i));
									} else {
										fr.drawStringWithShadow(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]", sr.getScaledWidth() - fr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, y, getCuteColor(i));
									}
								}
							} else {

								if (meta) {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName()) - 2, y, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									} else {
										fr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - fr.getStringWidth(mod.getName()) - 2, y, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									}
								} else {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]", sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, y, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									} else {
										fr.drawStringWithShadow(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]", sr.getScaledWidth() - fr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, y, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									}
								}
							}
						} else {
							if (trans.getValBoolean()) {
								if (meta) {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName()) - 2, y, getCuteColor(i));
									} else {
										fr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - fr.getStringWidth(mod.getName()) - 2, y, getCuteColor(i));
									}
								} else {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName() + " " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + mod.getMcHudMeta() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET, sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, y, getCuteColor(i));
									} else {
										fr.drawStringWithShadow(mod.getName() + " " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + mod.getMcHudMeta() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET, sr.getScaledWidth() - fr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, y, getCuteColor(i));
									}
								}
							} else {

								if (meta) {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName()) - 2, y, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									} else {
										fr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - fr.getStringWidth(mod.getName()) - 2, y, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									}
								} else {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName() + " " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + mod.getMcHudMeta() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET, sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, y, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									} else {
										fr.drawStringWithShadow(mod.getName() + " " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + mod.getMcHudMeta() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET, sr.getScaledWidth() - fr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, y, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									}
								}
							}
						}


						y += step.getValDouble();
						counter[0]++;
					}
				}
				//Put down hud stuff here


			} else {
				final int[] counter = {1};
				// intellij does this for you:
				// type `v.fori[enter]`
				ArrayUtils.reverse(ArrayUtils.toArray(v));
				for (int i = 0; i < v.length; i++) {
					Module mod = v[i];//m
					if (mod.visible) {
						meta = mod.getMcHudMeta().equals("");
						if (! metaData.getValBoolean() && ! meta) {
							meta = true;
						}


						if (metaDataColor.getValBoolean()) {
							if (trans.getValBoolean()) {
								if (meta) {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName()) - 2, downy, getCuteColor(i));
									} else {
										fr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - fr.getStringWidth(mod.getName()) - 2, downy, getCuteColor(i));
									}
								} else {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]", sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, downy, getCuteColor(i));
									} else {
										fr.drawStringWithShadow(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]", sr.getScaledWidth() - fr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, downy, getCuteColor(i));
									}
								}
							} else {

								if (meta) {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName()) - 2, downy, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									} else {
										fr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - fr.getStringWidth(mod.getName()) - 2, downy, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									}
								} else {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]", sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, downy, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									} else {
										fr.drawStringWithShadow(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]", sr.getScaledWidth() - fr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, downy, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									}
								}
							}
						} else {
							if (trans.getValBoolean()) {
								if (meta) {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName()) - 2, downy, getCuteColor(i));
									} else {
										fr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - fr.getStringWidth(mod.getName()) - 2, downy, getCuteColor(i));
									}
								} else {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName() + " " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + mod.getMcHudMeta() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET, sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, downy, getCuteColor(i));
									} else {
										fr.drawStringWithShadow(mod.getName() + " " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + mod.getMcHudMeta() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET, sr.getScaledWidth() - fr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, downy, getCuteColor(i));
									}
								}
							} else {

								if (meta) {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName()) - 2, downy, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									} else {
										fr.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - fr.getStringWidth(mod.getName()) - 2, downy, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									}
								} else {
									if (customFont.getValBoolean()) {
										BaseBand.cfr.drawStringWithShadow(mod.getName() + " " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + mod.getMcHudMeta() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET, sr.getScaledWidth() - BaseBand.cfr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, downy, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									} else {
										fr.drawStringWithShadow(mod.getName() + " " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + mod.getMcHudMeta() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET, sr.getScaledWidth() - fr.getStringWidth(mod.getName() + " " + "[" + mod.getMcHudMeta() + "]") - 2, downy, (! rainbow2.getValBoolean()) ? (moduleColor.getValBoolean()) ? mod.getColor() : new Color(0, 255, 0, 255).getRGB() : rainbow(counter[0] * 300));
									}
								}
							}
						}


						downy -= step.getValDouble();
						counter[0]++;

					}


				}
				//Put Up Hud Stuff


			}
		}


		//Watermark
		if (watermark.getValBoolean()) {

			final int[] counter2 = {1};
			if (watermarkshadow.getValBoolean()) {
				if (customFont.getValBoolean()) {

					if (twoline.getValBoolean()) {
						if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
							BaseBand.cfr.drawStringWithShadow(UpdateListener.CHANGEABLE_NAME + (versionSpoof.getValBoolean() ? (int) getVersionSpoof.getValDouble() : " " + BaseBand.VERSION) + " " + "git-" + BaseBand.hash, 2, 12, (rainbow2.getValBoolean()) ? rainbow(counter2[0] * 300) : new Color(0, 255, 0).getRGB());
						}
					} else {
						if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
							BaseBand.cfr.drawStringWithShadow(UpdateListener.CHANGEABLE_NAME + (versionSpoof.getValBoolean() ? (int) getVersionSpoof.getValDouble() : " " + BaseBand.VERSION) + " " + "git-" + BaseBand.hash, 2, 2, (rainbow2.getValBoolean()) ? rainbow(counter2[0] * 300) : new Color(0, 255, 0).getRGB());
						}
					}

				} else {


					if (twoline.getValBoolean()) {
						if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
							Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(UpdateListener.CHANGEABLE_NAME + (versionSpoof.getValBoolean() ? (int) getVersionSpoof.getValDouble() : " " + BaseBand.VERSION) + " " + "git-" + BaseBand.hash, 2, 12, (rainbow2.getValBoolean()) ? rainbow(counter2[0] * 300) : new Color(0, 255, 0).getRGB());
						}
					} else {
						if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
							Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(UpdateListener.CHANGEABLE_NAME + (versionSpoof.getValBoolean() ? (int) getVersionSpoof.getValDouble() : " " + BaseBand.VERSION) + " " + "git-" + BaseBand.hash, 2, 2, (rainbow2.getValBoolean()) ? rainbow(counter2[0] * 300) : new Color(0, 255, 0).getRGB());
						}
					}
				}
			} else {
				if (customFont.getValBoolean()) {

					if (twoline.getValBoolean()) {
						if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
							BaseBand.cfr.drawString(UpdateListener.CHANGEABLE_NAME + (versionSpoof.getValBoolean() ? (int) getVersionSpoof.getValDouble() : " " + BaseBand.VERSION) + " " + "git-" + BaseBand.hash, 2, 12, (rainbow2.getValBoolean()) ? rainbow(counter2[0] * 300) : new Color(0, 255, 0).getRGB());
						}
					} else {
						if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
							BaseBand.cfr.drawString(UpdateListener.CHANGEABLE_NAME + (versionSpoof.getValBoolean() ? (int) getVersionSpoof.getValDouble() : " " + BaseBand.VERSION) + " " + "git-" + BaseBand.hash, 2, 2, (rainbow2.getValBoolean()) ? rainbow(counter2[0] * 300) : new Color(0, 255, 0).getRGB());
						}
					}

				} else {


					if (twoline.getValBoolean()) {
						if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
							Minecraft.getMinecraft().fontRenderer.drawString(UpdateListener.CHANGEABLE_NAME + (versionSpoof.getValBoolean() ? (int) getVersionSpoof.getValDouble() : " " + BaseBand.VERSION) + " " + "git-" + BaseBand.hash, 2, 12, (rainbow2.getValBoolean()) ? rainbow(counter2[0] * 300) : new Color(0, 255, 0).getRGB());
						}
					} else {
						if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
							Minecraft.getMinecraft().fontRenderer.drawString(UpdateListener.CHANGEABLE_NAME + (versionSpoof.getValBoolean() ? (int) getVersionSpoof.getValDouble() : " " + BaseBand.VERSION) + " " + "git-" + BaseBand.hash, 2, 2, (rainbow2.getValBoolean()) ? rainbow(counter2[0] * 300) : new Color(0, 255, 0).getRGB());
						}
					}
				}
			}
			counter2[0]++;
		}

	}
}
