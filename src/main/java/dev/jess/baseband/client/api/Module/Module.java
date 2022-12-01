package dev.jess.baseband.client.api.Module;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public abstract class Module {
	protected static Minecraft mc = Minecraft.getMinecraft();
	private final Category category;
	public boolean visible = true;
	public Integer clickGuiX;
	public Integer clickGuiY;
	boolean toggled = false;
	String name;
	String description;
	String hudmeta = "";
	int Color;
	private int key;

	public Module(String name, String description, Category category, Integer Color) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.Color = Color;
	}

	public void enable() {
		if (! ModuleRegistry.toggledModules.contains(this)) {
			ModuleRegistry.toggledModules.add(this);
		}
	}

	public void disable() {
		ModuleRegistry.toggledModules.remove(this);
		setMcHudMeta("");
	}

	public void tick() {
	}

	public void renderWorld() {
	}

	public void renderOverlay() {
	}

	public void renderText() {
	}

	public void updatePlayerMotion(int stage) {

	}


	public String getMcHudMeta() {
		return hudmeta;
	}

	public void setMcHudMeta(String meta) {
		hudmeta = meta;
	}

	public String getDescription() {
		return description;
	}

	public Enum<Category> getCategory() {
		return category;
	}

	public int getColor() {
		return Color;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public boolean isToggled() {
		return toggled;
	}


	public void setToggled(boolean toggled) {
		if (mc.player != null && mc.world != null) {
			if (toggled) enable();
			else disable();

			if (toggled) {
				if (! ModuleRegistry.toggledModules.contains(this)) {
					ModuleRegistry.toggledModules.add(this);
				}
			} else {
				ModuleRegistry.toggledModules.remove(this);
			}
		}
		this.toggled = toggled;
		if (BaseBand.loaded) {
			ChatUtil.sendChatMsg(this.name + " <- " + this.toggled);
		}
	}

	public boolean onPacketSend(Packet<?> packet) {
		return false;
	}

	public boolean onPacketReceive(Packet<?> packet) {
		return false;
	}

}
