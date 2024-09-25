package io.github.xiewuzhiying.vs_addition.mixin.minecraft.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSClientGameUtils;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Pseudo
@Mixin(Particle.class)
public abstract class MixinParticle {
    @Shadow protected double x;

    @Shadow protected double y;

    @Shadow protected double z;

    @WrapMethod(
            method = "render"
    )
    private void man(VertexConsumer par1, Camera par2, float par3, Operation<Void> original) {
        if(par1 instanceof BufferBuilder bufferbuilder && VSGameUtilsKt.isBlockInShipyard(Minecraft.getInstance().level, this.x, this.y, this.z)) {
            ClientShip ship = VSClientGameUtils.getClientShip(this.x, this.y, this.z);
            Vector3d vec = ship.getRenderTransform().getShipToWorld().transformPosition(new Vector3d());
            bufferbuilder.vertex(vec.x, vec.y, vec.z)
                    .color(1.0F, 1.0F, 1.0F, 1.0F) // 设置颜色
                    .endVertex();
            original.call(bufferbuilder, par2, par3);
        }
    }
}
