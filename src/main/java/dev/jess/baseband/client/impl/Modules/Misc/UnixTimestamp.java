package dev.jess.baseband.client.impl.Modules.Misc;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;

import java.awt.*;
import java.util.Date;

public class UnixTimestamp extends Module {
	public UnixTimestamp() {
		super("UnixTimestamp", "furryware", Category.MISC, new Color(232,111,111,255).getRGB());
	}


	public long date=0;


	public void tick(){
		date=new Date().getTime();
	}

	public void renderWorld() {
		this.setMcHudMeta(date+"");
	}
}
