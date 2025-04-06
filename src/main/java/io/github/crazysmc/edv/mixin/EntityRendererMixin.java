package io.github.crazysmc.edv.mixin;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HitboxesRenderState;
import net.minecraft.client.renderer.entity.state.ServerHitboxesRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin
{
  @Shadow
  private static Entity getServerSideEntity(Entity entity)
  {
    return null;
  }

  @Shadow
  protected abstract HitboxesRenderState extractHitboxes(Entity entity, float f, boolean bl);

  @Inject(method = "extractHitboxes(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V",
          at = @At("RETURN"))
  private void extractServerSideHitboxes(Entity entity, EntityRenderState entityRenderState, float f, CallbackInfo ci)
  {
    Entity serverSideEntity = getServerSideEntity(entity);
    if (serverSideEntity == null)
    {
      entityRenderState.serverHitboxesRenderState = new ServerHitboxesRenderState(true);
      return;
    }
    Vec3 position = serverSideEntity.position();
    Vec3 movement = serverSideEntity.getDeltaMovement();
    entityRenderState.serverHitboxesRenderState =
        new ServerHitboxesRenderState(false, position.x, position.y, position.z, movement.x, movement.y, movement.z,
                                      serverSideEntity.getEyeHeight(), extractHitboxes(serverSideEntity, f, true));
  }
}
