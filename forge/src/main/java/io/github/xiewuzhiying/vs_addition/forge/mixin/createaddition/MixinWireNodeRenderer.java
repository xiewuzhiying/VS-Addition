package io.github.xiewuzhiying.vs_addition.forge.mixin.createaddition;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import com.mrh0.createaddition.energy.IWireNode;
import com.mrh0.createaddition.energy.WireType;
import com.mrh0.createaddition.rendering.WireNodeRenderer;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.lang.Math;

@Mixin(WireNodeRenderer.class)
public abstract class MixinWireNodeRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {

    @Unique
    private static Ship vs_addition$shareShip;

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mrh0/createaddition/rendering/WireNodeRenderer;distanceFromZero(FFF)F",
                    ordinal = 0
            )
    )
    public float distance1(float x, float y, float z, Operation<Float> original, @Local(ordinal = 0) IWireNode te,
                          @Local(ordinal = 0) BlockPos otherPos, @Local(ordinal = 0) Vec3 offset1, @Local(ordinal = 1) Vec3 offset2) {
        Level level = ((BlockEntity)te).getLevel();
        Ship ship = VSGameUtilsKt.getShipManagingPos(level, te.getPos());
        Vector3d diff;
        Vector3d vec1 = VSGameUtilsKt.toWorldCoordinates(level, VectorConversionsMCKt.toJOML(VecHelper.getCenterOf(otherPos)));
        Vector3d vec2 = VSGameUtilsKt.toWorldCoordinates(level, VectorConversionsMCKt.toJOML(VecHelper.getCenterOf(te.getPos())));
        diff = new Vector3d(vec1.sub(vec2));
        if(ship!=null) {
            Quaterniond shipQuat = new Quaterniond(ship.getTransform().getShipToWorldRotation());
            diff = diff.rotate(shipQuat.conjugate());
        }
        return original.call((float) diff.x, (float) diff.y ,(float) diff.z);
    }

//    @WrapOperation(
//            method = "render",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/mrh0/createaddition/rendering/WireNodeRenderer;distanceFromZero(FFF)F",
//                    ordinal = 1
//            )
//    )
//    public float distance2(float x, float y, float z, Operation<Float> original, @Local(ordinal = 3) Vec3 playerPos) {
//        vs_addition$vec3 = VSGameUtilsKt.toWorldCoordinates(vs_addition$level, VectorConversionsMCKt.toJOML(playerPos));
//        Vector3d diff = vs_addition$vec3.sub(vs_addition$vec2);
//        if(vs_addition$shareShip!=null)
//            diff = diff.rotate(vs_addition$shareShip.getTransform().getShipToWorldRotation().conjugate(new Quaterniond()));
//        return original.call((float) diff.x, (float) diff.y ,(float) diff.z);
//    }


    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V",
                    ordinal = 0
            )
    )
    public void translate1(PoseStack instance, double x, double y, double z, Operation<Void> original, @Local(ordinal = 0) IWireNode te,
                           @Local(ordinal = 0) BlockPos otherPos, @Local(ordinal = 0) Vec3 offset1, @Local(ordinal = 1) Vec3 offset2) {
        Level level = ((BlockEntity)te).getLevel();
        Ship ship = VSGameUtilsKt.getShipManagingPos(level, te.getPos());
        Vector3d diff;
        Vector3d vec1 = VSGameUtilsKt.toWorldCoordinates(level, VectorConversionsMCKt.toJOML(VecHelper.getCenterOf(otherPos)));
        Vector3d vec2 = VSGameUtilsKt.toWorldCoordinates(level, VectorConversionsMCKt.toJOML(VecHelper.getCenterOf(te.getPos())));
        diff = new Vector3d(vec1.sub(vec2));
        if(ship!=null) {
            Quaterniond shipQuat = new Quaterniond(ship.getTransform().getShipToWorldRotation());
            diff = diff.rotate(shipQuat.conjugate());
        }
        original.call(instance, diff.x+.5f+offset2.x, diff.y+.5f+offset2.y, diff.z+.5f+offset2.z);
    }

//    @WrapOperation(
//            method = "render",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V",
//                    ordinal = 1
//            )
//    )
//    public void translate2(PoseStack instance, double x, double y, double z, Operation<Void> original) {
//        Vector3d diff = vs_addition$vec3.sub(vs_addition$vec2);
//        if(vs_addition$shareShip!=null)
//            diff = diff.rotate(vs_addition$shareShip.getTransform().getShipToWorldRotation().conjugate(new Quaterniond()));
//        original.call(instance, diff.x+.5f, diff.y+.5f, diff.z+.5f);
//    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mrh0/createaddition/rendering/WireNodeRenderer;wireRender(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;FFFLcom/mrh0/createaddition/energy/WireType;F)V",
                    ordinal = 0
            )
    )
    public void wireRedner1(BlockEntity tileEntityIn, BlockPos other, PoseStack stack, MultiBufferSource buffer, float x, float y, float z, WireType type, float dis, Operation<Void> original,
                            @Local(ordinal = 0) IWireNode te, @Local(ordinal = 0) BlockPos otherPos, @Local(ordinal = 0) Vec3 offset1, @Local(ordinal = 1) Vec3 offset2) {
        Level level = ((BlockEntity)te).getLevel();
        Ship ship = VSGameUtilsKt.getShipManagingPos(level, te.getPos());
        Vector3d diff;
        Vector3d vec1 = VSGameUtilsKt.toWorldCoordinates(level, VectorConversionsMCKt.toJOML(VecHelper.getCenterOf(otherPos)));
        Vector3d vec2 = VSGameUtilsKt.toWorldCoordinates(level, VectorConversionsMCKt.toJOML(VecHelper.getCenterOf(te.getPos())));
        diff = new Vector3d(vec1.sub(vec2));
        if(ship!=null) {
            Quaterniond shipQuat = new Quaterniond(ship.getTransform().getShipToWorldRotation());
            diff = diff.rotate(shipQuat.conjugate());
            vs_addition$shareShip = ship;
        }
        original.call(tileEntityIn, other, stack, buffer, (float) (-diff.x+offset1.x-offset2.x), (float) (-diff.y+offset1.y-offset2.y),(float) (-diff.z+offset1.z-offset2.z), type, dis);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mrh0/createaddition/rendering/WireNodeRenderer;wireRender(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;FFFLcom/mrh0/createaddition/energy/WireType;F)V",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    public void removeShip1(T tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, CallbackInfo ci) {
        vs_addition$shareShip = null;
    }

//    @Inject(
//            method = "wireVert",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/mrh0/createaddition/rendering/WireNodeRenderer;divf(II)F",
//                    shift = At.Shift.BY,
//                    by = 2
//            ),
//            remap = false
//    )
//    private static void rotate(VertexConsumer vertBuilder, Matrix4f matrix, int light, float x, float y, float z, float a, float b, int count, int index, boolean sw, float o1, float o2, WireType type, float dis, BlockState state, PoseStack stack, int lightOffset, float hangFactor, CallbackInfo ci,
//                               @Local(ordinal = 1) LocalFloatRef fx, @Local(ordinal = 2) LocalFloatRef fy, @Local(ordinal = 3) LocalFloatRef fz) {
//        if(vs_addition$shareShip!=null){
//            Vector3d vec = new Vector3d(fx.get(), fy.get(), fz.get());
//            Quaterniond shipQuat = new Quaterniond(vs_addition$shareShip.getTransform().getShipToWorldRotation());
//            Quaterniond q_inv = new Quaterniond(shipQuat).invert();
//            Quaterniond q_y_inv = new Quaterniond(0, 1, 0, 0).invert();
//            vec.rotate(shipQuat.mul(q_inv.mul(q_y_inv)));
//            fx.set((float) vec.x);
//            fy.set((float) vec.y);
//            fz.set((float) vec.z);
//        }
//    }

//    @WrapOperation(
//            method = "wireVert",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lcom/mojang/math/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
//                    ordinal = 0
//            )
//    )
//    private static VertexConsumer vertex1(VertexConsumer instance, Matrix4f p_85983_, float p_85984_, float p_85985_, float p_85986_, Operation<VertexConsumer> original) {
//        if(vs_addition$shareShip!=null){
//            Vector3d vec = new Vector3d(p_85984_, p_85985_, p_85986_);
//            Quaterniond shipQuat = new Quaterniond(vs_addition$shareShip.getTransform().getShipToWorldRotation());
//            Vector3d yAxis = new Vector3d(0, 1, 0);
//            Vector3d rotatedYAxis = shipQuat.conjugate().transform(yAxis);
//            rotatedYAxis.y = 0;
//            rotatedYAxis.normalize();
//            vec.mul(rotatedYAxis);
//            return original.call(instance, p_85983_, (float) vec.x, (float) vec.y, (float) vec.z);
//        }
//        return original.call(instance, p_85983_, p_85984_, p_85985_, p_85986_);
//    }
//
//    @WrapOperation(
//            method = "wireVert",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lcom/mojang/math/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
//                    ordinal = 1
//            )
//    )
//    private static VertexConsumer vertex2(VertexConsumer instance, Matrix4f p_85983_, float p_85984_, float p_85985_, float p_85986_, Operation<VertexConsumer> original) {
//        if(vs_addition$shareShip!=null){
//            Vector3d vec = new Vector3d(p_85984_, p_85985_, p_85986_);
//            Quaterniond shipQuat = new Quaterniond(vs_addition$shareShip.getTransform().getShipToWorldRotation());
//            Vector3d yAxis = new Vector3d(0, 1, 0);
//            Vector3d rotatedYAxis = shipQuat.transform(yAxis);
//            rotatedYAxis.y = 0;
//            rotatedYAxis.normalize();
//            vec.mul(rotatedYAxis);
//            return original.call(instance, p_85983_, (float) vec.x, (float) vec.y, (float) vec.z);
//        }
//        return original.call(instance, p_85983_, p_85984_, p_85985_, p_85986_);
//    }

//    @WrapOperation(
//            method = "wireVert",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lcom/mojang/math/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
//                    ordinal = 2
//            )
//    )
//    private static VertexConsumer vertex3(VertexConsumer instance, Matrix4f p_85983_, float p_85984_, float p_85985_, float p_85986_, Operation<VertexConsumer> original) {
//        if(vs_addition$shareShip!=null){
//            Vector3d vec = new Vector3d(p_85984_, p_85985_, p_85986_);
//            Quaterniond shipQuat = new Quaterniond(vs_addition$shareShip.getTransform().getShipToWorldRotation());
//            Vector3d yAxis = new Vector3d(0, 1, 0);
//            Vector3d rotatedYAxis = shipQuat.transform(yAxis);
//            rotatedYAxis.y = 0;
//            rotatedYAxis.normalize();
//            vec.mul(rotatedYAxis);
//            return original.call(instance, p_85983_, (float) vec.x, (float) vec.y, (float) vec.z);
//        }
//        return original.call(instance, p_85983_, p_85984_, p_85985_, p_85986_);
//    }

//    @WrapOperation(
//            method = "wireVert",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lcom/mojang/math/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
//                    ordinal = 3
//            )
//    )
//    private static VertexConsumer vertex4(VertexConsumer instance, Matrix4f p_85983_, float p_85984_, float p_85985_, float p_85986_, Operation<VertexConsumer> original) {
//        if(vs_addition$shareShip!=null){
//            Vector3d vec = new Vector3d(p_85984_, p_85985_, p_85986_);
//            Quaterniond shipQuat = new Quaterniond(vs_addition$shareShip.getTransform().getShipToWorldRotation());
//            Vector3d yAxis = new Vector3d(0, 1, 0);
//            Vector3d rotatedYAxis = shipQuat.conjugate().transform(yAxis);
//            rotatedYAxis.y = 0;
//            rotatedYAxis.normalize();
//            vec.mul(rotatedYAxis);
//            return original.call(instance, p_85983_, (float) vec.x, (float) vec.y, (float) vec.z);
//        }
//        return original.call(instance, p_85983_, p_85984_, p_85985_, p_85986_);
//    }
//
//    @WrapOperation(
//            method = "wireVert",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lcom/mojang/math/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
//                    ordinal = 4
//            )
//    )
//    private static VertexConsumer vertex5(VertexConsumer instance, Matrix4f p_85983_, float p_85984_, float p_85985_, float p_85986_, Operation<VertexConsumer> original) {
//        if(vs_addition$shareShip!=null){
//            Vector3d vec = new Vector3d(p_85984_, p_85985_, p_85986_);
//            Quaterniond shipQuat = new Quaterniond(vs_addition$shareShip.getTransform().getShipToWorldRotation());
//            Vector3d yAxis = new Vector3d(0, 1, 0);
//            Vector3d rotatedYAxis = shipQuat.transform(yAxis);
//            rotatedYAxis.y = 0;
//            rotatedYAxis.normalize();
//            vec.mul(rotatedYAxis);
//            return original.call(instance, p_85983_, (float) vec.x, (float) vec.y, (float) vec.z);
//        }
//        return original.call(instance, p_85983_, p_85984_, p_85985_, p_85986_);
//    }

//    @WrapOperation(
//            method = "wireVert",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lcom/mojang/math/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
//                    ordinal = 5
//            )
//    )
//    private static VertexConsumer vertex6(VertexConsumer instance, Matrix4f p_85983_, float p_85984_, float p_85985_, float p_85986_, Operation<VertexConsumer> original) {
//        if(vs_addition$shareShip!=null){
//            Vector3d vec = new Vector3d(p_85984_, p_85985_, p_85986_);
//            Quaterniond shipQuat = new Quaterniond(vs_addition$shareShip.getTransform().getShipToWorldRotation());
//            Vector3d yAxis = new Vector3d(0, 1, 0);
//            Vector3d rotatedYAxis = shipQuat.transform(yAxis);
//            rotatedYAxis.y = 0;
//            rotatedYAxis.normalize();
//            vec.mul(rotatedYAxis);
//            return original.call(instance, p_85983_, (float) vec.x, (float) vec.y, (float) vec.z);
//        }
//        return original.call(instance, p_85983_, p_85984_, p_85985_, p_85986_);
//    }
}
