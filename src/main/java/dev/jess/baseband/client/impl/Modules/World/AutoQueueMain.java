package dev.jess.baseband.client.impl.Modules.World;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3i;

import java.awt.*;

public class AutoQueueMain extends Module {

	private final Timer timer = new Timer();
	private final Setting delay = new Setting("Delay", this, 30, 1, 90, true);

	public AutoQueueMain() {
		super("AutoQueueMain", "2b moment", Category.WORLD, new Color(122, 22, 22, 255).getRGB());
	}

	public void onUpdate() {

		// skip if delay not reached
		if (! shouldSendMessage(mc.player)) {
			return;
		}

		// info message

		ChatUtil.sendChatMsg("[AutoQueueMain] Sending message: /queue main");


		// send the message
		mc.player.sendChatMessage("/queue main");

		// reset the timer
		timer.reset();

	}

	private boolean shouldSendMessage(EntityPlayer player) {

		// skip if not in the end
		if (player.dimension != 1) {
			return false;
		}

		// skip if timer not passed
		if (! timer.passed(delay.getValDouble() * 1000)) {
			return false;
		}

		// this is the position the 2b2t queue sets your entity at, ensure we are there
		return player.getPosition().equals(new Vec3i(0, 240, 0));

	}

	public static final class Timer {

		private long time;

		Timer() {
			time = - 1;
		}

		boolean passed(double ms) {
			return System.currentTimeMillis() - time >= ms;
		}

		public void reset() {
			time = System.currentTimeMillis();
		}

	}

}
