package dev.jess.baseband.client.impl.Modules.Combat;

import dev.jess.baseband.client.api.Main.BaseBand;
import dev.jess.baseband.client.api.Module.Category;
import dev.jess.baseband.client.api.Module.Module;
import dev.jess.baseband.client.api.Settings.Setting;
import dev.jess.baseband.client.api.Utils.*;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoWeb extends Module {
	private final Setting range = new Setting("Range", this, 4, 1, 6, true);
	private final Setting tickDelay = new Setting("Delay", this, 1, 1, 10, true);
	private final Setting rotate = new Setting("Rotate", this, false);
	private final Setting legs = new Setting("Legs", this, false);
	private final Setting chest = new Setting("Chest", this, false);
	private final Setting head = new Setting("Head", this, false);
	private final Setting debug = new Setting("Debug", this, false);
	EntityPlayer player;
	int delay = 0;
	SecondCounter pps = new SecondCounter();


	public AutoWeb() {
		super("AutoWeb", "WebAura but under a different name because cope", Category.COMBAT, new Color(232, 211, 232).getRGB());
		BaseBand.settingsManager.rSetting(range);
		BaseBand.settingsManager.rSetting(tickDelay);
		BaseBand.settingsManager.rSetting(rotate);
		BaseBand.settingsManager.rSetting(legs);
		BaseBand.settingsManager.rSetting(chest);
		BaseBand.settingsManager.rSetting(head);
		BaseBand.settingsManager.rSetting(debug);
	}

	public static int findHotbarBlockz(Class clazz) {
		for (int i = 0; i < 9; ++ i) {
			ItemStack stack = mc.player.inventory.getStackInSlot(i);
			if (stack == ItemStack.EMPTY) continue;
			if (clazz.isInstance(stack.getItem())) {
				return i;
			}
			if (! (stack.getItem() instanceof ItemBlock) || ! clazz.isInstance(((ItemBlock) stack.getItem()).getBlock()))
				continue;
			return i;
		}
		return - 1;
	}

	public void enable() {
		if (mc.player == null || mc.world == null) return;
		delay = 0;
	}

	public void renderWorld() {
		this.setMcHudMeta("" + pps.getCount());
	}

	public void tick() {
		if (delay < tickDelay.getValDouble()) {
			delay++;
			return;
		} else {
			delay = 0;
		}
		if (debug.getValBoolean()) {
			ChatUtil.sendChatMsg("Delay");
		}
		//player = getTarget(this.range.getValDouble(), false);
		findClosestTarget();

		if (player == null) {
			if (debug.getValBoolean()) {
				ChatUtil.sendChatMsg("No Target!");
			}
			return;
		}

		if (mc.player.getDistance(player) > range.getValDouble()) {
			return;
		}

		if (debug.getValBoolean()) {
			ChatUtil.sendChatMsg(player.getName() + " targetted!");
		}
		ArrayList<Vec3d> placeTargets = getPos();
		if (debug.getValBoolean()) {
			ChatUtil.sendChatMsg("PlaceTargets Set");
		}
		if (placeTargets == null) {
			if (debug.getValBoolean()) {
				ChatUtil.sendChatMsg("PlaceTargets Null!");
			}
			return;
		}
		placeList(placeTargets);
		if (debug.getValBoolean()) {
			ChatUtil.sendChatMsg("Completed loop");
		}
	}

	private void findClosestTarget() {

		List<EntityPlayer> playerList = mc.world.playerEntities;

		player = null;

		for (EntityPlayer target : playerList) {

			if (target == mc.player) {
				continue;
			}

			if (Friends.isFriend(target.getName())) {
				continue;
			}

			if (! EntityUtil.isLiving(target)) {
				continue;
			}

			if ((target).getHealth() <= 0) {
				continue;
			}

			if (player == null) {
				player = target;
				continue;
			}

			if (mc.player.getDistance(target) < mc.player.getDistance(player)) {
				player = target;
			}

		}

	}

	private ArrayList<Vec3d> getPos() {
		ArrayList<Vec3d> list = new ArrayList<>();
		if (player == null) return null;
		Vec3d baseVec = player.getPositionVector();
		//if (this.lowFeet.get_value(true)) {
		list.add(baseVec.add(0.0, - 1.0, 0.0));
		//}
		if (legs.getValBoolean()) {
			list.add(baseVec);
		}
		if (chest.getValBoolean()) {
			list.add(baseVec.add(0.0, 1.0, 0.0));
		}
		if (head.getValBoolean()) {
			list.add(baseVec.add(0.0, 2.0, 0.0));
		}
		return list;
	}

	private void placeList(ArrayList<Vec3d> list) {
		list.sort((vec3d, vec3d2) -> Double.compare(mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
		list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
		for (Vec3d vec3d3 : list) {
			BlockPos position = new BlockPos(vec3d3);
			int placeability = BlockUtil.isPositionPlaceable(position, false);
			if (placeability != 3 && placeability != 1) continue;
			this.placeBlock(position);
			if (debug.getValBoolean()) {
				ChatUtil.sendChatMsg("Added Vec3d to PlaceList");
			}
		}
	}

	private void placeBlock(BlockPos pos) {
		int oldSlot = mc.player.inventory.currentItem;
		if (findHotbarBlockz(BlockWeb.class) == - 1) return;
		mc.player.connection.sendPacket(new CPacketHeldItemChange(findHotbarBlockz(BlockWeb.class)));
		mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
		mc.playerController.updateController();
		if (rotate.getValBoolean()) {
			BlockInteractionHelper.faceVectorPacketInstant(new Vec3d(pos));
		}
		if (debug.getValBoolean()) {
			ChatUtil.sendChatMsg("Placed Block");
		}
		pps.increment();
		BlockInteractionHelper.placeBlockScaffold(pos);
		mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		mc.playerController.updateController();
		mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
	}


}
