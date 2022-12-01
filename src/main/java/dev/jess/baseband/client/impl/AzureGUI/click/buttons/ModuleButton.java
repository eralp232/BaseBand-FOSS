package dev.jess.baseband.client.impl.AzureGUI.click.buttons;


import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.AzureRenderUtils;
import dev.jess.baseband.client.impl.AzureGUI.click.Panel;
import dev.jess.baseband.client.impl.AzureGUI.click.buttons.settings.*;
import dev.jess.baseband.client.impl.AzureGUI.click.implement.Component;
import dev.jess.baseband.client.impl.AzureGUI.font.FontManager;
import dev.jess.baseband.client.impl.Modules.Client.ColorModule;

import java.awt.*;
import java.util.ArrayList;

public class ModuleButton extends Component {
	private final ArrayList<Component> components;
	public Module module;
	public Panel panel;
	public int offset;
	public boolean open;
	private boolean isHovered;

	public ModuleButton(final Module module, final Panel panel, final int offset) {
		this.module = module;
		this.panel = panel;
		this.offset = offset;
		this.components = new ArrayList<>();
		this.open = false;
		int tempY = offset + 16;


		if (BaseBand.settingsManager.getSettingsByMod(module) != null && ! BaseBand.settingsManager.getSettingsByMod(module).isEmpty()) {
			for (final Setting setting : BaseBand.settingsManager.getSettingsByMod(module)) {
				if (setting.isCheck()) {
					components.add(new BooleanButton(setting, this, tempY));
					tempY += 16;
				}

				if (setting.isCombo()) {
					components.add(new ModeButton(setting, this, tempY));
					tempY += 16;
				}

				if (setting.isSlider() && setting.onlyInt()) {
					components.add(new IntegerButton(setting, this, tempY));
					tempY += 16;
				}

				if (setting.isSlider() && ! setting.onlyInt()) {
					components.add(new DoubleButton(setting, this, tempY));
					tempY += 16;
				}


			}
		}


		components.add(new BindButton(this, tempY));
	}

	public ArrayList<Component> getSubComponents() {
		return components;
	}

	@Override
	public void renderComponent() {
		AzureRenderUtils.drawRect(panel.getX(), panel.getY() + offset + 1, panel.getX() + panel.getWidth(), panel.getY() + 14 + offset, new Color(0, 0, 0, 135).getRGB());
		AzureRenderUtils.drawRect(panel.getX(), panel.getY() + offset, panel.getX() + panel.getWidth(), panel.getY() + offset + 1, new Color(0, 0, 0, 135).getRGB());
		AzureRenderUtils.drawRect(panel.getX() + 86, panel.getY() + offset + 1, panel.getX() + panel.getWidth() - 2, panel.getY() + offset + 13, module.isToggled() ? ColorModule.getColor().getRGB() : new Color(0, 0, 0, 135).getRGB());
		FontManager.drawString(module.getName(), panel.getX() + 3, panel.getY() + offset + 3, module.isToggled() ? ColorModule.getColor().getRGB() : - 1);

		if (open && ! components.isEmpty()) {
			for (final Component component : components) {
				component.renderComponent();
			}
		}
	}

	@Override
	public void setOffset(final int offset) {
		this.offset = offset;
		int tempY = offset + 14;
		for (final Component component : components) {
			component.setOffset(tempY);
			tempY += 14;
		}
	}

	@Override
	public int getHeight() {
		if (open) {
			return 14 * (components.size() + 1);
		}

		return 14;
	}

	@Override
	public void updateComponent(final int mouseX, final int mouseY) {
		isHovered = isMouseOnButton(mouseX, mouseY);
		if (! components.isEmpty()) {
			for (final Component component : components) {
				component.updateComponent(mouseX, mouseY);
			}
		}
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button) {
		if (isMouseOnButton(mouseX, mouseY) && button == 0) {
			module.setToggled(! module.isToggled());
		}

		if (isMouseOnButton(mouseX, mouseY) && button == 1) {
			open = ! open;
			panel.refresh();
		}

		for (final Component component : components) {
			component.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void mouseReleased(final int mouseX, final int mouseY, final int button) {
		for (final Component component : components) {
			component.mouseReleased(mouseX, mouseY, button);
		}
	}

	@Override
	public void keyTyped(final char typedChar, final int key) {
		for (final Component component : components) {
			component.keyTyped(typedChar, key);
		}
	}

	public boolean isMouseOnButton(final int x, final int y) {
		return x > panel.getX() && x < panel.getX() + panel.getWidth() && y > panel.getY() + offset && y < panel.getY() + 14 + offset;
	}
}
