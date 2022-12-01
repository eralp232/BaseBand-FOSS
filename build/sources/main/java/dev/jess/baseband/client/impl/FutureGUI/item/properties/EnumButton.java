package dev.jess.baseband.client.impl.FutureGUI.item.properties;

import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.impl.FutureGUI.RenderMethods;
import dev.jess.baseband.client.impl.FutureGUI.font.FontUtil;
import dev.jess.baseband.client.impl.FutureGUI.item.Button;
import dev.jess.baseband.client.impl.Modules.Client.ColorModule;


public class EnumButton
		extends Button {
	private final Setting property;

	public EnumButton(Setting property) {
		super(property.getName());
		this.property = property;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// RenderMethods.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height, this.getState() ? (!this.isHovering(mouseX, mouseY) ? 2012955202 : -1711586750) : (!this.isHovering(mouseX, mouseY) ? 0x11333333 : -2009910477));
		RenderMethods.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height, this.getState() ? (! this.isHovering(mouseX, mouseY) ? ColorModule.getColor(242).getRGB() : ColorModule.getColor(77).getRGB()) : (! this.isHovering(mouseX, mouseY) ? 0x11333333 : - 2009910477));
//        FontUtil.drawString(String.format("%s\u00a77 %s", this.getLabel(), this.property.getFixedValue()), this.x + 2.3f, this.y - 1.0f, this.getState() ? -1 : -5592406);
		FontUtil.drawString(String.format("%s\u00a77 %s", this.getLabel(), this.property.getValString()), this.x + 2.0f, this.y + 4.0f, this.getState() ? - 1 : - 5592406);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (this.isHovering(mouseX, mouseY)) {
//            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.DeltaGUI"), 1.0f));
			if (mouseButton == 0) {
				property.setValString(property.getOptions().get(getModeIndexForward()));
			} else if (mouseButton == 1) {
				//  property.setValString(property.getOptions().get(getModeIndexBackward()));
			}
		}
	}

	public int getModeIndexBackward() {
		int length = property.getOptions().size();
		if (length - 1 <= 0) {
			return 0;
		} else {
			return length - 1;
		}
	}

	public int getModeIndexForward() {
		for (int i = 0; i < property.getOptions().size(); i++) {
			if (property.getOptions().get(i).equals(property.getValString())) {
				if (i + 1 >= property.getOptions().size()) {
					return 0;
				}
				return i + 1;
			}
		}
		return 0;
	}


	@Override
	public int getHeight() {
		return 14;
	}

	@Override
	public void toggle() {
	}

	@Override
	public boolean getState() {
		return true;
	}
}

