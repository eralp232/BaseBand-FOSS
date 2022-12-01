package dev.jess.baseband.client.api.Registry;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.impl.Modules.Client.*;
import dev.jess.baseband.client.impl.Modules.Combat.*;
import dev.jess.baseband.client.impl.Modules.Exploits.*;
import dev.jess.baseband.client.impl.Modules.Misc.*;
import dev.jess.baseband.client.impl.Modules.Movement.*;
import dev.jess.baseband.client.impl.Modules.Render.*;
import dev.jess.baseband.client.impl.Modules.World.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleRegistry {
	public static ModuleRegistry INSTANCE = null;
	public static List<Module> MODULES = new ArrayList<>();
	public static ArrayList<Module> toggledModules = new ArrayList<>();

	public ModuleRegistry() {
		if (INSTANCE != null) {
			throw new IllegalStateException("ModuleRegistry Attempted Instance Set Twice!");
		}
		INSTANCE = this;
		registerModules();
	}

	public static ArrayList<Module> getModulesInCategory(Category c) {
		ArrayList<Module> mods = new ArrayList<>();
		for (Module m : MODULES) {
			if (m.getCategory() == c) {
				mods.add(m);
			}
		}
		return mods;
	}

	public static ModuleRegistry getInstance() {
		if (INSTANCE == null) new ModuleRegistry();
		return INSTANCE;
	}

	public static List<Module> getAll() {
		return MODULES;
	}

	public static Module getModule(String name) {
		for (Module m : MODULES) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}

	static void registerModules() {
		//Time to sort by Categories..............................
		//PAAAAAAIN
		//Movement
		MODULES.add(new AntiTrip());
		MODULES.add(new AntiVelocity());
		MODULES.add(new BoatFly());
		MODULES.add(new CreativeFly());
		MODULES.add(new DownStep());
		MODULES.add(new ElytraFlight());
		MODULES.add(new EntitySpeed());
		MODULES.add(new LevitationControl());
		MODULES.add(new LongJump());
		MODULES.add(new NoPush());
		MODULES.add(new NoUseSlow());
		MODULES.add(new Speed());

		//Exploits
		MODULES.add(new AntiGUIClose());
		MODULES.add(new AntiHunger());
		MODULES.add(new AntiLog4Shell());
		MODULES.add(new AntiNoForge());
		MODULES.add(new ChestAura());
		MODULES.add(new ColourSign());
		MODULES.add(new CornerClip());
		MODULES.add(new FastProjectile());
		MODULES.add(new LimitInteract());
		if (BaseBand.dev) {
			MODULES.add(new LiveOverFlowDisabler());
			BaseBand.log.warn("Registered LiveOverFlow Server Disabler");
		}
		MODULES.add(new NocomMaster());
		MODULES.add(new NocomSpiral());
		MODULES.add(new NoRotate());
		MODULES.add(new PacketFly());
		MODULES.add(new PacketPosCrasher());
		MODULES.add(new Phase());
		MODULES.add(new PortalGodmode());
		MODULES.add(new Randomizer());
		MODULES.add(new ThunderHack());
		MODULES.add(new TickShift());
		MODULES.add(new TraceTeleport());
		MODULES.add(new XCarry());

		//Misc
		MODULES.add(new AutoMount());
		MODULES.add(new AutoReconnect());
		MODULES.add(new AutoRespawn());
		if (BaseBand.baritone) {
			MODULES.add(new BaritoneClick());
		}
		MODULES.add(new BaseBandSign());
		MODULES.add(new BurrowCounter());
		MODULES.add(new UnixTimestamp());


		//Render
		MODULES.add(new ArmourHUD());
		if (BaseBand.baritone) {
			MODULES.add(new BaritoneTask());
		}
		MODULES.add(new Brightness());
		MODULES.add(new CameraClip());
		MODULES.add(new CPS());
		MODULES.add(new CrystalRender());
		MODULES.add(new FPS());
		MODULES.add(new IP());
		MODULES.add(new LowOffhand());
		MODULES.add(new Motion());
		MODULES.add(new NameTags());
		MODULES.add(new NoBlock());
		MODULES.add(new NoFire());
		MODULES.add(new NoHurtCam());
		MODULES.add(new NoPotionIcon());
		MODULES.add(new NoUnicode());
		MODULES.add(new PacketRender());
		MODULES.add(new Ping());
		MODULES.add(new PPS());
		MODULES.add(new ServerBrand());
		MODULES.add(new TotemPopChams());
		MODULES.add(new TPS());
		MODULES.add(new UPS());


		//Client
		MODULES.add(new AzureGUI());
		MODULES.add(new Capes());
		MODULES.add(new CatGUI());
		MODULES.add(new ClickGUI());
		MODULES.add(new Colorr());
		MODULES.add(new Console());
		MODULES.add(new ExeterGUI());
		MODULES.add(new FriendSystem());
		MODULES.add(new HUD());
		MODULES.add(new MainMenuShader());
		MODULES.add(new Management());


		//Combat
		MODULES.add(new Anchor());
		MODULES.add(new ArmourEquip());
		MODULES.add(new AutoCrystal());
		MODULES.add(new AutoHit());
		MODULES.add(new AutoObsidian());
		MODULES.add(new AutoTotem());
		MODULES.add(new AutoTrap());
		MODULES.add(new AutoWeb());
		MODULES.add(new AutoXP());
		MODULES.add(new BowAim());
		MODULES.add(new BowSpam());
		MODULES.add(new Criticals());
		MODULES.add(new HoleFill());
		MODULES.add(new HotbarRefill());
		MODULES.add(new PopLag());
		MODULES.add(new StackedTotem());
		MODULES.add(new TriggerBot());


		//World
		MODULES.add(new AntiAFK());
		MODULES.add(new AutoQueueMain());
		MODULES.add(new AutoTool());
		MODULES.add(new ChatCrypt());
		MODULES.add(new ChatSuffix());
		MODULES.add(new ChatTimeStamps());
		MODULES.add(new ChestSlotSwap());
		MODULES.add(new ClearChat());
		MODULES.add(new HashChat());
		MODULES.add(new InfiniteChat());
		MODULES.add(new NoteBot());
		MODULES.add(new PacketMine());
		MODULES.add(new Scaffold());
		MODULES.add(new Spammer());
		MODULES.add(new TotemPopCounter());
		MODULES.add(new VisualRange());

	}

	public List<Module> getModuleList() {
		return MODULES;
	}

	public Module getByName(String name) {
		for (Module module : MODULES) {
			if (module.getName().equalsIgnoreCase(name)) return module;
		}
		return null;
	}
}
