package dev.jess.baseband.client.impl.Modules.Render;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;

public class CameraClip extends Module {
	public CameraClip() {
		super("CameraClip", "KAMII", Category.RENDER, new Color(171, 94, 160, 255).getRGB());
	}
}
