package dev.jess.baseband.client.impl.GUI;

import dev.jess.baseband.client.api.Utils.RenderingMethods;
import dev.jess.baseband.client.api.Utils.Wrapper;

import java.awt.*;


public class PinableFrame {

	public boolean open;
	public int defaultWidth;
	public int y;
	public int x;
	public int barHeight;
	public int dragX;
	public int dragY;
	public String title;
	public String[] text;
	private int width;
	private boolean isDragging;
	private boolean isHovered;
	private boolean isPinned;
	private boolean isExtendButtonHovered;

	public PinableFrame(String title, String[] text, int y) {
		this.title = title;
		this.text = text;

		this.width = 88;
		this.defaultWidth = 88;
		this.x = 200;
		this.y = y;
		this.barHeight = 13;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;

	}

	public void renderFrame() {

		RenderingMethods.drawBorderedRectReliantGui(this.x, this.y, this.x + this.width, this.y + this.barHeight, 1, new Color(25, 25, 25, 128).getRGB(), Color.gray.getRGB());


		RenderingMethods.drawBorderedRectReliantGui(x + width - 2 - 10, y + 2, x + width - 2 - 8 - 10, y + 10, 1, isPinned ? new Color(0, 170, 171).getRGB() : 0x00000001, Color.gray.getRGB());

		RenderingMethods.drawBorderedRectReliantGui(x + width - 2, y + 2, x + width - 2 - 8, y + 10, 1, open ? new Color(0, 170, 170).getRGB() : 0x00000000, Color.gray.getRGB());

		Wrapper.fr.drawStringWithShadow(title, this.x + 3, this.y + 3, 0xFFFFFFFF);


	}

	public void renderFrameText() {
		if (this.open) {
			RenderingMethods.drawBorderedRectReliantGui(this.x, this.y + this.barHeight + 1, this.x + this.width, this.y + this.barHeight + (10 * text.length) + 3, 1, new Color(25, 25, 25, 128).getRGB(), Color.gray.getRGB());

			int yCount = this.y + this.barHeight + 3;
			for (String line : text) {
				Wrapper.fr.drawString(line, this.x + 3, yCount, - 1);
				yCount += 10;
			}

		}
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

	public boolean isWithinPinRange(int x, int y) {
		return x <= this.x + this.width - 2 - 10 && x >= this.x + this.width - 2 - 8 - 10 && y >= this.y + 2 && y <= this.y + 10;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void setDrag(boolean drag) {
		this.isDragging = drag;
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

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean pinned) {
		this.isPinned = pinned;
	}
}
