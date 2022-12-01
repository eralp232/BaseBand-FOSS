package dev.jess.baseband.client.impl.GUI;

import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.RenderingMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;


public class ModeButton extends Component {

	private final dev.jess.baseband.client.impl.GUI.Button parent;
	private final Setting set;
	private final Module mod;
	private boolean hovered;
	private int offset;
	private int x;
	private int y;

	public ModeButton(Setting set, Button button, Module mod, int offset) {
		this.set = set;
		this.parent = button;
		this.mod = mod;
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX() + parent.parent.getWidth() + 5;
		this.offset = offset;
	}

	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}

	@Override
	public void renderComponent() {

		int umodR = 0;
		int umodG = 130;
		int umodB = 130;

		RenderingMethods.drawBorderedRectReliantGui(x, parent.parent.getY() + offset - 0.5, x + parent.parent.getWidth() - 11.5, parent.parent.getY() + offset + 12, 1, new Color(25, 25, 25, 128).getRGB(), Color.gray.getRGB());

		Gui.drawRect(x, parent.parent.getY() + offset, x + parent.parent.getWidth() - 12, parent.parent.getY() + offset + 12, new Color(umodR, umodG, umodB).getRGB());

		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(set.getName() + set.getValString(), x + 2, (parent.parent.getY() + offset + 2), - 1);
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX() + parent.parent.getWidth() + 5;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			set.setValString(set.getOptions().get(getModeIndexForward()));
		}
	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12 + 1;
	}

	public int getModeIndexForward() {
		for (int i = 0; i < set.getOptions().size(); i++) {
			if (set.getOptions().get(i).equals(set.getValString())) {
				if (i + 1 >= set.getOptions().size()) {
					return 0;
				}
				return i + 1;
			}
		}
		return 0;
	}

}
