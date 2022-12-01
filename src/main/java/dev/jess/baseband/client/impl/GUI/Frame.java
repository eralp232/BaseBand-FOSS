package dev.jess.baseband.client.impl.GUI;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.RenderingMethods;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;


public class Frame {

	private final int width;
	private final int barHeight;
	public ArrayList<Component> components;
	public Category category;
	public boolean open;
	public int y;
	public int x;
	public int dragX;
	public int dragY;
	public boolean visable;
	private boolean isDragging;
	private boolean isHovered;
	private boolean isPinned;
	private boolean isExtendButtonHovered;
	private int dragged;

	public Frame(Category cat) {
		this.components = new ArrayList<Component>();
		this.category = cat;
		this.width = 100;
		this.x = 5;
		this.y = 5;
		this.barHeight = 13;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;
		int tY = this.barHeight + 3;

		ScaledResolution sr = new ScaledResolution(Wrapper.mc);

		ArrayList<Module> mods = ModuleRegistry.toggledModules;

		mods.sort(new Comparator<Module>() {

			@Override
			public int compare(Module o1, Module o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (Module m : ModuleRegistry.MODULES) {
			if (! m.getCategory().equals(cat)) continue;
			dev.jess.baseband.client.impl.GUI.Button modButton = new Button(m, this, tY);
			this.components.add(modButton);
			tY += 14;
		}

		isPinned = false;
	}


	public ArrayList<Component> getComponents() {
		return components;
	}

	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void renderFrame(FontRenderer fontRenderer) {

		RenderingMethods.drawBorderedRectReliantGui(this.x, this.y, this.x + this.width, this.y + this.barHeight, 1, new Color(25, 25, 25, 128).getRGB(), Color.gray.getRGB());

		Wrapper.fr.drawStringWithShadow(this.category.name(), this.x + 3, this.y + 2, 0xFFFFFFFF);

		//RenderingMethods.drawBorderedRectReliantGui(x + width - 2 - 10, y + 2, x + width - 2 - 8 - 10, y + 10, 1, -1, 0xff000000);

		RenderingMethods.drawBorderedRectReliantGui(x + width - 2, y + 2, x + width - 2 - 8, y + 10, 1, open ? new Color(0, 170, 170).getRGB() : 0x00000000, Color.gray.getRGB());

		if (this.open) {
			if (! this.components.isEmpty()) {
				RenderingMethods.drawBorderedRectReliant(this.x, this.y + this.barHeight + 1, this.x + this.width, this.y + this.barHeight + (14 * components.size()) + 2, 1, new Color(25, 25, 25, 128).getRGB(), Color.gray.getRGB());
				for (Component component : components) {
					component.renderComponent();
				}
			}
		}
	}

	public void refresh() {
		int off = this.barHeight;
		for (Component comp : components) {
			comp.setOff(off);
			off += 14;
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public int getY() {
		return y;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public int getWidth() {
		return width;
	}

	public void updatePosition(int mouseX, int mouseY) {
		this.isHovered = this.isWithinHeader(mouseX, mouseY);
		this.isExtendButtonHovered = isWithinExtendRange(mouseX, mouseY);
		if (this.isDragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}

	public boolean isWithinHeader(int x, int y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
	}

	public boolean isWithinExtendRange(int x, int y) {
		return x <= this.x + this.width - 2 && x >= this.x + this.width - 2 - 8 && y >= this.y + 2 && y <= this.y + 10;
	}

	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean pinned) {
		this.isPinned = pinned;
	}
}
