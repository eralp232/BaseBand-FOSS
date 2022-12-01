package com.baseband.launch;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.loader.launch.LaunchClass;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
		modid = BaseBand.MOD_ID,
		name = BaseBand.MOD_NAME,
		version = BaseBand.VERSION
)
public class LoaderEntryPoint implements LaunchClass {

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		System.out.println("PreInit via Loader");
	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {
		System.out.println("Init via Loader");
		new BaseBand().INSTANCE().init();
	}

	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		System.out.println("PostInit via Loader");
	}
}
