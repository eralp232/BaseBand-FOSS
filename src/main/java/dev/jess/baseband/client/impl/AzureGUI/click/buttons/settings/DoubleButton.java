package dev.jess.baseband.client.impl.AzureGUI.click.buttons.settings;

import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.AzureRenderUtils;
import dev.jess.baseband.client.impl.AzureGUI.click.buttons.ModuleButton;
import dev.jess.baseband.client.impl.AzureGUI.click.implement.Component;
import dev.jess.baseband.client.impl.AzureGUI.font.FontManager;
import dev.jess.baseband.client.impl.Modules.Client.ColorModule;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleButton extends Component {
	private final Setting setting;
	private final ModuleButton parent;
	private boolean hovered;
	private double renderWidth;
	private boolean dragging;
	private int offset;
	private int x;
	private int y;

	public DoubleButton(final Setting setting, final ModuleButton parent, final int offset) {
		this.setting = setting;
		this.parent = parent;
		this.x = parent.panel.getX();
		this.y = parent.panel.getY() + parent.offset;
		this.offset = offset;
		this.dragging = false;
	}

	private static double roundToPlace(final double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public void renderComponent() {
		AzureRenderUtils.drawRect(getParentX(), getParentY() + offset + 1, getParentX() + getParentWidth(), getParentY() + 14 + offset, new Color(0, 0, 0, 135).getRGB());
		AzureRenderUtils.drawRect(getParentX(), getParentY() + offset, getParentX() + getParentWidth(), getParentY() + 1 + offset, new Color(0, 0, 0, 135).getRGB());
		AzureRenderUtils.drawRect(getParentX() + 2, getParentY() + offset + 1, getParentX() + getParentWidth() - 2, getParentY() + 13 + offset, ColorModule.getColor(35).getRGB());
		AzureRenderUtils.drawRect(getParentX() + 2, getParentY() + offset + 1, getParentX() + (int) renderWidth, getParentY() + 13 + offset, ColorModule.getColor(100).getRGB());
		FontManager.drawString(setting.getName() + ": " + setting.getValDouble(), getParentX() + 4, getParentY() + offset + 3, - 1);
	}

	@Override
	public void setOffset(final int offset) {
		this.offset = offset;
	}

	@Override
	public void updateComponent(final int mouseX, final int mouseY) {
		hovered = isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY);
		x = parent.panel.getX();
		y = parent.panel.getY() + offset;
		final double diff = Math.min(100, Math.max(0, mouseX - x));
		final double max = setting.getMax();
		final double min = setting.getMin();
		renderWidth = 88.0f * (setting.getValDouble() - min) / (max - min);
		if (dragging) {
			if (diff == 0.0) {
				setting.setValDouble(setting.getMin());
			} else {
				final double newValue = roundToPlace(diff / 100.0 * (max - min) + min);
				setting.setValDouble(newValue);
			}
		}
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button) {
		if (this.isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
			this.dragging = true;
		}
		if (this.isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
			this.dragging = true;
		}
	}

	@Override
	public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
		this.dragging = false;
	}

	public boolean isMouseOnButtonD(final int x, final int y) {
		return x > this.x && x < this.x + (getParentWidth() / 2 + 1) && y > this.y && y < this.y + 14;
	}

	public boolean isMouseOnButtonI(final int x, final int y) {
		return x > this.x + getParentWidth() / 2 && x < this.x + getParentWidth() && y > this.y && y < this.y + 14;
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
