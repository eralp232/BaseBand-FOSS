package dev.jess.baseband.client.api.Main;

import dev.jess.baseband.client.api.Font.CFontRenderer;
import dev.jess.baseband.client.api.Listeners.CommandListener;
import dev.jess.baseband.client.api.Listeners.UpdateListener;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Registry.CommandRegistry;
import dev.jess.baseband.client.api.Registry.ConfigRegistry;
import dev.jess.baseband.client.api.Registry.FriendRegistry;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Settings.SettingsManager;
import dev.jess.baseband.client.api.Utils.BannedUsers;
import dev.jess.baseband.client.api.Utils.CapeManager;
import dev.jess.baseband.client.api.Utils.WebServices;
import dev.jess.baseband.client.api.Utils.Wrapper;
import dev.jess.baseband.client.impl.AzureGUI.click.ClickGUI;
import dev.jess.baseband.client.impl.CatGUI.CatGUI;
import dev.jess.baseband.client.impl.GUI.ClickGui;
import dev.jess.baseband.client.impl.GUI.Frame;
import dev.jess.baseband.client.impl.GUI.PinableFrame;
import dev.jess.baseband.client.impl.MainMenuShaders.Shaders;
import dev.jess.baseband.client.impl.Modules.Misc.AutoReconnect;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import sun.management.VMManagement;
import sun.misc.Unsafe;
import tudbut.tools.Lock;

import javax.naming.Context;
import java.awt.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class BaseBand {


	public static final String MOD_ID = "baseband";
	public static final String MOD_NAME = "BaseBand";
	public static final String REPO = "0x7DB/BaseBand";
	public static final String VERSION = "A1238";
	public static final String hash = "571e98d4099a";
	public static final String CLEAN_VERSION = "vA0.0." + VERSION.substring(1) + "a"; // Needed for WebServices
	public static final Logger log = LogManager.getLogger("BaseBand");
	public static boolean loaded = false;
	public static Shaders shaders;
	public static SettingsManager settingsManager = new SettingsManager();
	public static ModuleRegistry moduleManager;
	public static CommandRegistry commandManager;
	public static CFontRenderer cfr;
	public static ClickGui gui;
	public static CatGUI catGui;
	public static ClickGUI azureGUI;
	public static boolean unload = true;
	public static boolean baritone = true;
	public static boolean dev = false;
	public static volatile boolean wait = true;
	public static BaseBand Instance2 = null;
	static Field field = null;
	private static Unsafe unsafe2 = null;
	//public static Propane4J log = new Propane4J("BaseBand");


	static {
		try {
			String[] naughtyFlags = {
					"-XBootclasspath",
					"-javaagent",
					"-Xdebug",
					"-agentlib",
					"-Xrunjdwp",
					"-Xnoagent",
					"-verbose",
					"-DproxySet",
					"-DproxyHost",
					"-DproxyPort",
					"-Djavax.net.ssl.trustStore",
					"-Djavax.net.ssl.trustStorePassword"
			};
			Field jvmField = ManagementFactory.getRuntimeMXBean().getClass().getDeclaredField("jvm");
			jvmField.setAccessible(true);
			VMManagement jvm = (VMManagement) jvmField.get(ManagementFactory.getRuntimeMXBean());
			List<String> inputArguments = jvm.getVmArguments();
			for (String arg : naughtyFlags) {
				for (String inputArgument : inputArguments) {
					BaseBand.log.debug("Checking JVM Argument: ");
					BaseBand.log.debug(inputArgument);
					if (inputArgument.contains(arg)) {
						BaseBand.log.fatal("Bad JVM Argument!");
						unsafe().putAddress(1, 1);
					}
				}
			}
		} catch (Exception ignored) {
			BaseBand.unsafe().putAddress(1, 1);
		}
		log.info(hash);
		if (dev || ! unload) {
			unsafe().putAddress(1, 1);
		}
	}

	public CapeManager capeManager;
	public File bb;

	public static Unsafe unsafe() { //NO USE THIS TO GET UNSAFE!
		if (unsafe2 == null) {
			try {
				field = Unsafe.class.getDeclaredField("theUnsafe");
				field.setAccessible(true);
				unsafe2 = (Unsafe) field.get(null);
			} catch (Exception e) {
				e.printStackTrace();
				throw new NullPointerException("Unable to set Unsafe Field");
			}
		}
		return unsafe2;
	}

	public static boolean isIngame() {
		return Wrapper.mc.world != null && Wrapper.mc.player != null && loaded;
	}

	public static String getLongestStringArray(ArrayList<String> array) {
		int maxLength = 0;
		String longestString = null;
		for (String s : array) {
			if (s.length() > maxLength) {
				maxLength = s.length();
				longestString = s;
			}
		}
		return longestString;
	}

	public static String getLongestString(String[] array) {
		int maxLength = 0;
		String longestString = null;
		for (String s : array) {
			if (s.length() > maxLength) {
				maxLength = s.length();
				longestString = s;
			}
		}
		return longestString;
	}

	/**
	 * Patches the JNDI context to fix CVE-2021-44228.
	 */
	private static void patchAndHookJndiManager() {
		final ClassLoader ourClassLoader = BaseBand.class.getClassLoader();
		log.info("Loaded in classloader: " + ourClassLoader);
		final List<ClassLoader> classLoaders = new ArrayList<>();
		classLoaders.add(ClassLoader.getSystemClassLoader());
		classLoaders.add(Thread.currentThread().getContextClassLoader());
		classLoaders.add(ourClassLoader);
		boolean fixed = false;
		final List<Throwable> causes = new ArrayList<>();
		for (final ClassLoader classLoader : classLoaders) {
			if (classLoader == null) {
				continue;
			}
			log.info("Trying to patch JNDI context in " + classLoader.getClass().getName());
			try {
				Class<?> clazz;
				try {
					clazz = classLoader.loadClass("org.apache.logging.log4j.core.appender.AbstractManager");
				} catch (final Throwable t) {
					// This should do the same as the above line, but I had random issues with some JVMs that people were using.
					clazz = Class.forName("org.apache.logging.log4j.core.appender.AbstractManager", false, classLoader);
				}
				Class<?> jndiManagerClass = null;
				try {
					try {
						jndiManagerClass = classLoader.loadClass("org.apache.logging.log4j.core.net.JndiManager");
					} catch (final Throwable t) {
						// This should do the same as the above line, but I had random issues with some JVMs that people were using.
						jndiManagerClass = Class.forName("org.apache.logging.log4j.core.net.JndiManager", false, classLoader);
					}
					// Initialize the JNDI manager for classloader and patch the context.
					final Method getDefaultManager = jndiManagerClass.getDeclaredMethod("getDefaultManager");
					final Object defaultManagerInstance = getDefaultManager.invoke(null);
					if (defaultManagerInstance != null && patchJndiContext(defaultManagerInstance)) {
						fixed = true;
					}
					// Alternative hacky fallback patch (we are unable to use the hook method due to classloading issues)
					try {
						final Field field = jndiManagerClass.getDeclaredField("FACTORY");
						stripFinal(field).setAccessible(true);
						field.set(null, null);
					} catch (final Throwable t) {
						t.printStackTrace();
					}
				} catch (final Throwable t) {
					causes.add(t);
				}
				if (clazz != null) {
					// Patch all instances of the JNDI manager with our custom context.
					final Field[] fields = clazz.getDeclaredFields();
					for (final Field field : fields) {
						if (Modifier.isStatic(field.getModifiers()) && Map.class.isAssignableFrom(field.getType())) {
							stripFinal(field).setAccessible(true);
							final Map<?, ?> map = (Map<?, ?>) field.get(null);
							if (map == null) {
								continue;
							}
							for (final Object value : map.values()) {
								if ((jndiManagerClass != null && jndiManagerClass.isAssignableFrom(value.getClass()))) {
									try {
										if (patchJndiContext(value)) {
											fixed = true;
										}
									} catch (final Throwable t) {
										causes.add(t);
									}
								}
							}
							map.clear();
						}
					}
				}
			} catch (final Throwable t) {
				t.printStackTrace();
			}
		}
		if (! fixed) {
			final VerifyError error = new VerifyError("Failed to patch JNDI context to fix CVE-2021-44228");
			for (final Throwable cause : causes) {
				error.addSuppressed(cause);
			}
			throw error;
		}
	}

	/**
	 * Patch the JNDI context to use our custom context.
	 *
	 * @return true if the context was patched, false otherwise.
	 */
	private static boolean patchJndiContext(final Object jndiManager) throws ReflectiveOperationException {
		boolean fixed = false;
		Class<?> currClass = jndiManager.getClass();
		while (currClass != null) {
			final Field[] fields = currClass.getDeclaredFields();
			for (final Field field : fields) {
				if (Context.class.isAssignableFrom(field.getType())) {
					stripFinal(field).setAccessible(true);
					field.set(jndiManager, null);
					fixed = true;
				}
			}
			currClass = currClass.getSuperclass();
		}
		return fixed;
	}

	/**
	 * Strip the final modifier from a field.
	 *
	 * @param field the field to strip.
	 * @return the field with the final modifier stripped.
	 */
	public static Field stripFinal(final Field field) throws ReflectiveOperationException {
		final Field modifiersField = Field.class.getDeclaredField("modifiers");
		final boolean modifiersAccessible = modifiersField.isAccessible();
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~ Modifier.FINAL);
		modifiersField.setAccessible(modifiersAccessible);
		return field;
	}

	public BaseBand INSTANCE() {
		if (Instance2 == null) {
			Instance2 = this;
		}
		return Instance2;
	}


	public void init() {
		BannedUsers.check(Minecraft.getMinecraft().getSession().getProfile().getId().toString());
		//UIDManager.UIDCheck();

		if (BaseBand.dev || !BaseBand.unload) {
			unsafe().putAddress(1, 1);
		}


		//set StartTime,
		long startTime = System.nanoTime() / 1000000L;






		if (!unload) {
			log.info("API-Data Recieved!");
			Display.setTitle("BaseBand "+VERSION+" git-"+hash);
		} else {
			log.error("Unlicensed Copy of BaseBand!");
			log.info("But i don't care! (access locally set to true)");
			unload=false;
		}


		if (dev) {
			log.warn("Dev Mode Enabled!");
		}else{
			dev=true;
		}


		try {
			patchAndHookJndiManager();
		} catch (final Throwable t) {
			log.error("Failed to patch JNDI context to fix CVE-2021-44228!\nWe were unable to patch the game to fix CVE-2021-44228.\nYour current game session is highly insecure.");
			log.info("If you are running Forge 14.23.5.2859 or later, You're probably fine.");
		}





		//Listen for Commands and Updates Events.


		try {
			Class.forName("baritone.a");
		} catch (Exception e) {
			baritone = false;
			log.info("Baritone Not Found!");
		}

		if (System.getProperty("com.bb.disableBaritone") != null && baritone) {
			if (System.getProperty("com.bb.disableBaritone").equalsIgnoreCase("true")) {
				log.info("Baritone Integration Disabled via Java Argument \"-Dcom.bb.disableBaritone=true\"");
				baritone = false;
			}
		}

		if (baritone) {
			log.info("Baritone Detected.");
		}


		if (! unload) {
			MinecraftForge.EVENT_BUS.register(new CommandListener());
			MinecraftForge.EVENT_BUS.register(this);
		}


		moduleManager = new ModuleRegistry();

		//^ Modules are now *also* registered on Instance Set.

		//Commands
		commandManager = new CommandRegistry();
		//^ Commands are now registered on Instance Set.
		// CommandRegistry.registerCommands() (This is a private field.)
		//Well not private, but not explicitly public.


		MinecraftForge.EVENT_BUS.register(new UpdateListener());

		//^ this has update stuff that needs ModuleRegistry to be set /shrug


		//We need this
		new File(Wrapper.mc.gameDir + File.separator + "BaseBand" + File.separator + "notebot").mkdir();


		//Yum.
		shaders = new Shaders();


		//soon to be integrated into the API
		this.capeManager = new CapeManager();
		this.capeManager.initializeCapes();


		initFileRhack();


		gui = new ClickGui();

		gui.initGui();

		azureGUI = new ClickGUI();

		catGui = new CatGUI();


		try {
			new FriendRegistry().load();
		} catch (Exception e) {
			try {
				FriendRegistry.friends.add("1");
				new FriendRegistry().save();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
				log.error("FileNotFound: Friend List");
			}
		}

		try {
			loadGuiPos();
		} catch (Exception file) {
			saveGuiPos();
		}
		Wrapper.mc.gameSettings.autoJump = false;

		//^ Fuck AutoJump, Disable it on StartUp.


		try {
			ConfigRegistry.load();
		} catch (Exception e) {
			e.printStackTrace();
		}


		cfr = new CFontRenderer(new Font("Verdana", Font.PLAIN, 18), true, true);


		log.info(String.format("Completed Client Startup. (Took %s milliseconds.)", System.nanoTime() / 1000000L - startTime));

	}


	public void initFileRhack() {
		bb = new File(Wrapper.mc.gameDir + File.separator + "BaseBand");
		if (! bb.exists()) {
			bb.mkdirs();
		}
	}

	public void saveGuiPos() {
		//Slider
		try {
			File file = new File(bb.getAbsolutePath(), "Gui.txt");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for (Frame i : ClickGui.frames) {

				out.write(i.category.name() + "=" + i.x + "=" + i.y + "=" + i.open + "\r\n");

			}
			for (PinableFrame i : ClickGui.pinableFrames) {
				out.write(i.title + "=" + i.x + "=" + i.y + "=" + i.open + "\r\n");
			}
			out.close();
		} catch (Exception ignored) {
		}
	}


	public void loadGuiPos() {


		try {
			File file = new File(bb.getAbsolutePath(), "Gui.txt");
			FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				String curLine = line.trim();
				String name = curLine.split("=")[0];
				String x = (curLine.split("=")[1]);
				String y = (curLine.split("=")[2]);
				String exteneded = (curLine.split("=")[3]);
				for (Frame i : ClickGui.frames) {
					if (i.category.name().equals(name)) {
						i.x = Integer.parseInt(x);
						i.y = Integer.parseInt(y);
						i.open = Boolean.parseBoolean(exteneded);

					}

				}
				for (PinableFrame i : ClickGui.pinableFrames) {
					if (i.title.equals(name)) {
						i.x = Integer.parseInt(x);
						i.y = Integer.parseInt(y);
						i.open = Boolean.parseBoolean(exteneded);
						i.setPinned(Boolean.parseBoolean(exteneded));

					}

				}
			}
			br.close();
		} catch (Exception e) {
			log.info("Unable to load Gui Config, Creating Config.");
			//e.printStackTrace();
			saveGuiPos();
		}


	}


	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent e) {
		loaded = true;
	}

	@SubscribeEvent
	public void connect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		AutoReconnect.serverData = Minecraft.getMinecraft().currentServerData;
		for (Module module : ModuleRegistry.MODULES) {
			if (module.getName().equalsIgnoreCase("NoteBot") || module.getName().equalsIgnoreCase("Phase") || module.getName().equalsIgnoreCase("IRC")) {
				module.setToggled(false);
				return;
			}
			try {
				loaded = false;
				if (module.isToggled()) {
					module.enable();
				} else {
					module.disable();
				}
				loaded = true;
			} catch (Exception e) {
				//Screwy workaround. should work though.
			}
		}
	}

	@SubscribeEvent
	public void disconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		AutoReconnect.serverData = Minecraft.getMinecraft().currentServerData;
		ConfigRegistry.save();
		saveGuiPos();
	}

	@SubscribeEvent
	public void key(InputEvent.KeyInputEvent e) {
		if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null)
			return;
		try {
			if (Keyboard.isCreated()) {
				if (Keyboard.getEventKeyState()) {
					int keyCode = Keyboard.getEventKey();
					if (keyCode <= 0)
						return;
					for (Module m : ModuleRegistry.getAll()) {
						if (m.getKey() == keyCode && keyCode > 0) {
							m.setToggled(! m.isToggled());
						}
					}
				}
			}
		} catch (Exception q) {
			q.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent e) {
		for (Module module : ModuleRegistry.MODULES) {
			if (module.isToggled()) {
				if (! ModuleRegistry.toggledModules.contains(module)) {
					ModuleRegistry.toggledModules.add(module);
				}
			} else {
				ModuleRegistry.toggledModules.remove(module);
			}
		}
	}


}

