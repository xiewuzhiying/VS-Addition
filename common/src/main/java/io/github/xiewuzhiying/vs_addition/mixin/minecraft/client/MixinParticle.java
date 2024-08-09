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
import org.spongepowered.asm.mixin.Shadow;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSClientGameUtils;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

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
            par1.vertex(1f, 2f, 3f);
            bufferbuilder.nextElement();
        }
    }
}
