package dev.jess.baseband.client.impl.GUI;

import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.RenderingMethods;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class Slider extends Component {

	private final Setting set;
	private final dev.jess.baseband.client.impl.GUI.Button parent;
	private boolean hovered;
	private int offset;
	private int x;
	private int y;
	private boolean dragging = false;

	private double renderWidth;

	public Slider(Setting value, Button button, int offset) {
		this.set = value;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	private static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public void renderComponent() {

		int umodR = 0;
		int umodG = 130;
		int umodB = 130;

		RenderingMethods.drawBorderedRectReliantGui(x, parent.parent.getY() + offset - 0.5, x + parent.parent.getWidth() - 11.5, parent.parent.getY() + offset + 12, 1, new Color(25, 25, 25, 128).getRGB(), Color.gray.getRGB());


		final int drag = (int) (this.set.getValDouble() / this.set.getMax() * this.parent.parent.getWidth());
		Gui.drawRect(x, parent.parent.getY() + offset, x + (int) renderWidth, parent.parent.getY() + offset + 12, new Color(umodR, umodG, umodB).getRGB());
		Gui.drawRect(x, parent.parent.getY() + offset, x, parent.parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();

		Wrapper.fr.drawStringWithShadow(this.set.getName() + " - " + Math.round(set.getValDouble() * 100D) / 100D, x + 2, (parent.parent.getY() + offset + 2), - 1);

		GL11.glPopMatrix();
	}

	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX() + parent.parent.getWidth() + 5;

		double diff = Math.min(88, Math.max(0, mouseX - this.x));

		double min = set.getMin();
		double max = set.getMax();

		renderWidth = (88) * (set.getValDouble() - min) / (max - min);

		if (dragging) {
			if (diff == 0) {
				set.setValDouble(set.getMin());
				//Wrapper.getFileManager().saveSettingsList();
			} else {
				double newValue = roundToPlace(((diff / 88) * (max - min) + min), 2);
				set.setValDouble(newValue);
				//Wrapper.fileManager.saveSettingsList();
			}
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
		}
		if (isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}

	public boolean isMouseOnButtonD(int x, int y) {
		return x > this.x - 2 && x < this.x + (parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 12 + 1;
	}

	public boolean isMouseOnButtonI(int x, int y) {
		return x > this.x + parent.parent.getWidth() / 2 && x < this.x + parent.parent.getWidth() && y > this.y && y < this.y + 12 + 1;
	}
}
