package dev.jess.baseband.client.impl.GUI;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.RenderingMethods;
import dev.jess.baseband.client.api.Utils.Wrapper;

import java.awt.*;
import java.util.ArrayList;


public class Button extends Component {

	private final ArrayList<Component> subcomponents;
	private final int height;
	public Module mod;
	public Frame parent;
	public int offset;
	public boolean open;
	private boolean isHovered;

	public Button(Module mod, Frame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<Component>();
		this.open = false;
		height = 12;
		int opY = offset;
		if (BaseBand.settingsManager.getSettingsByMod(mod) != null) {
			for (Setting s : BaseBand.settingsManager.getSettingsByMod(mod)) {
				if (s.isCombo()) {
					this.subcomponents.add(new ModeButton(s, this, mod, opY));
					opY += 12;
				}
				if (s.isSlider()) {
					this.subcomponents.add(new Slider(s, this, opY));
					opY += 12;
				}
				if (s.isCheck()) {
					this.subcomponents.add(new CheckBox(s, this, opY));
					opY += 12;
				}
			}
		}
	}

	@Override
	public void setOff(int newOff) {
		/*
		offset = newOff;
		int opY = offset + 14;
		for(Component comp : this.subcomponents) {
			comp.setOff(opY);
			opY += 14;
		}
		*/
	}

	@Override
	public void renderComponent() {

		int smodR = 0;
		int smodG = 170;
		int smodB = 170;
		int umodR = 0;
		int umodG = 130;
		int umodB = 130;

		RenderingMethods.drawGuiRect(parent.getX() + 2, this.parent.getY() + offset, BaseBand.settingsManager.getSettingsByMod(mod) != null ? parent.getX() + parent.getWidth() - 2 - 12 : parent.getX() + parent.getWidth() - 2, this.parent.getY() + 12 + offset, this.mod.isToggled() ? new Color(smodR, smodG, smodB).getRGB() : new Color(umodR, umodG, umodB).getRGB());

		Wrapper.fr.drawStringWithShadow(this.mod.getName(), parent.getX() + parent.getWidth() / 2 - Wrapper.fr.getStringWidth(this.mod.getName()) / 2, this.parent.getY() + offset + 2, - 1);


		if (BaseBand.settingsManager.getSettingsByMod(mod) != null) {
			RenderingMethods.drawGuiRect(parent.getX() + parent.getWidth() - 2 - 10, this.parent.getY() + offset, parent.getX() + parent.getWidth() - 2, this.parent.getY() + 12 + offset, new Color(umodR, umodG, umodB).getRGB());
		}


		if (this.open) {
			if (! this.subcomponents.isEmpty()) {
				for (Component comp : this.subcomponents) {
					comp.renderComponent();
				}
			}
		}


	}

	@Override
	public int getHeight() {
		/*
		if(this.open) {
			return (12 * (this.subcomponents.size() + 1));
		}
		*/
		return 12;
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.isHovered = isMouseOnButton(mouseX, mouseY);

		if (! this.subcomponents.isEmpty()) {
			for (Component comp : this.subcomponents) {
				comp.updateComponent(mouseX, mouseY);
			}
		}

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButton(mouseX, mouseY) && button == 0) {
			this.mod.setToggled(! this.mod.isToggled());
		}
		if (isMouseOnOptionsButton(mouseX, mouseY) && (button == 1 || button == 0)) {
			this.open = ! this.open;
			this.parent.refresh();
		}

		for (Component comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}

	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

		for (Component comp : this.subcomponents) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}

	}

	@Override
	public void keyTyped(char typedChar, int key) {

		for (Component comp : this.subcomponents) {
			comp.keyTyped(typedChar, key);
		}

	}

	public boolean isMouseOnButton(int x, int y) {
		return x > parent.getX() && x < parent.getX() + parent.getWidth() - (BaseBand.settingsManager.getSettingsByMod(mod) != null ? 14 : 0) && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
	}

	public boolean isMouseOnOptionsButton(int x, int y) {
		return BaseBand.settingsManager.getSettingsByMod(mod) != null && x > parent.getX() + parent.getWidth() - 14 && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
	}
}
