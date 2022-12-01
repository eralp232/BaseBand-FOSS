package dev.jess.baseband.client.impl.CatGUI.component.components.sub;

import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.impl.CatGUI.component.Component;
import dev.jess.baseband.client.impl.CatGUI.component.components.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class ModeButton extends Component {

	private final Button parent;
	private final Setting set;
	private final Module mod;
	private boolean hovered;
	private int offset;
	private int x;
	private int y;
	private int modeIndex;

	public ModeButton(Setting set, Button button, Module mod, int offset) {
		this.set = set;
		this.parent = button;
		this.mod = mod;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
		this.modeIndex = 0;
	}

	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}

	@Override
	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() * 1), parent.parent.getY() + offset + 12, this.hovered ? 0xFF222222 : 0xFF111111);
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Mode: " + set.getValString(), (parent.parent.getX() + 7) * 2, (parent.parent.getY() + offset + 2) * 2 + 5, - 1);
		GL11.glPopMatrix();
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			int maxIndex = set.getOptions().size();

			if (modeIndex + 1 > maxIndex)
				modeIndex = 0;
			else
				modeIndex++;

			set.setValString(set.getOptions().get(modeIndex));
		}
	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}
