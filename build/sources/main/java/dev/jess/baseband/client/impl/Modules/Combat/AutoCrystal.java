package dev.jess.baseband.client.impl.Modules.Combat;


import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.Friends;
import dev.jess.baseband.client.api.Utils.ObfuscatedHelper;
import dev.jess.baseband.client.api.Utils.RotationUtil;
import dev.jess.baseband.client.api.Utils.TimerUtil;
import dev.jess.baseband.client.api.Utils.kisman.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class AutoCrystal extends Module {


	public final Setting logic = new Setting("CaLogic", this, "BreakPlace", new ArrayList<>(Arrays.asList("BreakPlace", "PlaceBreak")));
	public final Setting placeRange = new Setting("PlaceRange", this, 4, 1, 6, false);
	public final Setting placeWallRange = new Setting("Place Wall Range", this, 4.5d, 0, 6, false);
	public final Setting hand = new Setting("BreakHand", this, "PacketSwing", new ArrayList<>(Arrays.asList("MainHand", "OffHand", "PacketSwing"))); //MainHand, OffHand, PacketSwing
	public final Setting breakRange = new Setting("BreakRange", this, 4, 1, 6, false);
	public final Setting targetRange = new Setting("Target Range", this, 15, 1, 20, true);
	public final Setting packetBreak = new Setting("PacketBreak", this, false);
	public final Setting packetPlace = new Setting("PacketPlace", this, true);
	public final Setting minDMG = new Setting("MinDmg", this, 6, 0, 20, true);
	public final Setting maxSelfDMG = new Setting("MaxSelfDMG", this, 18, 0, 40, true);
	public final Setting antiSuicide = new Setting("AntiSuicide", this, false);
	public final Setting multiPlace = new Setting("MultiPlace", this, false);
	public final Setting raytrace = new Setting("Raytrace", this, true);

	public final Setting check = new Setting("CheckPlacements", this, true);
	public final Setting placeDelay = new Setting("PlaceDelay", this, 4, 0, 160, true);
	public final Setting breakDelay = new Setting("Break Delay", this, 4, 0, 160, true);
	public final Setting switchMode = new Setting("Switch Mode", this, "None", new ArrayList<>(Arrays.asList("None", "Normal", "Silent")));

	private final Setting clientSide = new Setting("Client Side", this, false);
	private final Setting rotate = new Setting("Rotate", this, true);
	private final Setting rotateMode = new Setting("RotateMode", this, "Spoof", new ArrayList<>(Arrays.asList("Packet", "Normal", "Spoof")));

	public AutoCrystal() {
		super("AutoCrystal", "Automatically places and breaks end crystals", Category.COMBAT, new Color(107, 180, 222).getRGB());

		BaseBand.settingsManager.rSetting(logic);
		BaseBand.settingsManager.rSetting(placeRange);
		BaseBand.settingsManager.rSetting(placeWallRange);
		BaseBand.settingsManager.rSetting(hand);
		BaseBand.settingsManager.rSetting(breakRange);
		BaseBand.settingsManager.rSetting(targetRange);
		BaseBand.settingsManager.rSetting(packetBreak);
		BaseBand.settingsManager.rSetting(packetPlace);
		BaseBand.settingsManager.rSetting(minDMG);
		BaseBand.settingsManager.rSetting(maxSelfDMG);
		BaseBand.settingsManager.rSetting(antiSuicide);
		BaseBand.settingsManager.rSetting(multiPlace);
		BaseBand.settingsManager.rSetting(check);
		BaseBand.settingsManager.rSetting(placeDelay);
		BaseBand.settingsManager.rSetting(breakDelay);
		BaseBand.settingsManager.rSetting(switchMode);
		BaseBand.settingsManager.rSetting(clientSide);
		BaseBand.settingsManager.rSetting(rotate);
		BaseBand.settingsManager.rSetting(rotateMode);
	}

	static AI.HalqPos bestCrystalPos = new AI.HalqPos(BlockPos.ORIGIN, 0);



	private final TimerUtil placeTimer = new TimerUtil(), breakTimer = new TimerUtil();
	public EntityPlayer target;



	public AI.HalqPos placeCalculateAI() {
		AI.HalqPos posToReturn = new AI.HalqPos(BlockPos.ORIGIN, 0.5f);
		for (BlockPos pos : AIUtils.getSphere(placeRange.getValDouble())) {
			float targetDamage = CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, target, true);
			float selfDamage = CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, mc.player, true);
			if (CrystalUtils.canPlaceCrystal(pos, check.getValBoolean(), true, multiPlace.getValBoolean(), false)) {
				if (mc.player.getDistance(pos.getX() + 0.5f, pos.getY() + 1.0f, pos.getZ() + 0.5f) > MathUtil.square(placeRange.getValDouble())) continue;
				if (selfDamage > maxSelfDMG.getValDouble()) continue;
				if (targetDamage < minDMG.getValDouble()) continue;
				if (antiSuicide.getValBoolean()) if (selfDamage < 2F) continue;
				if (targetDamage > posToReturn.getTargetDamage()) posToReturn = new AI.HalqPos(pos, targetDamage);
			}
		}
		return posToReturn;
	}


	public void enable() {
		placeTimer.reset();
		bestCrystalPos = new AI.HalqPos(BlockPos.ORIGIN, 0);
	}

	public void disable() {
		target = null;
		super.setMcHudMeta("");
	}



	public void doBreak() {
		final EntityEnderCrystal crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).min(Comparator.comparing(c -> mc.player.getDistance(c))).orElse(null);
		if (crystal == null) return;
		breakProcess(crystal);
		mc.playerController.updateController();
	}

	public void oldRotate(float pitch, float yaw) {
		if (rotate.getValBoolean()) {
			if (rotateMode.getValString().equalsIgnoreCase("Normal")) {
				mc.player.rotationYaw = yaw;
				mc.player.rotationPitch = pitch;
			} else if (rotateMode.getValString().equalsIgnoreCase("Packet")) {
				mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, yaw, mc.player.onGround));
				mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationPitch, pitch, mc.player.onGround));
			}
		}
	}

	public void newRotate(float pitch, float yaw) {
		if (rotate.getValBoolean()) {
			if (rotateMode.getValString().equalsIgnoreCase("Normal")) {
				mc.player.rotationYaw = yaw;
				mc.player.rotationPitch = pitch;
			} else if (rotateMode.getValString().equalsIgnoreCase("Packet")) {
				mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, yaw, mc.player.onGround));
				mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationPitch, pitch, mc.player.onGround));
			}
		}
	}


	public boolean onPacketSend(Packet<?> packet15) {
		bestCrystalPos = placeCalculateAI();
		float[] rot = RotationUtil.getRotationToPos(bestCrystalPos.getBlockPos());
		Packet packet = packet15;
		if (packet instanceof CPacketPlayer && rotateMode.getValString().equalsIgnoreCase("Spoof")) {
			((CPacketPlayer) packet).yaw = rot[0];
			((CPacketPlayer) packet).pitch = rot[1];
		}

		return false;
	}



	private void breakProcess(Entity entity) {
		if (breakTimer.passedMs((long)breakDelay.getValDouble())) {
			if (mc.player.getDistance(entity) < breakRange.getValDouble()) {
				if (breakTimer.passedMs((long)breakDelay.getValDouble())) {
					float[] rot = RotationUtil.getRotationToPos(entity.getPosition());
					newRotate(rot[1], rot[0]);
					for (int i = 0; i <= 1; i++) {
						if (packetBreak.getValBoolean()) {
							mc.player.connection.sendPacket(new CPacketUseEntity(entity));
							CPacketUseEntity packet = new CPacketUseEntity();
							packet.entityId = entity.entityId;
							packet.action = CPacketUseEntity.Action.ATTACK;
							mc.player.connection.sendPacket(packet);
						} else mc.playerController.attackEntity(mc.player, entity);
					}
					try {if (clientSide.getValBoolean()) mc.world.removeEntityFromWorld(entity.entityId);} catch (Exception ignored) {}
					breakTimer.reset();
				}
				breakTimer.reset();
			}
			if (hand.getValString().equals("MainHand")) mc.player.swingArm(EnumHand.MAIN_HAND);
			else if (hand.getValString().equals("OffHand")) mc.player.swingArm(EnumHand.OFF_HAND);
			else mc.player.connection.sendPacket(new CPacketAnimation());
			breakTimer.reset();
		}
		breakTimer.reset();
	}

	public void doPlace() {
		bestCrystalPos = placeCalculateAI();

		float[] old = new float[]{mc.player.rotationYaw, mc.player.rotationPitch};
		if (bestCrystalPos.getBlockPos() != BlockPos.ORIGIN) {
			if (placeTimer.passedMs((long)placeDelay.getValDouble())) {
				int crystalSlot = InventoryUtil.findItem(Items.END_CRYSTAL, 0, 9), oldSlot = mc.player.inventory.currentItem;
				boolean canSwitch = true, offhand = mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL);
				if (crystalSlot != mc.player.inventory.currentItem && switchMode.getValString().equalsIgnoreCase("None") && !offhand) return;
				if (crystalSlot == mc.player.inventory.currentItem || offhand) canSwitch = false;
				if (canSwitch) InventoryUtil.switchToSlot(crystalSlot, switchMode.getValString().equalsIgnoreCase("Silent"));
				if (placeTimer.passedMs((long)placeDelay.getValDouble())) {
					if (packetPlace.getValBoolean()) mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(bestCrystalPos.getBlockPos(), AI.getEnumFacing(raytrace.getValBoolean(), bestCrystalPos.getBlockPos()), mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
					else mc.playerController.processRightClickBlock(mc.player, mc.world, bestCrystalPos.getBlockPos(), AI.getEnumFacing(raytrace.getValBoolean(), bestCrystalPos.getBlockPos()), new Vec3d(0, 0, 0), mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
					placeTimer.reset();
				}
				placeTimer.reset();
				if (switchMode.getValString().equalsIgnoreCase("Silent")) InventoryUtil.switchToSlot(oldSlot, true);
			}
			placeTimer.reset();
		}
		oldRotate(old[1], old[0]);
	}





	public void tick() {
		if (mc.player == null || mc.world == null) return;
		try {
			target = getTarget((float) targetRange.getValDouble());
			if (target == null) {
				super.setMcHudMeta("");
				return;
			}
			//super.setDisplayInfo("[" + target.getName() + "]");
			if (logic.getValString().equals("BreakPlace")) {
				doBreak();
				doPlace();
			} else if (logic.getValString().equals("PlaceBreak")) {
				doPlace();
				doBreak();
			}
		}catch (Exception ignored){
			if(!ObfuscatedHelper.check()) {
				ignored.printStackTrace();
			}
		}
		//if(MultiThread.getValBoolean()) newThread(); //NO
	}


	public static EntityPlayer getTarget(final float range) {
		return getTarget(range, range);
	}

	public static EntityPlayer getTarget(final float range, float wallRange) {
		EntityPlayer currentTarget = null;
		for (int size = mc.world.playerEntities.size(), i = 0; i < size; ++i) {
			final EntityPlayer player = mc.world.playerEntities.get(i);
			if (!isntValid(player, range, wallRange)) {
				if (currentTarget == null) currentTarget = player;
				else if (mc.player.getDistanceSq(player) < mc.player.getDistanceSq(currentTarget)) currentTarget = player;
			}
		}
		return currentTarget;
	}


	public static boolean isntValid(final EntityLivingBase entity, final double range, double wallRange) {
		return (mc.player.getDistance(entity) > (mc.player.canEntityBeSeen(entity) ? range : wallRange)) || entity == mc.player || entity.getHealth() <= 0.0f || entity.isDead || Friends.isFriend(entity.getName());
	}


}

