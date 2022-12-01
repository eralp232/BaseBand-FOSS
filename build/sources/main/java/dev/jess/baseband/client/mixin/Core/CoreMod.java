package dev.jess.baseband.client.mixin.Core;


import dev.jess.baseband.client.api.Main.BaseBand;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("baseband")
public class CoreMod implements IFMLLoadingPlugin {


	@Override
	public String[] getASMTransformerClass() {
		return new String[]{};

	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		BaseBand.log.debug("Init Mixins");
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.baseband.json");
		MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
		BaseBand.log.debug("Mixins Inited");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
