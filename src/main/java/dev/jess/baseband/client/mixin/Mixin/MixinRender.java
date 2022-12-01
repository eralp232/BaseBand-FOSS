package dev.jess.baseband.client.mixin.Mixin;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Render.class, priority = 9999)
public abstract class MixinRender<T extends Entity> {
	@Shadow
	protected boolean renderOutlines;
	@Shadow
	@Final
	protected RenderManager renderManager;

	@Shadow
	protected abstract int getTeamColor(T entityIn);

	@Shadow
	protected abstract boolean bindEntityTexture(T entity);
}
