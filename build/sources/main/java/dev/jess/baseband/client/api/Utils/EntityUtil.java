package dev.jess.baseband.client.api.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;

import java.util.Objects;

public class EntityUtil {

	private static final Minecraft mc = Minecraft.getMinecraft();

	public static boolean isPassive(Entity e) {
		if (e instanceof EntityWolf && ((EntityWolf) e).isAngry()) return false;
		if (e instanceof EntityAnimal || e instanceof EntityAgeable || e instanceof EntityTameable || e instanceof EntityAmbientCreature || e instanceof EntitySquid)
			return true;
		return e instanceof EntityIronGolem && ((EntityIronGolem) e).getRevengeTarget() == null;
	}

	public static boolean isLiving(Entity e) {
		return e instanceof EntityLivingBase;
	}

	public static boolean isFakeLocalPlayer(Entity entity) {
		return entity != null && entity.getEntityId() == - 100 && Wrapper.getPlayer() != entity;
	}

	public static double getMaxSpeed() {
		double maxModifier = 0.2873;
		if (Minecraft.getMinecraft().player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(1)))) {
			maxModifier *= 1.0 + 0.2 * (double) (Objects.requireNonNull(Minecraft.getMinecraft().player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier() + 1);
		}
		return maxModifier;
	}


	/**
	 * Find the entities interpolated amount
	 */
	public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
		return new Vec3d(
				(entity.posX - entity.lastTickPosX) * x,
				(entity.posY - entity.lastTickPosY) * y,
				(entity.posZ - entity.lastTickPosZ) * z
		);
	}

	public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
		return getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
	}

	public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
		return getInterpolatedAmount(entity, ticks, ticks, ticks);
	}

	public static boolean isMobAggressive(Entity entity) {
		if (entity instanceof EntityPigZombie) {
			// arms raised = aggressive, angry = either game or we have set the anger cooldown
			if (((EntityPigZombie) entity).isArmsRaised() || ((EntityPigZombie) entity).isAngry()) {
				return true;
			}
		} else if (entity instanceof EntityWolf) {
			return ((EntityWolf) entity).isAngry() &&
					! Wrapper.getPlayer().equals(((EntityWolf) entity).getOwner());
		} else if (entity instanceof EntityEnderman) {
			return ((EntityEnderman) entity).isScreaming();
		}
		return isHostileMob(entity);
	}

	/**
	 * If the mob by default wont attack the player, but will if the player attacks it
	 */
	public static boolean isNeutralMob(Entity entity) {
		return entity instanceof EntityPigZombie ||
				entity instanceof EntityWolf ||
				entity instanceof EntityEnderman;
	}


	public static float getBlockDensity(final Vec3d vec, final AxisAlignedBB bb) {
		final double d0 = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
		final double d2 = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
		final double d3 = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
		final double d4 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
		final double d5 = (1.0 - Math.floor(1.0 / d3) * d3) / 2.0;
		float j2 = 0.0f;
		float k2 = 0.0f;
		for (float f = 0.0f; f <= 1.0f; f += (float) d0) {
			for (float f2 = 0.0f; f2 <= 1.0f; f2 += (float) d2) {
				for (float f3 = 0.0f; f3 <= 1.0f; f3 += (float) d3) {
					final double d6 = bb.minX + (bb.maxX - bb.minX) * f;
					final double d7 = bb.minY + (bb.maxY - bb.minY) * f2;
					final double d8 = bb.minZ + (bb.maxZ - bb.minZ) * f3;
					if (rayTraceBlocks(new Vec3d(d6 + d4, d7, d8 + d5), vec, false, false, false) == null) ++ j2;
					++ k2;
				}
			}
		}
		return j2 / k2;
	}


	public static RayTraceResult rayTraceBlocks(Vec3d vec31, final Vec3d vec32, final boolean stopOnLiquid, final boolean ignoreBlockWithoutBoundingBox, final boolean returnLastUncollidableBlock) {
		final int i = MathHelper.floor(vec32.x);
		final int j = MathHelper.floor(vec32.y);
		final int k = MathHelper.floor(vec32.z);
		int l = MathHelper.floor(vec31.x);
		int i2 = MathHelper.floor(vec31.y);
		int j2 = MathHelper.floor(vec31.z);
		BlockPos blockpos = new BlockPos(l, i2, j2);
		final IBlockState iblockstate = EntityUtil.mc.world.getBlockState(blockpos);
		final Block block = iblockstate.getBlock();
		if ((! ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(mc.world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid))
			return iblockstate.collisionRayTrace(mc.world, blockpos, vec31, vec32);

		RayTraceResult raytraceresult2 = null;
		int k2 = 200;
		while (k2-- >= 0) {
			if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
				return null;
			}
			if (l == i && i2 == j && j2 == k) {
				return returnLastUncollidableBlock ? raytraceresult2 : null;
			}
			boolean flag2 = true;
			boolean flag3 = true;
			boolean flag4 = true;
			double d0 = 999.0;
			double d2 = 999.0;
			double d3 = 999.0;
			if (i > l) {
				d0 = l + 1.0;
			} else if (i < l) {
				d0 = l + 0.0;
			} else {
				flag2 = false;
			}
			if (j > i2) {
				d2 = i2 + 1.0;
			} else if (j < i2) {
				d2 = i2 + 0.0;
			} else {
				flag3 = false;
			}
			if (k > j2) {
				d3 = j2 + 1.0;
			} else if (k < j2) {
				d3 = j2 + 0.0;
			} else {
				flag4 = false;
			}
			double d4 = 999.0;
			double d5 = 999.0;
			double d6 = 999.0;
			final double d7 = vec32.x - vec31.x;
			final double d8 = vec32.y - vec31.y;
			final double d9 = vec32.z - vec31.z;
			if (flag2) {
				d4 = (d0 - vec31.x) / d7;
			}
			if (flag3) {
				d5 = (d2 - vec31.y) / d8;
			}
			if (flag4) {
				d6 = (d3 - vec31.z) / d9;
			}
			if (d4 == - 0.0) {
				d4 = - 1.0E-4;
			}
			if (d5 == - 0.0) {
				d5 = - 1.0E-4;
			}
			if (d6 == - 0.0) {
				d6 = - 1.0E-4;
			}
			EnumFacing enumfacing;
			if (d4 < d5 && d4 < d6) {
				enumfacing = ((i > l) ? EnumFacing.WEST : EnumFacing.EAST);
				vec31 = new Vec3d(d0, vec31.y + d8 * d4, vec31.z + d9 * d4);
			} else if (d5 < d6) {
				enumfacing = ((j > i2) ? EnumFacing.DOWN : EnumFacing.UP);
				vec31 = new Vec3d(vec31.x + d7 * d5, d2, vec31.z + d9 * d5);
			} else {
				enumfacing = ((k > j2) ? EnumFacing.NORTH : EnumFacing.SOUTH);
				vec31 = new Vec3d(vec31.x + d7 * d6, vec31.y + d8 * d6, d3);
			}
			l = MathHelper.floor(vec31.x) - ((enumfacing == EnumFacing.EAST) ? 1 : 0);
			i2 = MathHelper.floor(vec31.y) - ((enumfacing == EnumFacing.UP) ? 1 : 0);
			j2 = MathHelper.floor(vec31.z) - ((enumfacing == EnumFacing.SOUTH) ? 1 : 0);
			blockpos = new BlockPos(l, i2, j2);
			final IBlockState iblockstate2 = EntityUtil.mc.world.getBlockState(blockpos);
			final Block block2 = iblockstate2.getBlock();
			if (ignoreBlockWithoutBoundingBox && iblockstate2.getMaterial() != Material.PORTAL && iblockstate2.getCollisionBoundingBox(mc.world, blockpos) == Block.NULL_AABB) {
				continue;
			}
			if (block2.canCollideCheck(iblockstate2, stopOnLiquid) && ! (block2 instanceof BlockWeb)) {
				return iblockstate2.collisionRayTrace(mc.world, blockpos, vec31, vec32);
			}
			raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
		}
		return returnLastUncollidableBlock ? raytraceresult2 : null;
	}


	public static double getDistance(double p_X, double p_Y, double p_Z, double x, double y, double z) {
		double d0 = p_X - x;
		double d1 = p_Y - y;
		double d2 = p_Z - z;
		return MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
	}


	public static boolean basicChecksEntity(Entity pl) {
		return pl.getName().equals(mc.player.getName()) || pl.isDead;
	}


	/**
	 * If the mob is friendly (not aggressive)
	 */
	public static boolean isFriendlyMob(Entity entity) {
		return (entity.isCreatureType(EnumCreatureType.CREATURE, false) && ! EntityUtil.isNeutralMob(entity)) ||
				(entity.isCreatureType(EnumCreatureType.AMBIENT, false)) ||
				entity instanceof EntityVillager ||
				entity instanceof EntityIronGolem ||
				(isNeutralMob(entity) && ! EntityUtil.isMobAggressive(entity));
	}

	/**
	 * If the mob is hostile
	 */
	public static boolean isHostileMob(Entity entity) {
		return (entity.isCreatureType(EnumCreatureType.MONSTER, false) && ! EntityUtil.isNeutralMob(entity));
	}

	/**
	 * Find the entities interpolated position
	 */
	public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
		return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
	}

	public static Vec3d getInterpolatedRenderPos(Entity entity, float ticks) {
		return getInterpolatedPos(entity, ticks).subtract(Wrapper.getMinecraft().getRenderManager().renderPosX, Wrapper.getMinecraft().getRenderManager().renderPosY, Wrapper.getMinecraft().getRenderManager().renderPosZ);
	}

	public static boolean isInWater(Entity entity) {
		if (entity == null) return false;

		double y = entity.posY + 0.01;

		for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++)
			for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); z++) {
				BlockPos pos = new BlockPos(x, (int) y, z);

				if (Wrapper.getWorld().getBlockState(pos).getBlock() instanceof BlockLiquid) return true;
			}

		return false;
	}

	public static boolean isDrivenByPlayer(Entity entityIn) {
		return Wrapper.getPlayer() != null && entityIn != null && entityIn.equals(Wrapper.getPlayer().getRidingEntity());
	}

	public static boolean isAboveWater(Entity entity) {
		return isAboveWater(entity, false);
	}

	public static boolean isAboveWater(Entity entity, boolean packet) {
		if (entity == null) return false;

		double y = entity.posY - (packet ? 0.03 : (EntityUtil.isPlayer(entity) ? 0.2 : 0.5)); // increasing this seems to flag more in NCP but needs to be increased so the player lands on solid water

		for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++)
			for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); z++) {
				BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);

				if (Wrapper.getWorld().getBlockState(pos).getBlock() instanceof BlockLiquid) return true;
			}

		return false;
	}

	public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
		double dirx = me.posX - px;
		double diry = me.posY - py;
		double dirz = me.posZ - pz;

		double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

		dirx /= len;
		diry /= len;
		dirz /= len;

		double pitch = Math.asin(diry);
		double yaw = Math.atan2(dirz, dirx);

		//to degree
		pitch = pitch * 180.0d / Math.PI;
		yaw = yaw * 180.0d / Math.PI;

		yaw += 90f;

		return new double[]{yaw, pitch};
	}

	public static boolean isPlayer(Entity entity) {
		return entity instanceof EntityPlayer;
	}

	public static double getRelativeX(float yaw) {
		return MathHelper.sin(- yaw * 0.017453292F);
	}

	public static double getRelativeZ(float yaw) {
		return MathHelper.cos(yaw * 0.017453292F);
	}


	public static boolean canPlaceCrystal(BlockPos blockPos, boolean multiPlace, boolean thirteen) {
		try {
			if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
				return false;
			}

			if (! thirteen && ! mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().isReplaceable(mc.world, blockPos) || ! mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().isReplaceable(mc.world, blockPos)) {
				return false;
			}

			for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos.add(0, 1, 0)))) {
				if (entity.isDead || multiPlace && entity instanceof EntityEnderCrystal) continue;

				return false;
			}

			if (! thirteen) {
				for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos.add(0, 2, 0)))) {
					if (entity.isDead || multiPlace && entity instanceof EntityEnderCrystal) continue;

					return false;
				}
			}
		} catch (Exception exception) {
			return false;
		}

		return true;
	}


	public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
		try {
			double factor = (1.0 - entity.getDistance(posX, posY, posZ) / 12.0) * entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());

			float calculatedDamage = (float) (int) ((factor * factor + factor) / 2.0f * 7.0f * 12.0f + 1.0f);

			double damage = 1.0;

			if (entity instanceof EntityLivingBase) {//maybe i broke this by making entity entity not null lol idk
				damage = getBlastReduction((EntityLivingBase) entity, calculatedDamage * ((mc.world.getDifficulty().getId() == 0) ? 0.0f : ((mc.world.getDifficulty().getId() == 2) ? 1.0f : ((mc.world.getDifficulty().getId() == 1) ? 0.5f : 1.5f))), new Explosion(mc.world, entity, posX, posY, posZ, 6.0f, false, true));
			}

			return (float) damage;
		} catch (Exception ignored) {
		}

		return 0.0f;
	}

	public static float getBlastReduction(EntityLivingBase entityLivingBase, float damage, Explosion explosion) {
		if (entityLivingBase instanceof EntityPlayer) {
			damage = CombatRules.getDamageAfterAbsorb(damage, (float) entityLivingBase.getTotalArmorValue(), (float) entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
			damage *= 1.0f - MathHelper.clamp((float) EnchantmentHelper.getEnchantmentModifierDamage(entityLivingBase.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion)), 0.0f, 20.0f) / 25.0f;

			if (entityLivingBase.isPotionActive(MobEffects.RESISTANCE)) {
				damage -= damage / 4.0f;
			}

			return damage;
		}

		damage = CombatRules.getDamageAfterAbsorb(damage, (float) entityLivingBase.getTotalArmorValue(), (float) entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

		return damage;
	}


}
