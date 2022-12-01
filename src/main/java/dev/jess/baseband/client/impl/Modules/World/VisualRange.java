package dev.jess.baseband.client.impl.Modules.World;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import dev.jess.baseband.client.api.Utils.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.ArrayList;

public class VisualRange extends Module {
	private final ArrayList<String> names = new ArrayList<>();
	private final ArrayList<String> newnames = new ArrayList<>();

	public VisualRange() {
		super("VisualRange", "Yum.", Category.WORLD, new Color(21, 222, 100).getRGB());
	}

	public void disable() {
		names.clear();
		newnames.clear();
	}

	public void tick() {
		this.setMcHudMeta(String.valueOf(names.size()));
		this.newnames.clear();
		try {
			for (final Entity entity : mc.world.loadedEntityList)
				if (entity instanceof EntityPlayer && ! entity.getName().equalsIgnoreCase(mc.player.getName())) {
					this.newnames.add(entity.getName());
					if(!names.contains(entity.getName())) {
						getOverlappingPlayers((EntityPlayer) entity);
					}
				}

			if (! this.names.equals(this.newnames)) {
				for (final String name : this.newnames)
					if (! this.names.contains(name))
						ChatUtil.sendChatMsg(ChatFormatting.GREEN + name + " entered visual range.");
				for (final String name : this.names)
					if (! this.newnames.contains(name))
						ChatUtil.sendChatMsg(ChatFormatting.GREEN + name + " left visual range.");
				this.names.clear();
				this.names.addAll(this.newnames);
			}
		} catch (Exception ignored) {
		}
	}

	public void getOverlappingPlayers(EntityPlayer player){
		long z = Math.round(player.posZ);
		long x = Math.round(player.posX);
		for (Entity entity : mc.world.loadedEntityList){
			if(entity instanceof EntityPlayer){
				if(entity.getName()== player.getName()){
					continue;
				}
				long z2 = Math.round(entity.posZ);
				long x2 = Math.round(entity.posX);

				if(x==x2 && z==z2){
					ChatUtil.sendChatMsg(ChatFormatting.RED+"Player Name "+entity.getName()+" overlapped with "+player.getName());
					ChatUtil.sendChatMsg("I think "+player.getName()+" teleported "+entity.getName()+"!");
				}
			}
		}
	}
}