package dev.jess.baseband.client.impl.Modules.Misc;


import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Utils.RotationUtil;
import dev.jess.baseband.client.api.Utils.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.*;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec2f;

import java.awt.*;

public class AutoMount extends Module {
	public AutoMount() {
		super("AutoMount", "sss", Category.MISC, new Color(232, 22, 232).getRGB());
	}


	public void tick() {
		if (mc.player.ridingEntity != null)
			return;

		for (Entity e : mc.world.loadedEntityList) {
			if (valid(e)) {
				Vec2f rot = RotationUtil.getRotationTo(e.getPositionVector());
				mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rot.x, rot.y, mc.player.onGround));
				mc.playerController.interactWithEntity(mc.player, e, EnumHand.MAIN_HAND);
			}

		}

	}

	boolean valid(Entity entity) {
		return (entity instanceof EntityBoat
				|| (entity instanceof EntityAnimal && ((EntityAnimal) entity).getGrowingAge() == 1
				&& (entity instanceof EntityHorse
				|| entity instanceof EntitySkeletonHorse
				|| entity instanceof EntityDonkey
				|| entity instanceof EntityMule
				|| entity instanceof EntityPig && ((EntityPig) entity).getSaddled()
				|| entity instanceof EntityLlama))) && Wrapper.getMinecraft().player.getDistance(entity) < 6;
	}


}
