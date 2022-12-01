package dev.jess.baseband.client.impl.Console;

import dev.jess.baseband.client.api.Utils.RenderingMethods;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

import java.awt.*;


public class GuiConsoleTextField extends GuiTextField {


	public GuiConsoleTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int width, int height) {
		super(componentId, fontrendererObj, x, y, width, height);
	}

	public void drawTextBox() {
		RenderingMethods.drawBorderedRectReliantGui(x, y, x + width, y + height, 1, new Color(169, 169, 169).getRGB(), Color.white.getRGB());

		Wrapper.fr.drawStringWithShadow(this.getText(), x + 1, y + 1, - 1);

	}
}
