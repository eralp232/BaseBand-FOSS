package dev.jess.baseband.client.impl.Modules.Misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Utils.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BurrowCounter extends Module {
	private final ConcurrentHashMap<EntityPlayer, Integer> players = new ConcurrentHashMap<>();
	private final List<EntityPlayer> anti_spam = new ArrayList<>();

	public BurrowCounter() {
		super("BurrowCounter", "BurrowCounter", Category.MISC, new Color(124, 22, 231, 255).getRGB());
	}


	private void add_player(EntityPlayer player) {
		if (player == null) return;
		if (players.containsKey(player)) {
			int value = players.get(player) + 1;
			players.put(player, value);
			ChatUtil.sendChatMsg(ChatFormatting.GREEN + "!!" + TextFormatting.RESET + player.getName() + ChatFormatting.GREEN + " has burrowed/used an anvil " + value + " times.");
		} else {
			players.put(player, 1);
			ChatUtil.sendChatMsg(ChatFormatting.GREEN + "!!" + TextFormatting.RESET + player.getName() + ChatFormatting.GREEN + " has burrowed/used an anvil.");
		}
	}


	public void tick() {
		if (mc.player == null || mc.world == null) return;

		for (EntityPlayer player : mc.world.playerEntities) {
			if (anti_spam.contains(player)) continue;


			if (isBurrowed(player)) {
				add_player(player);
				anti_spam.add(player);
			}
		}
	}


	private boolean isBurrowed(Entity entity) {
		BlockPos entityPos = new BlockPos(roundValueToCenter(entity.posX), entity.posY + .2, roundValueToCenter(entity.posZ));

		return mc.world.getBlockState(entityPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(entityPos).getBlock() == Blocks.ENDER_CHEST;
	}

	private double roundValueToCenter(double inputVal) {
		double roundVal = Math.round(inputVal);

		if (roundVal > inputVal) {
			roundVal -= 0.5;
		} else if (roundVal <= inputVal) {
			roundVal += 0.5;
		}

		return roundVal;
	}

}
