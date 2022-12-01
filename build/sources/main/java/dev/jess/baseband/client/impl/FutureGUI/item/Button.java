package dev.jess.baseband.client.impl.FutureGUI.item;


import dev.jess.baseband.client.impl.FutureGUI.ClickGui;
import dev.jess.baseband.client.impl.FutureGUI.Panel;
import dev.jess.baseband.client.impl.FutureGUI.RenderMethods;
import dev.jess.baseband.client.impl.FutureGUI.font.FontUtil;
import dev.jess.baseband.client.impl.Modules.Client.ColorModule;

public class Button
		extends Item {
	private boolean state;

	public Button(String label) {
		super(label);
		this.height = 15;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//  The original exeter
		//RenderMethods.drawGradientRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height, this.getState() ? (!this.isHovering(mouseX, mouseY) ? 2012955202 : 1442529858) : (!this.isHovering(mouseX, mouseY) ? 0x33555555 : -2007673515), this.getState() ? (!this.isHovering(mouseX, mouseY) ? -1426374078 : -1711586750) : (!this.isHovering(mouseX, mouseY) ? 0x55555555 : -1722460843));

		// Future
		//RenderMethods.drawGradientRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height, this.getState() ? (!this.isHovering(mouseX, mouseY) ? 0x77FB4242 : 0x55FB4242) : (!this.isHovering(mouseX, mouseY) ? 0x33555555 : 0x77AAAAAB), this.getState() ? (!this.isHovering(mouseX, mouseY) ? 0x77FB4242 : 0x55FB4242) : (!this.isHovering(mouseX, mouseY) ? 0x55555555 : 0x66AAAAAB));
		RenderMethods.drawGradientRect(this.x, this.y, this.x + (float) this.width, this.y + (float) this.height, this.getState() ? (! this.isHovering(mouseX, mouseY) ? ColorModule.getColor(242).getRGB() : ColorModule.getColor(55).getRGB()) : (! this.isHovering(mouseX, mouseY) ? 0x33555555 : 0x77AAAAAB), this.getState() ? (! this.isHovering(mouseX, mouseY) ? ColorModule.getColor(242).getRGB() : ColorModule.getColor(77).getRGB()) : (! this.isHovering(mouseX, mouseY) ? 0x55555555 : 0x66AAAAAB));


		FontUtil.drawString(this.getLabel(), this.x + 2.0f, this.y + 4.0f, this.getState() ? - 1 : - 5592406);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
			this.state = ! this.state;
			this.toggle();
//            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.DeltaGUI"), 1.0f));
		}
	}

	public void toggle() {
	}

	public boolean getState() {
		return this.state;
	}

	@Override
	public int getHeight() {
		return 14;
	}

	protected boolean isHovering(int mouseX, int mouseY) {
		for (Panel panel : ClickGui.getClickGui().getPanels()) {
			if (! panel.drag) continue;
			return false;
		}
		return (float) mouseX >= this.getX() && (float) mouseX <= this.getX() + (float) this.getWidth() && (float) mouseY >= this.getY() && (float) mouseY <= this.getY() + (float) this.height;
	}
}

