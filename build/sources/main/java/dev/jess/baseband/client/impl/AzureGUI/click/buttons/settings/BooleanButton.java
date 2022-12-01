package dev.jess.baseband.client.impl.AzureGUI.click.buttons.settings;

import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.AzureRenderUtils;
import dev.jess.baseband.client.impl.AzureGUI.click.buttons.ModuleButton;
import dev.jess.baseband.client.impl.AzureGUI.click.implement.Component;
import dev.jess.baseband.client.impl.AzureGUI.font.FontManager;
import dev.jess.baseband.client.impl.Modules.Client.ColorModule;

import java.awt.*;

public class BooleanButton extends Component {
	private final Setting setting;
	private final ModuleButton parent;
	private boolean hovered;
	private int offset;
	private int x;
	private int y;

	public BooleanButton(final Setting setting, final ModuleButton parent, final int offset) {
		this.setting = setting;
		this.parent = parent;
		this.x = parent.panel.getX();
		this.y = parent.panel.getY() + parent.offset;
		this.offset = offset;
	}

	@Override
	public void renderComponent() {
		AzureRenderUtils.drawRect(getParentX(), getParentY() + offset + 1, getParentX() + getParentWidth(), getParentY() + 14 + offset, new Color(0, 0, 0, 135).getRGB());
		AzureRenderUtils.drawRect(getParentX(), getParentY() + offset, getParentX() + getParentWidth(), getParentY() + 1 + offset, new Color(0, 0, 0, 135).getRGB());
		AzureRenderUtils.drawRect(getParentX() + 2, getParentY() + offset + 1, getParentX() + getParentWidth() - 2, getParentY() + 13 + offset, setting.getValBoolean() ? ColorModule.getColor(100).getRGB() : ColorModule.getColor(35).getRGB());
		FontManager.drawString(setting.getName(), getParentX() + 4, getParentY() + offset + 3, - 1);
	}

	@Override
	public void setOffset(final int offset) {
		this.offset = offset;
	}

	@Override
	public void updateComponent(final int mouseX, final int mouseY) {
		hovered = isMouseOnButton(mouseX, mouseY);
		x = parent.panel.getX();
		y = parent.panel.getY() + offset;
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button) {
		if (isMouseOnButton(mouseX, mouseY) && button == 0 && parent.open) {
			setting.setValBoolean(! setting.getValBoolean());
		}
	}

	public boolean isMouseOnButton(final int x, final int y) {
		return x > this.x && x < this.x + 90 && y > this.y && y < this.y + 14;
	}

	public int getParentX() {
		return parent.panel.getX();
	}

	public int getParentY() {
		return parent.panel.getY();
	}

	public int getParentWidth() {
		return parent.panel.getWidth();
	}
}
