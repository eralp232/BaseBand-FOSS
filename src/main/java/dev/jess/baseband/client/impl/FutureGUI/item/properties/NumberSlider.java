package dev.jess.baseband.client.impl.FutureGUI.item.properties;

import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.impl.FutureGUI.ClickGui;
import dev.jess.baseband.client.impl.FutureGUI.Panel;
import dev.jess.baseband.client.impl.FutureGUI.RenderMethods;
import dev.jess.baseband.client.impl.FutureGUI.font.FontUtil;
import dev.jess.baseband.client.impl.FutureGUI.item.Item;
import dev.jess.baseband.client.impl.Modules.Client.ColorModule;
import org.lwjgl.input.Mouse;

public class NumberSlider
		extends Item {
	private final Setting setting;
	private final Number min;
	private final Number max;
	private final int difference;

	public NumberSlider(Setting setting) {
		super(setting.getName());
		this.setting = setting;
		this.min = setting.getMin();
		this.max = setting.getMax();
		this.difference = max.intValue() - min.intValue();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		dragSetting(mouseX, mouseY);
//        RenderMethods.drawRect(x, y, ((Number)numberProperty.getValue()).floatValue() <= min.floatValue() ? x : x + (width + 7.4F) * partialMultiplier(), y + height - 0.5f, !isHovering(mouseX, mouseY) ? 2012955202 : -1711586750);
		RenderMethods.drawRect(x, y, ((Number) setting.getValDouble()).floatValue() <= min.floatValue() ? x : x + (width + 7.4F) * partialMultiplier(), y + height, ! isHovering(mouseX, mouseY) ? ColorModule.getColor(242).getRGB() : ColorModule.getColor(77).getRGB());
		//RenderMethods.drawRect(x, y, ((Number)setting.getValDouble()).floatValue() <= min.floatValue() ? x : x + (width + 7.4F) * partialMultiplier(), y + height - 0.5f, !isHovering(mouseX, mouseY) ? 2012955202 : -1711586750);
		//RenderMethods.drawRect(x, y, x + getValueWidth(), y + height, ! isHovering(mouseX, mouseY) ? 2012955202 : -1711586750);//2002577475 : -1721964477);
//        FontUtil.drawString(String.format("%s\u00a77 %s", this.getLabel(), this.numberProperty.getValue()), this.x + 2.3f, this.y - 1.0f, -1);
		FontUtil.drawString(String.format("%s\u00a77 %s", this.getLabel(), this.setting.getValDouble()), this.x + 2.0f, this.y + 4.0f, - 1);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (isHovering(mouseX, mouseY) && mouseButton == 0) {
			setSettingFromX(mouseX);
		}
	}

	private void setSettingFromX(int mouseX) {
		float percent = (mouseX - x) / (width + 7.4F);
		//if (!numberProperty.onlyInt()) {
		//    double result = (Double) numberProperty.getMin() + (difference * percent);
		//    numberProperty.setValDouble(Math.round(10.0 * result) / 10.0);
		//}else if(numberProperty.onlyInt()){
		setting.setValDouble((setting.getMin() + (int) (difference * percent)));
		//}

	}


	@Override
	public int getHeight() {
		return 14;
	}

	private void dragSetting(int mouseX, int mouseY) {
		if (isHovering(mouseX, mouseY) && Mouse.isButtonDown(0)) {
			setSettingFromX(mouseX);
		}
	}


	private boolean isHovering(int mouseX, int mouseY) {
		for (Panel panel : ClickGui.getClickGui().getPanels()) {
			if (! panel.drag) continue;
			return false;
		}
		return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() && (float) mouseY >= this.getY() && (float) mouseY <= this.getY() + (float) this.height;
	}

	private float getValueWidth() {
		return ((Number) this.setting.getMax()).floatValue() - ((Number) this.setting.getMin()).floatValue() + ((Number) this.setting.getValDouble()).floatValue();
	}

	private float middle() {
		return max.floatValue() - min.floatValue();
	}

	private float part() {
		return ((Number) setting.getValDouble()).floatValue() - min.floatValue();
	}

	private float partialMultiplier() {
		return part() / middle();
	}
}

