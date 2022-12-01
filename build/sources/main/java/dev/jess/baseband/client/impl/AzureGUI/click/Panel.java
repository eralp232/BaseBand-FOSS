package dev.jess.baseband.client.impl.AzureGUI.click;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Utils.AzureRenderUtils;
import dev.jess.baseband.client.impl.AzureGUI.click.buttons.ModuleButton;
import dev.jess.baseband.client.impl.AzureGUI.click.implement.Component;
import dev.jess.baseband.client.impl.AzureGUI.font.FontManager;
import dev.jess.baseband.client.impl.Modules.Client.ColorModule;

import java.awt.*;
import java.util.ArrayList;

public class Panel {
	private final int width;
	public ArrayList<Component> components;
	public Category category;
	public int x;
	public int y;
	public int dragX;
	public int dragY;
	public boolean open;
	private boolean isDragging;

	public Panel(final Category category) {
		this.components = new ArrayList<>();
		this.category = category;
		this.open = true;
		this.isDragging = false;
		this.x = 5;
		this.y = 5;
		this.dragX = 0;
		this.width = 90;
		setup();
		refresh();
	}

	public void setup() {

	}


	public ArrayList<Component> getComponents() {
		return components;
	}

	public int getWidth() {
		return width;
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public void drawScreen() {
		AzureRenderUtils.drawRect(x, y - 1, x + width, y + 13, ColorModule.getColor().getRGB());
		AzureRenderUtils.drawRect(x, y + 13, x + width, y + 14, new Color(0, 0, 0, 135).getRGB());
		FontManager.drawString(category.toString(), x + 3.0f, y + 2.0f, - 1);

		int order = 14;

		if (open && ! components.isEmpty()) {
			for (final Component component : components) {
				component.renderComponent();
				if (component instanceof ModuleButton) {
					ModuleButton button = (ModuleButton) component;
					if (button.open) {
						for (Component ignored : button.getSubComponents()) {
							order += 14;
						}
					}
				}
				order += 14;
			}
		}

		AzureRenderUtils.drawRect(x, y + order, x + width, y + order + 1, new Color(0, 0, 0, 135).getRGB());
		AzureRenderUtils.drawRect(x, y + order + 1, x + width, y + order + 15, ColorModule.getColor(225).getRGB());
	}

	public void updatePosition(final int mouseX, final int mouseY) {
		if (isDragging) {
			setX(mouseX - dragX);
			setY(mouseY - dragY);
		}
	}

	public boolean isWithinHeader(final int x, final int y) {
		return x >= this.x && x <= this.x + width && y >= this.y && y < this.y + 14;
	}

	public void setDragging(final boolean dragging) {
		this.isDragging = dragging;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(final boolean open) {
		this.open = open;
	}

	public void refresh() {
		int offset = 14;
		for (final Component component : components) {
			component.setOffset(offset);
			offset += component.getHeight();
		}
	}
}
