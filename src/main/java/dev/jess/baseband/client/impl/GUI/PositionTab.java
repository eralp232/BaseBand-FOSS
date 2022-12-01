package dev.jess.baseband.client.impl.GUI;

import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.util.math.MathHelper;

import java.text.DecimalFormat;


public class PositionTab extends PinableFrame {

	public DecimalFormat coords = new DecimalFormat("#");
	public DecimalFormat rotation = new DecimalFormat("#.##");

	public PositionTab() {
		super("Position", new String[]{}, 75);
	}

	public void renderFrame() {
		this.text = new String[]{"X: " + coords.format(Wrapper.mc.player.posX) + (Wrapper.mc.player.dimension == - 1 ? "(" + coords.format(Wrapper.mc.player.posX * 8) + ")" : ""), "Y: " + coords.format(Wrapper.mc.player.posY), "Z: " + coords.format(Wrapper.mc.player.posZ) + (Wrapper.mc.player.dimension == - 1 ? "(" + coords.format(Wrapper.mc.player.posZ * 8) + ")" : ""), "Yaw: " + rotation.format(MathHelper.wrapDegrees(Wrapper.mc.player.rotationYaw)), "Pitch: " + rotation.format(MathHelper.wrapDegrees(Wrapper.mc.player.rotationPitch))};
		super.renderFrame();
	}


}

