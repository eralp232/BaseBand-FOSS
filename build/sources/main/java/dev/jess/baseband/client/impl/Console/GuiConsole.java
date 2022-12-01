package dev.jess.baseband.client.impl.Console;

import dev.jess.baseband.client.api.Command.Command;
import dev.jess.baseband.client.api.Registry.CommandRegistry;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class GuiConsole extends GuiScreen {

	public static ArrayList console = new ArrayList();
	private final GuiConsoleTextField textField = new GuiConsoleTextField(0, Wrapper.fr, 2, 2, new ScaledResolution(Wrapper.mc).getScaledWidth() - 4, 10);

	public GuiConsole() {
		textField.setFocused(true);
		textField.setCanLoseFocus(false);
	}

	public static void addConsoleMessage(String s) {
		GuiConsole.console.add(0, s);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			this.mc.displayGuiScreen(null);
		} else if (keyCode != 28 && keyCode != 156) {

			this.textField.textboxKeyTyped(typedChar, keyCode);

		} else {
			String s = this.textField.getText().trim();

			if (! s.isEmpty()) {


				String messageWithoutPrefix = s;
				String[] cmdAndArgs = messageWithoutPrefix.split(" +");
				String command = cmdAndArgs[0].toLowerCase();
				String[] args = Arrays.copyOfRange(cmdAndArgs, 1, cmdAndArgs.length);
				Command toExecute = CommandRegistry.getInstance().getByAlias(command);
				if (toExecute == null) {
					ChatUtil.sendChatMsg("Command Not Found.");
				} else toExecute.execute(args);

			}

			this.textField.setText("");
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution res = new ScaledResolution(Wrapper.mc);
		super.drawScreen(mouseX, mouseY, partialTicks);
		textField.drawTextBox();
	}

}
