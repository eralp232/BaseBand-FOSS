package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Utils.SecondCounter;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class BowSpam extends Module {
	SecondCounter sps = new SecondCounter();

	public BowSpam() {
		super("BowSpam", "MoneyMod+2 go brrr", Category.COMBAT, new Color(90, 170, 246, 255).getRGB());
	}

	public void tick() {
		if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
			sps.increment();
			mc.player.stopActiveHand();
		}
	}


	public void renderWorld() {
		this.setMcHudMeta("" + sps.getCount());
	}

}
