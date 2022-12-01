package dev.jess.baseband.client.impl.FutureGUI.item.properties;

import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.impl.FutureGUI.RenderMethods;
import dev.jess.baseband.client.impl.FutureGUI.font.FontUtil;
import dev.jess.baseband.client.impl.FutureGUI.item.Button;
import dev.jess.baseband.client.impl.Modules.Client.ColorModule;

public class BooleanButton extends Button {
	private final Setting property;

	public BooleanButton(Setting property) {
		super(property.getName());
		//super(property.getAliases()[0]);
		this.property = property;
		this.width = 15;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//RenderMethods.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height, this.getState() ? (!this.isHovering(mouseX, mouseY) ? 2012955202 : -1711586750) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
		RenderMethods.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height, this.getState() ? (! this.isHovering(mouseX, mouseY) ? ColorModule.getColor(242).getRGB() : ColorModule.getColor(77).getRGB()) : (! this.isHovering(mouseX, mouseY) ? 0x11555555 : - 2007673515));
		FontUtil.drawString(this.getLabel(), this.x + 2.0f, this.y + 4.0f, this.getState() ? - 1 : - 5592406);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (this.isHovering(mouseX, mouseY)) {
//            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.DeltaGUI"), 1.0f));
		}
	}

	@Override
	public int getHeight() {
		return 14;
	}

	@Override
	public void toggle() {
		property.setValBoolean(! property.getValBoolean());
	}

	@Override
	public boolean getState() {
		return property.getValBoolean();
	}
}

