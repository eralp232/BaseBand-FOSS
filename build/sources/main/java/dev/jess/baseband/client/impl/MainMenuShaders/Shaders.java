package dev.jess.baseband.client.impl.MainMenuShaders;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Registry.ModuleRegistry;

import java.io.InputStream;

public class Shaders {
	static String randomname = null;
	static InputStream fis = null;
	public GLSLSandboxShader currentshader;
	public long time;

	public static void clear() {
		randomname = null;

		fis = null;
	}

	public void init() {
		try {
			Object[] shader = getShader();
			clear();
			if (shader == null) {
				currentshader = null;
				BaseBand.log.info("MainMenuShaders Disabled.");
			} else {
				String name = (String) shader[0];
				InputStream is = (InputStream) shader[1];

				currentshader = new GLSLSandboxShader(name, is);
				if (! currentshader.initialized)
					currentshader = null;
				else
					time = System.currentTimeMillis();
			}
		} catch (Exception e) {
			e.printStackTrace();
			currentshader = null;
		}
	}

	public Object[] getShader() {
		if (! BaseBand.moduleManager.getByName("MainMenuShader").isToggled()) {
			return null;
		}

		randomname = null;
		fis = null;


		switch (BaseBand.settingsManager.getSettingByName(ModuleRegistry.getModule("MainMenuShader"), "Shader: ").getValString()) {
			case ("BaseBand"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/baseband.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/baseband.fsh");
				break;
			}

			case ("Blue Hole"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/bluehole.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/bluehole.fsh");
				break;
			}
			case ("Cube"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/cube.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/cube.fsh");
				break;
			}
			case ("Cubic Pulse"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/cubicpulse.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/cubicpulse.fsh");
				break;
			}
			case ("Dalek"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/dalek.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/dalek.fsh");
				break;
			}
			case ("Dj"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/dj.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/dj.fsh");
				break;
			}
			case ("DNA"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/dna.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/dna.fsh");
				break;
			}
			case ("Doom"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/doom.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/doom.fsh");
				break;
			}
			case ("Flappy Bird"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/flappybird.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/flappybird.fsh");
				break;
			}
			case ("Gas"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/gas.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/gas.fsh");
				break;
			}
			case ("Leafy"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/leafy.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/leafy.fsh");
				break;
			}
			case ("Minecraft"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/minecraft.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/minecraft.fsh");
				break;
			}
			case ("Nebula"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/nebula.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/nebula.fsh");
				break;
			}
			case ("Steam"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/steam.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/steam.fsh");
				break;
			}
			case ("TudbuT"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/tudbut.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/tudbut.fsh");
				break;
			}
			case ("Ukraine"): {
				randomname = String.valueOf(GLSLSandboxShader.class.getResource("/shaders/ukraine.fsh"));
				fis = GLSLSandboxShader.class.getResourceAsStream("/shaders/ukraine.fsh");
				break;
			}
			default: {
				randomname = null;
				fis = null;
			}

		}


		return new Object[]{randomname, fis};
	}
}
