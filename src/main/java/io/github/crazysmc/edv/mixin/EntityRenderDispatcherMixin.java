package io.github.crazysmc.edv.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin
{
  @Shadow
  private static void renderServerSideHitbox(PoseStack poseStack, Entity entity, MultiBufferSource multiBufferSource)
  {
  }

  @Inject(method = "renderServerSideHitbox",
          at = @At(value = "INVOKE",
                   target = "Lnet/minecraft/client/renderer/debug/DebugRenderer;renderFloatingText(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/lang/String;DDDI)V"),
          cancellable = true)
  private static void noFloatingText(PoseStack poseStack, Entity entity, MultiBufferSource multiBufferSource,
                                     CallbackInfo ci)
  {
    ci.cancel();
  }

  @Inject(method = "render",
          at = @At(value = "INVOKE",
                   target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;renderHitbox(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;FFFF)V"))
  private void renderServerSideHitbox(Entity entity, double d, double e, double f, float g, float h,
                                      PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci)
  {
    renderServerSideHitbox(poseStack, entity, multiBufferSource);
  }
}
