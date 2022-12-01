package dev.jess.baseband.client.impl.GUI;

import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.RenderingMethods;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.client.gui.Gui;

import java.awt.*;


public class CheckBox extends Component {

	private final Setting op;
	private final dev.jess.baseband.client.impl.GUI.Button parent;
	private boolean hovered;
	private int offset;
	private int x;
	private int y;

	public CheckBox(Setting option, Button button, int offset) {
		this.op = option;
		this.parent = button;
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX() + parent.parent.getWidth() + 5;
		this.offset = offset;
	}

	@Override
	public void renderComponent() {

		int umodR = 0;
		int umodG = 130;
		int umodB = 130;

		RenderingMethods.drawBorderedRectReliantGui(x, parent.parent.getY() + offset - 0.5, x + parent.parent.getWidth() - 11.5, parent.parent.getY() + offset + 12, 1, new Color(25, 25, 25, 128).getRGB(), Color.gray.getRGB());

		Gui.drawRect(x, parent.parent.getY() + offset, x + parent.parent.getWidth() - 12, parent.parent.getY() + offset + 12, new Color(umodR, umodG, umodB).getRGB());


		Wrapper.fr.drawStringWithShadow(this.op.getName(), x + 13, (parent.parent.getY() + offset + 2), - 1);


		RenderingMethods.drawBorderedRectReliantGui(x + 2, parent.parent.getY() + offset - - 1, x + 12, parent.parent.getY() + offset + 10.5, 1, new Color(25, 25, 25, 128).getRGB(), Color.gray.getRGB());
		//Gui.drawRect(x, parent.parent.getY() + offset, x + parent.parent.getWidth() - 12, parent.parent.getY() + offset + 12, new Colorr(0, 130, 130).getRGB());


		if (! this.op.getValBoolean()) {
			RenderingMethods.fakeGuiRect(x + 2, parent.parent.getY() + offset - - 1.5, x + 11.5, parent.parent.getY() + offset + 10.5, new Color(umodR, umodG, umodB).getRGB());
		}

	}

	@Override
	public void setOff(int newOff) {
		offset = newOff;
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
			this.op.setValBoolean(! op.getValBoolean());
			//Wrapper.getFileManager().saveSettingsList();
		}
	}

	public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12 + 1;
	}
}
