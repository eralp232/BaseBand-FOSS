package dev.jess.baseband.client.api.Registry;

import com.google.gson.*;
import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigRegistry {
	private static final String fileName = "BaseBand/";
	private static final String moduleName = "Modules/";
	private static final String mainName = "Main/";
	private static final String miscName = "Misc/";

	public static void load() {
		try {
			loadModules();
			loadEnabledModules();
			loadModuleKeybinds();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadModules() throws IOException {
		String moduleLocation = fileName + moduleName;

		for (Module module : BaseBand.moduleManager.getModuleList()) {
			boolean settings;

			try {
				if (BaseBand.settingsManager.getSettingsByMod(module) == null) {
					settings = false;
				} else settings = ! BaseBand.settingsManager.getSettingsByMod(module).isEmpty();
				loadModuleDirect(moduleLocation, module, settings);
			} catch (IOException e) {
				System.out.println(module.getName());
				e.printStackTrace();
			}
		}
	}

	private static void loadModuleDirect(String moduleLocation, Module module, boolean settings) throws IOException {
		if (! Files.exists(Paths.get(moduleLocation + module.getName() + ".json"))) {
			return;
		}

		InputStream inputStream = Files.newInputStream(Paths.get(moduleLocation + module.getName() + ".json"));
		JsonObject moduleObject;
		try {
			moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();
		} catch (java.lang.IllegalStateException e) {
			return;
		}

		if (moduleObject.get("Module") == null) {
			return;
		}

		JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
		JsonElement keyObject = settingObject.get("key");
		JsonElement visibleObject = settingObject.get("hidden");

		if (settings) {
			for (Setting setting : BaseBand.settingsManager.getSettingsByMod(module)) {
				JsonElement dataObject = settingObject.get(setting.getName());


				try {
					if (dataObject != null && dataObject.isJsonPrimitive()) {
						if (setting.isCheck()) {
							setting.setValBoolean(dataObject.getAsBoolean());
						}
						if (setting.isSlider()) {
							setting.setValDouble(dataObject.getAsDouble());
						}
						if (setting.isCombo()) {
							setting.setValString(dataObject.getAsString());
						}
					}
				} catch (NumberFormatException e) {
					System.out.println(setting.getName() + " " + module.getName());
					System.out.println(dataObject);
				}
			}
		}

		if (keyObject != null && keyObject.isJsonPrimitive()) {
			module.setKey(Keyboard.getKeyIndex(keyObject.getAsString()));
			//try {
			//     Module.setKey(Keyboard.getKeyIndex(keyObject.getAsString()));
			// } catch (Exception e) {
			//    System.out.println("invalid key");
			//     System.out.println(keyObject);
			//  }
		}
		if (visibleObject != null && visibleObject.isJsonPrimitive()) {
			module.visible = visibleObject.getAsBoolean();
		}

		inputStream.close();
	}

	private static void loadEnabledModules() throws IOException {
		String enabledLocation = fileName + mainName;

		if (! Files.exists(Paths.get(enabledLocation + "Toggle" + ".json"))) {
			return;
		}

		InputStream inputStream = Files.newInputStream(Paths.get(enabledLocation + "Toggle" + ".json"));
		JsonObject moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

		if (moduleObject.get("Modules") == null) {
			return;
		}

		JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();

		for (Module module : BaseBand.moduleManager.getModuleList()) {
			JsonElement dataObject = settingObject.get(module.getName());

			if (dataObject != null && dataObject.isJsonPrimitive()) {
				try {
					module.setToggled(dataObject.getAsBoolean());
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}

		inputStream.close();
	}

	private static void loadModuleKeybinds() throws IOException {
		String keyLocation = fileName + mainName;

		if (! Files.exists(Paths.get(keyLocation + "Key" + ".json"))) {
			return;
		}

		InputStream inputStream = Files.newInputStream(Paths.get(keyLocation + "Key" + ".json"));
		JsonObject moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

		if (moduleObject.get("Modules") == null) {
			return;
		}

		JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();

		for (Module module : BaseBand.moduleManager.getModuleList()) {
			JsonElement dataObject = settingObject.get(module.getName());

			if (dataObject != null && dataObject.isJsonPrimitive()) {
				try {
					module.setKey(Keyboard.getKeyIndex(dataObject.getAsString()));
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
	}


	//Begin Saving Config class


	public static void save() {
		try {
			saveConfig();
			saveModules();
			saveEnabledModules();
			saveModuleKeybinds();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BaseBand.log.info("Saved Config!");
	}

	private static void saveConfig() throws IOException {
		if (! Files.exists(Paths.get(fileName))) {
			Files.createDirectories(Paths.get(fileName));
		}
		if (! Files.exists(Paths.get(fileName + moduleName))) {
			Files.createDirectories(Paths.get(fileName + moduleName));
		}
		if (! Files.exists(Paths.get(fileName + mainName))) {
			Files.createDirectories(Paths.get(fileName + mainName));
		}
		if (! Files.exists(Paths.get(fileName + miscName))) {
			Files.createDirectories(Paths.get(fileName + miscName));
		}
	}

	private static void registerFiles(String location, String name) throws IOException {
		if (Files.exists(Paths.get(fileName + location + name + ".json"))) {
			File file = new File(fileName + location + name + ".json");

			file.delete();

		} else {
			Files.createFile(Paths.get(fileName + location + name + ".json"));
		}

	}

	private static void saveModules() throws IOException {
		for (Module module : BaseBand.moduleManager.getModuleList()) {
			try {
				boolean setting;

				if (BaseBand.settingsManager.getSettingsByMod(module) != null) {
					setting = ! BaseBand.settingsManager.getSettingsByMod(module).isEmpty();
				} else {
					setting = false;
				}

				saveModuleDirect(module, setting);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void saveModuleDirect(Module module, boolean settings) throws IOException {
		registerFiles(moduleName, module.getName());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + moduleName + module.getName() + ".json"), StandardCharsets.UTF_8);
		JsonObject moduleObject = new JsonObject();
		JsonObject settingObject = new JsonObject();
		moduleObject.add("Module", new JsonPrimitive(module.getName()));

		if (settings) {
			if (! (BaseBand.settingsManager.getSettingsByMod(module).isEmpty())) {
				for (Setting setting : BaseBand.settingsManager.getSettingsByMod(module)) {
					if (setting != null) {
						if (setting.isCheck()) {
							settingObject.add(setting.getName(), new JsonPrimitive(setting.getValBoolean()));
						}
						if (setting.isSlider()) {
							settingObject.add(setting.getName(), new JsonPrimitive(setting.getValDouble()));
						}
						if (setting.isCombo()) {
							settingObject.add(setting.getName(), new JsonPrimitive(setting.getValString()));
						}
					}
				}
			}

			settingObject.add("hidden", new JsonPrimitive(module.visible));
			settingObject.add("key", new JsonPrimitive(Keyboard.getKeyName(module.getKey())));
		}


		moduleObject.add("Settings", settingObject);
		String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
		fileOutputStreamWriter.write(jsonString);
		fileOutputStreamWriter.close();
	}

	private static void saveEnabledModules() throws IOException {
		registerFiles(mainName, "Toggle");

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + mainName + "Toggle" + ".json"), StandardCharsets.UTF_8);
		JsonObject moduleObject = new JsonObject();
		JsonObject enabledObject = new JsonObject();

		for (Module mod : BaseBand.moduleManager.getModuleList()) {
			enabledObject.add(mod.getName(), new JsonPrimitive(mod.isToggled()));
		}

		moduleObject.add("Modules", enabledObject);
		String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
		fileOutputStreamWriter.write(jsonString);
		fileOutputStreamWriter.close();
	}

	private static void saveModuleKeybinds() throws IOException {
		registerFiles(mainName, "Key");

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + mainName + "Key" + ".json"), StandardCharsets.UTF_8);
		JsonObject moduleObject = new JsonObject();
		JsonObject keyObject = new JsonObject();

		for (Module mod : BaseBand.moduleManager.getModuleList()) {
			keyObject.add(mod.getName(), new JsonPrimitive(Keyboard.getKeyName(mod.getKey())));
		}

		moduleObject.add("Modules", keyObject);
		String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
		fileOutputStreamWriter.write(jsonString);
		fileOutputStreamWriter.close();
	}


}
