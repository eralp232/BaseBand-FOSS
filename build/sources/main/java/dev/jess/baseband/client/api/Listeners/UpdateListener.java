package dev.jess.baseband.client.api.Listeners;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.KamiGuiDisconnected;
import dev.jess.baseband.client.api.Utils.Wrapper;
import dev.jess.baseband.client.impl.GUI.ClickGui;
import dev.jess.baseband.client.impl.GUI.PinableFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.codec.digest.DigestUtils;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class UpdateListener {


	public static String CHANGEABLE_NAME = "BaseBand";
	public static float partialrenderticks = 0;

	@SubscribeEvent
	public void onUpdate(TickEvent.ClientTickEvent t) {
		for (Module module : ModuleRegistry.getInstance().getAll()) {
			if (BaseBand.isIngame()) {
				if (! BaseBand.unload) {
					if (module.isToggled()) module.tick();
				}
			}
		}
	}

	@SubscribeEvent
	public void guiReconnect(final GuiOpenEvent event) {
		if (event.getGui() instanceof GuiDisconnected && ModuleRegistry.getModule("AutoReconnect").isToggled()) {
			// this.updateLastConnectedServer();
			//if (AutoLog.getInstance().isOff()) {

			final GuiDisconnected disconnected = (GuiDisconnected) event.getGui();
			event.setGui(new KamiGuiDisconnected(disconnected));
			//}
		}
	}

	@SubscribeEvent
	public void overlayBlockRenderEvent(RenderBlockOverlayEvent event) {
		if (ModuleRegistry.getModule("NoFire").isToggled() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE)
			event.setCanceled(true);
		if (ModuleRegistry.getModule("NoBlock").isToggled() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.BLOCK)
			event.setCanceled(true);

	}

	@SubscribeEvent
	public void overlayGameRenderEvent(RenderGameOverlayEvent event) {
		if (ModuleRegistry.getModule("NoPotionIcon").isToggled() && event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS)
			event.setCanceled(true);
	}

    /*
    @SubscribeEvent
    public void anvilUpdate(AnvilUpdateEvent e){
        ItemStack change = e.getRight();

        String changename = change.getDisplayName();

        change.setTranslatableName(changename.replace("&","§"));
        e.setOutput(change);
    }
     */

	@SubscribeEvent
	public void onBlockClick(PlayerInteractEvent.LeftClickBlock event) {
		if (! BaseBand.isIngame() || ! Objects.requireNonNull(ModuleRegistry.getModule("PacketMine")).isToggled())
			return;

		if (Wrapper.mc.world.getBlockState(event.getPos()).getBlock().getBlockHardness(Wrapper.mc.world.getBlockState(event.getPos()), Wrapper.mc.world, event.getPos()) != - 1) {
			Wrapper.mc.player.swingArm(EnumHand.MAIN_HAND);
			Wrapper.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), Objects.requireNonNull(event.getFace())));
			Wrapper.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
			// if (renderBlock == null && render.getBooleanValue()) renderBlock = event.getPos();
		}
	}

	@SubscribeEvent
	public void onUpdate(ClientChatReceivedEvent t) {
		if (ModuleRegistry.getModule("ChatTimeStamps").isToggled()) {
			TextComponentString newTextComponentString = new TextComponentString(ChatFormatting.GREEN + "[" + new SimpleDateFormat("k:mm").format(new Date()) + "]" + ChatFormatting.RESET + " ");
			newTextComponentString.appendSibling(t.getMessage());

			t.setMessage(newTextComponentString);
		}
	}

	@SubscribeEvent
	public void requiredUpdates(TickEvent.ClientTickEvent t) {
		if (! Objects.requireNonNull(ModuleRegistry.getModule("Management")).isToggled()) {
			Objects.requireNonNull(ModuleRegistry.getModule("Management")).setToggled(true);
		}

		CHANGEABLE_NAME = BaseBand.settingsManager.getSettingByName(ModuleRegistry.getModule("Management"), "Client Name: ").getValString();


	}

	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent e) {
		partialrenderticks = Wrapper.mc.getRenderPartialTicks();
		for (Module module : ModuleRegistry.getInstance().getAll()) {
			if (BaseBand.isIngame()) {
				if (! BaseBand.unload) {
					if (module.isToggled()) {
						Wrapper.mc.profiler.startSection("baseband");
						GlStateManager.disableTexture2D();
						GlStateManager.enableBlend();
						GlStateManager.disableAlpha();
						GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
						GlStateManager.shadeModel(7425);
						GlStateManager.disableDepth();
						GlStateManager.glLineWidth(1.0F);
						module.renderWorld();
						GlStateManager.glLineWidth(1.0F);
						GlStateManager.shadeModel(7424);
						GlStateManager.disableBlend();
						GlStateManager.enableAlpha();
						GlStateManager.enableTexture2D();
						GlStateManager.enableDepth();
						GlStateManager.enableCull();
						GlStateManager.enableCull();
						GlStateManager.depthMask(true);
						GlStateManager.enableTexture2D();
						GlStateManager.enableBlend();
						GlStateManager.enableDepth();
						Wrapper.mc.profiler.endSection();
					}
				}
			}
		}
	}


	@SubscribeEvent
	public void onOverlayRender(RenderGameOverlayEvent.Post e) {
		for (Module module : ModuleRegistry.getInstance().getAll()) {
			if (BaseBand.isIngame()) {
				if (! BaseBand.unload) {
					if (module.isToggled()) module.renderOverlay();
				}
			}
		}

	}


	@SubscribeEvent
	public void onInputMove(InputUpdateEvent e) {
		if (Wrapper.mc.player.isHandActive() && ! Wrapper.mc.player.isRiding() && Objects.requireNonNull(ModuleRegistry.getModule("NoUseSlow")).isToggled()) {
			e.getMovementInput().moveStrafe *= 5;
			e.getMovementInput().moveForward *= 5;
		}
	}

	@SubscribeEvent
	public void nhackHudHook(RenderGameOverlayEvent e) {
		if (e.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
			if (Wrapper.mc.currentScreen == BaseBand.gui) {
			} else {
				for (PinableFrame frame : ClickGui.pinableFrames) {
					if (frame.isPinned()) {


						frame.renderFrame();
						frame.renderFrameText();
					}
				}

			}
		}

	}


	@SubscribeEvent
	public void onRenderText(RenderGameOverlayEvent r) {
		if (r.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {
			for (Module module : ModuleRegistry.getInstance().getAll()) {
				if (! BaseBand.unload) {
					if (BaseBand.isIngame()) {
						if (module.isToggled()) module.renderText();
					}
				} else {
					Minecraft.getMinecraft().gameSettings.limitFramerate = 5;
					ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
					FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
					for (int h = 0; h < sr.getScaledHeight(); h += fr.FONT_HEIGHT) {
						for (int i = 0; i < sr.getScaledWidth(); i = i + fr.getStringWidth("!!UNLICENSED!!")) {
							if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
								Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("!!UNLICENSED!!", i, h, new Color(100, 255, 100).getRGB());
							}
						}
					}
				}
			}
		}
	}


	@SubscribeEvent
	public void onRecieveChat(ClientChatReceivedEvent e) {
		if (Objects.requireNonNull(ModuleRegistry.getModule("NoUnicode")).isToggled()) {
			String check = e.getMessage().getUnformattedText();
			char r = 0;
			for (char c : check.toCharArray()) {
				if (c > 0x1000) {
					check.replace(c, r);
				}
			}
			e.setMessage(new TextComponentString(check));
		}
	}


	@SubscribeEvent
	public void onChat(ClientChatEvent r) {
		if (! BaseBand.unload) {


			if (Objects.requireNonNull(ModuleRegistry.getModule("HashChat")).isToggled() && ! r.getMessage().startsWith("/") && ! r.getMessage().startsWith("!") && ! r.getMessage().startsWith(".") && ! r.getMessage().startsWith(",") && ! r.getMessage().startsWith("*") && ! r.getMessage().startsWith("=")) {
				String s = r.getMessage();
				int salt = r.getMessage().hashCode();
				String completemsg;


				switch (BaseBand.settingsManager.getSettingByName(ModuleRegistry.getModule("HashChat"), "Mode: ").getValString()) {
					case "SHA-1":
						if (BaseBand.settingsManager.getSettingByName(ModuleRegistry.getModule("HashChat"), "Salt").getValBoolean()) {
							completemsg = DigestUtils.sha1Hex(s + salt);
						} else {
							completemsg = DigestUtils.sha1Hex(s);
						}
						break;
					case "SHA-256":
						if (BaseBand.settingsManager.getSettingByName(ModuleRegistry.getModule("HashChat"), "Salt").getValBoolean()) {
							completemsg = DigestUtils.sha256Hex(s + salt);
						} else {
							completemsg = DigestUtils.sha256Hex(s);
						}
						break;
					case "SHA-384":
						if (BaseBand.settingsManager.getSettingByName(ModuleRegistry.getModule("HashChat"), "Salt").getValBoolean()) {
							completemsg = DigestUtils.sha384Hex(s + salt);
						} else {
							completemsg = DigestUtils.sha384Hex(s);
						}
						break;
					case "SHA-512":
						if (BaseBand.settingsManager.getSettingByName(ModuleRegistry.getModule("HashChat"), "Salt").getValBoolean()) {
							completemsg = DigestUtils.sha512Hex(s + salt);
						} else {
							completemsg = DigestUtils.sha512Hex(s);
						}
						break;
					case "MD-2":
						if (BaseBand.settingsManager.getSettingByName(ModuleRegistry.getModule("HashChat"), "Salt").getValBoolean()) {
							completemsg = DigestUtils.md2Hex(s + salt);
						} else {
							completemsg = DigestUtils.md2Hex(s);
						}
						break;
					case "MD-5":
						if (BaseBand.settingsManager.getSettingByName(ModuleRegistry.getModule("HashChat"), "Salt").getValBoolean()) {
							completemsg = DigestUtils.md5Hex(s + salt);
						} else {
							completemsg = DigestUtils.md5Hex(s);
						}
						break;
					default:
						completemsg = s;
						break;
				}


				r.setMessage(completemsg);
			}

		}
	}


}
