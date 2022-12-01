package dev.jess.baseband.client.impl.AzureGUI.click;


import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.AzureRenderUtils;
import dev.jess.baseband.client.impl.AzureGUI.click.buttons.ModuleButton;
import dev.jess.baseband.client.impl.AzureGUI.click.implement.Component;
import dev.jess.baseband.client.impl.Modules.Client.ColorModule;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {
	public static ArrayList<Panel> panels;

	public ClickGUI() {
		panels = new ArrayList<>();
		int panelX = 5;
		for (Category category : Category.values()) {
			final Panel panel = new Panel(category) {
				@Override
				public void setup() {
					int tempY = 15;
					for (Module module : ModuleRegistry.getModulesInCategory(category)) {
						ModuleButton button = new ModuleButton(module, this, tempY);
						components.add(button);
						tempY += 14;
					}
				}
			};
			panel.setX(panelX);
			panels.add(panel);
			panelX += panel.getWidth() + 10;
		}
	}

	public static Panel getPanel(String name) {
		for (Panel panel : panels) {
			if (name.equalsIgnoreCase(String.valueOf(panel.category))) {
				return panel;
			}
		}

		return null;
	}

	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		ScaledResolution resolution = new ScaledResolution(mc);

		AzureRenderUtils.drawGradientRect(0.0f, 0.0f, (float) resolution.getScaledWidth(), (float) resolution.getScaledHeight(), new Color(0, 0, 0, 0).getRGB(), ColorModule.getColor(130).getRGB());
		for (final Panel panel : panels) {
			panel.drawScreen();
			panel.updatePosition(mouseX, mouseY);
			for (final Component component : panel.getComponents()) {
				component.updateComponent(mouseX, mouseY);
			}
		}
	}

	protected void mouseClicked(final int mouseX, final int mouseY, final int button) {
		for (final Panel panel : panels) {
			if (panel.isWithinHeader(mouseX, mouseY) && button == 0) {
				panel.setDragging(true);
				panel.dragX = mouseX - panel.getX();
				panel.dragY = mouseY - panel.getY();
			}

			if (panel.isWithinHeader(mouseX, mouseY) && button == 1) {
				panel.setOpen(true);
			}

			if (panel.isOpen() && ! panel.getComponents().isEmpty()) {
				for (final Component component : panel.getComponents()) {
					component.mouseClicked(mouseX, mouseY, button);
				}
			}
		}
	}

	protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
		for (final Panel panel : panels) {
			panel.setDragging(false);
			if (panel.isOpen() && ! panel.getComponents().isEmpty()) {
				for (final Component component : panel.getComponents()) {
					component.mouseReleased(mouseX, mouseY, state);
				}
			}
		}
	}

	protected void keyTyped(final char typedChar, final int keyCode) {
		for (final Panel panel : panels) {
			if (panel.isOpen() && ! panel.getComponents().isEmpty()) {
				for (final Component component : panel.getComponents()) {
					component.keyTyped(typedChar, keyCode);
				}
			}
		}

		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(null);
		}
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void initGui() {
	}
}
