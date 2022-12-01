package dev.jess.baseband.client.impl.GUI;

import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.RenderingMethods;
import dev.jess.baseband.client.api.Utils.Wrapper;

import java.awt.*;

public class FeatureList extends PinableFrame {

	public FeatureList() {
		super("Feature List", new String[]{}, 50);
	}

	public void renderFrameText() {

		if (this.open && ModuleRegistry.toggledModules.size() >= 1) {

			RenderingMethods.drawBorderedRectReliantGui(this.x, this.y + this.barHeight + 1, this.x + this.getWidth(), this.y + this.barHeight + (10 * ModuleRegistry.toggledModules.size()) + 1, 1, new Color(25, 25, 25, 128).getRGB(), Color.gray.getRGB());


			int yCount = this.y + this.barHeight + 3;
			for (Module module : ModuleRegistry.toggledModules) {
				if (module.isToggled()) {
					Wrapper.fr.drawString(module.getName() + " " + module.getMcHudMeta(), this.x + 3, yCount, - 1);
					yCount += 10;
				}
			}

		}

	}

}
