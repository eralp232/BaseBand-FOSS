package dev.jess.baseband.client.impl.FutureGUI.item;


public class Item {
	private final String label;
	protected float x;
	protected float y;
	protected int width;
	protected int height;

	public Item(String label) {
		this.label = label;
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	}

	public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
	}

	public void keyTyped(char keyTyped, int keyCode) {

	}


	public final String getLabel() {
		return this.label;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}

