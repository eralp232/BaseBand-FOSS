package dev.jess.baseband.client.impl.GUI;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;


public class ClickGui extends GuiScreen {

	public static ArrayList<Frame> frames;
	public static ArrayList<PinableFrame> pinableFrames;

	public ClickGui() {
		frames = new ArrayList<Frame>();
		int frameY = 25;
		for (Category category : Category.values()) {
			Frame frame = new Frame(category);
			frame.setY(frameY);
			frames.add(frame);
			frameY += 13 + 15;
		}

		pinableFrames = new ArrayList<PinableFrame>();


		pinableFrames.add(new FeatureList());
		pinableFrames.add(new PositionTab());
		pinableFrames.add(new EntityListTab());
		pinableFrames.add(new HungerStatsTab());
		pinableFrames.add(new ServerInfo());

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui() {
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for (Frame frame : frames) {
			frame.renderFrame(this.fontRenderer);
			frame.updatePosition(mouseX, mouseY);

			for (Component comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}

		for (PinableFrame pinableFrame : pinableFrames) {
			pinableFrame.renderFrame();
			pinableFrame.renderFrameText();
			pinableFrame.updatePosition(mouseX, mouseY);
		}

	}

	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		for (Frame frame : frames) {

			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if (frame.isWithinExtendRange(mouseX, mouseY) && (mouseButton == 1 || mouseButton == 0)) {
				frame.setOpen(! frame.isOpen());
			}
			if (frame.isOpen()) {
				if (! frame.getComponents().isEmpty()) {
					for (Component component : frame.getComponents()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}

		for (PinableFrame pinableFrame : pinableFrames) {
			if (pinableFrame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				pinableFrame.setDrag(true);
				pinableFrame.dragX = mouseX - pinableFrame.getX();
				pinableFrame.dragY = mouseY - pinableFrame.getY();
			}

			if (pinableFrame.isWithinExtendRange(mouseX, mouseY) && (mouseButton == 1 || mouseButton == 0)) {
				pinableFrame.setOpen(! pinableFrame.isOpen());
				pinableFrame.setWidth(88);
			}

			if (pinableFrame.isWithinPinRange(mouseX, mouseY) && (mouseButton == 1 || mouseButton == 0)) {
				pinableFrame.setPinned(! pinableFrame.isPinned());
			}
		}


	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for (Frame frame : frames) {
			if (frame.isOpen() && keyCode != 1) {
				if (! frame.getComponents().isEmpty()) {
					for (Component component : frame.getComponents()) {
						component.keyTyped(typedChar, keyCode);
					}
				}
			}
		}
		if (keyCode == 1) {
			this.mc.displayGuiScreen(null);
		}
	}


	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		for (Frame frame : frames) {
			frame.setDrag(false);
			if (frame.isOpen()) {
				if (! frame.getComponents().isEmpty()) {
					for (Component component : frame.getComponents()) {
						component.mouseReleased(mouseX, mouseY, state);
					}
				}
			}
		}

		for (PinableFrame pinableFrame : pinableFrames) {
			pinableFrame.setDrag(false);
		}
	}

	public void onGuiClosed() {
		new BaseBand().saveGuiPos();
	}

	public void handleMouseInput() throws IOException {
		int scrollAmount = 10;
		if (Mouse.getEventDWheel() > 0) {
			for (Frame frame : frames) {
				frame.y += scrollAmount;
			}
			for (PinableFrame pinableFrame : pinableFrames) {
				pinableFrame.y += scrollAmount;
			}
		}
		if (Mouse.getEventDWheel() < 0) {
			for (Frame frame : frames) {
				frame.y -= scrollAmount;
			}
			for (PinableFrame pinableFrame : pinableFrames) {
				pinableFrame.y -= scrollAmount;
			}
		}

		super.handleMouseInput();
	}

}
