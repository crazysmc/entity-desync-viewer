package io.github.crazysmc.edv.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin
{
  @Shadow
  private static void renderServerSideHitbox(PoseStack poseStack, Entity entity, MultiBufferSource multiBufferSource)
  {
  }

  @WrapOperation(method = "render(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V",
                 at = @At(value = "INVOKE",
                          target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;renderHitbox(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;FFFF)V"))
  private void wrapRenderHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float tickDelta,
                                float r, float g, float b, Operation<Void> original,
                                @Local(argsOnly = true) MultiBufferSource multiBufferSource)
  {
    try
    {
      renderServerSideHitbox(poseStack, entity, multiBufferSource);
      original.call(poseStack, vertexConsumer, entity, tickDelta, r, g, b);
    }
    catch (IllegalStateException ignored)
    {
    }
  }
}
