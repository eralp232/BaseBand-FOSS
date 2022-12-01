package dev.jess.baseband.client.impl.FutureGUI;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.impl.FutureGUI.item.ModuleButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public final class ClickGui
		extends GuiScreen {
	private static ClickGui clickGui;
	//    public final CustomFont guiFont = new CustomFont("Segoe UI", 18.0f);
	private final ArrayList<Panel> panels = new ArrayList();

	public ClickGui() {
		if (this.getPanels().isEmpty()) {
			this.load();
		}
	}

	public static ClickGui getClickGui() {
		return clickGui == null ? (clickGui = new ClickGui()) : clickGui;
	}

	private void load() {
		int x = - 84;
		for (Category category : Category.values()) {
			this.panels.add(new Panel(category.name(), x += 90, 4, true) {

				@Override
				public void setupItems() {
					BaseBand.moduleManager.getModuleList().forEach(module -> {
						if (module.getCategory() == category) {
							this.addButton(new ModuleButton(module));
						}
						//if (module instanceof Toggleable && !module.getLabel().equalsIgnoreCase("Tab Gui") && !module.getLabel().equalsIgnoreCase("Click Gui") && (toggleableModule = (ToggleableModule)module).getModuleType().equals((Object)moduleType)) {
						//this.addButton(new ModuleButton(module));
						//}
					});
				}
			});
		}
        /*
        this.panels.add(new Panel("Always Active", x += 90, 4, true){

            @Override
            public void setupItems() {
                Exeter.getInstance().getModuleManager().getRegistry().forEach(module -> {
                    if (!(module instanceof Toggleable || module.getLabel().equalsIgnoreCase("Tab Gui") || module.getLabel().equalsIgnoreCase("Click Gui"))) {
                        this.addButton(new ModuleButton((Module)module));
                    }
                });
            }
        });
         */
		this.panels.forEach(panel -> panel.getItems().sort((item1, item2) -> item1.getLabel().compareTo(item2.getLabel())));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		RenderMethods.drawGradientRect(0.0F, 0.0F, mc.displayWidth, mc.displayHeight, 536870912, - 1879048192);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
		this.panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
	}

//    @Override
//    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        this.drawDefaultBackground();
//        this.panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
//    }

	@Override
	public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
		this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, clickedButton));
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
		this.panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, releaseButton));
	}

	@Override
	protected void keyTyped(final char typedChar, final int keyCode) {
		for (Panel panel : panels) {
			panel.keyTyped(typedChar, keyCode);
		}

		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(null);
		}
	}


	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public ArrayList<Panel> getPanels() {
		return this.panels;
	}
}

