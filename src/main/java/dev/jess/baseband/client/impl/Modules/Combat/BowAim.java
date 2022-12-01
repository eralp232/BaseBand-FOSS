package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class BowAim extends Module {

	private final Setting maxDist = new Setting("Range", this, 20, 1, 50, true);

	public BowAim() {
		super("BowAim", "Like the FutureClient.", Category.COMBAT, new Color(80, 0, 0, 255).getRGB());
		BaseBand.settingsManager.rSetting(maxDist);
	}

	public static float[] calculateAngle(Vec3d from, Vec3d to) {
		return new float[]{
				(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(to.z - from.z, to.x - from.x)) - 90.0), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2((to.y - from.y) * - 1.0, MathHelper.sqrt(Math.pow(to.x - from.x, 2) + Math.pow(to.z - from.z, 2)))))
		};
	}


	public void tick() {
		if (mc.player == null && mc.world == null) return;

		if (! mc.player.isHandActive()) {
			setMcHudMeta("");
		}

		if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
			EntityPlayer player = null;
			float tickDis = 100f;
			for (EntityPlayer p : mc.world.playerEntities) {
				if (p == mc.player) continue;
				if (p.getDistance(mc.player) > (int) maxDist.getValDouble()) continue;
				float dis = p.getDistance(mc.player);
				if (dis < tickDis) {
					tickDis = dis;
					player = p;
				}
			}
			if (player != null) {
				this.setMcHudMeta(player.getName());
				Vec3d pos = EntityUtil.getInterpolatedPos(player, mc.getRenderPartialTicks());
				float[] angels = calculateAngle(EntityUtil.getInterpolatedPos(mc.player, mc.getRenderPartialTicks()), pos);
				mc.player.rotationYaw = angels[0];
				mc.player.rotationPitch = angels[1];
			}
		}
	}


}
