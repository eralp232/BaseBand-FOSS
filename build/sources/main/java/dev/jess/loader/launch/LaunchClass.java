package dev.jess.loader.launch;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface LaunchClass {
	void onPreInit(final FMLPreInitializationEvent p0);

	void onInit(final FMLInitializationEvent p0);

	void onPostInit(final FMLPostInitializationEvent p0);
}
