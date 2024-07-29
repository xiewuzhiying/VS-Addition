package io.github.xiewuzhiying.vs_addition.util;

import com.simibubi.create.AllTags;
import io.github.xiewuzhiying.vs_addition.mixin.minecraft.HitResultAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.core.api.ships.LoadedShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.core.api.ships.properties.ShipIdKt;
import org.valkyrienskies.core.api.ships.properties.ShipTransform;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.util.Iterator;
import java.util.Optional;

import static net.minecraft.world.level.BlockGetter.traverseBlocks;

public class RaycastUtils {
    public static BlockHitResult clipIncludeShips(@NotNull Level $this$clipIncludeShips, @NotNull ClipContext ctx) {
        BlockHitResult vanillaHit = clip($this$clipIncludeShips, ctx,ctx.getFrom(),ctx.getTo());
        VSGameUtilsKt.getShipObjectWorld($this$clipIncludeShips);
        BlockHitResult closestHit = vanillaHit;
        Vec3 closestHitPos = vanillaHit.getLocation();
        double closestHitDist = closestHitPos.distanceToSqr(ctx.getFrom());
        Vec3 var10002 = ctx.getFrom();
        Vector3dc var24 = VectorConversionsMCKt.toJOML(var10002);
        Vec3 var10003 = ctx.getTo();
        AABBd var10000 = (new AABBd(var24, VectorConversionsMCKt.toJOML(var10003))).correctBounds();
        Iterator var10 = VSGameUtilsKt.getShipObjectWorld($this$clipIncludeShips).getLoadedShips().getIntersecting(var10000).iterator();
        while(true) {
            LoadedShip ship;
            if (!var10.hasNext()) {
                return new BlockHitResult(closestHitPos,closestHit.getDirection(),closestHit.getBlockPos(),closestHit.isInside());
            }
            ship = (LoadedShip)var10.next();
            ClientShip var21;
            ShipTransform var22;
            Matrix4dc var23;
            label52: {
                var21 = ship instanceof ClientShip ? (ClientShip)ship : null;
                if ((ship instanceof ClientShip ? (ClientShip)ship : null) != null) {
                    var22 = var21.getRenderTransform();
                    if (var22 != null) {
                        var23 = var22.getWorldToShipMatrix();
                        if (var23 != null) {
                            break label52;
                        }
                    }
                }
                var23 = ship.getWorldToShip();
            }
            Matrix4dc worldToShip;
            label59: {
                worldToShip = var23;
                var21 = ship instanceof ClientShip ? (ClientShip)ship : null;
                if ((ship instanceof ClientShip ? (ClientShip)ship : null) != null) {
                    var22 = var21.getRenderTransform();
                    if (var22 != null) {
                        var23 = var22.getShipToWorldMatrix();
                        if (var23 != null) {
                            break label59;
                        }
                    }
                }
                var23 = ship.getShipToWorld();
            }
            Matrix4dc shipToWorld = var23;
            Vec3 var10001 = ctx.getFrom();
            Vector3d var25 = worldToShip.transformPosition(VectorConversionsMCKt.toJOML(var10001));
            Vec3 shipStart = VectorConversionsMCKt.toMinecraft(var25);
            var10001 = ctx.getTo();
            var25 = worldToShip.transformPosition(VectorConversionsMCKt.toJOML(var10001));
            Vec3 shipEnd = VectorConversionsMCKt.toMinecraft(var25);
            BlockHitResult shipHit = clip($this$clipIncludeShips,ctx, shipStart, shipEnd);
            var10001 = shipHit.getLocation();
            var25 = shipToWorld.transformPosition(VectorConversionsMCKt.toJOML(var10001));
            Vec3 shipHitPos = VectorConversionsMCKt.toMinecraft(var25);
            double shipHitDist = shipHitPos.distanceToSqr(ctx.getFrom());
            if (shipHitDist < closestHitDist && shipHit.getType() != HitResult.Type.MISS) {
                closestHit = shipHit;
                closestHitPos = shipHitPos;
                closestHitDist = shipHitDist;
            }
        }
    }
    private static BlockHitResult clip(Level level,ClipContext context, Vec3 realStart, Vec3 realEnd) {
        return traverseBlocks(realStart,realEnd,context,(context1, blockPos)->{
            BlockState blockState = level.getBlockState(blockPos);
            if(AllTags.AllBlockTags.FAN_TRANSPARENT.matches(blockState)){
                return null;
            }
            FluidState var17 = level.getFluidState(blockPos);
            VoxelShape voxelShape = context1.getBlockShape(blockState, level, blockPos);
            BlockHitResult blockHitResult = level.clipWithInteractionOverride(realStart, realEnd, blockPos, voxelShape, blockState);
            VoxelShape voxelShape2 = context.getFluidShape(var17, level, blockPos);
            BlockHitResult blockHitResult2 = voxelShape2.clip(realStart, realEnd, blockPos);
            double d = blockHitResult == null ? Double.MAX_VALUE : realStart.distanceToSqr(blockHitResult.getLocation());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : realEnd.distanceToSqr(blockHitResult2.getLocation());
            return d <= e ? blockHitResult : blockHitResult2;
        },(context1)->{
            Vec3 start = context1.getFrom();
            Vec3 end = context1.getTo();
            Vec3 vec3d = start.subtract(end);
            return BlockHitResult.miss(end, Direction.getNearest(vec3d.x, vec3d.y, vec3d.z), BlockPos.containing(end));
        });
    }

    public static BlockHitResult isBlockInLineIncludeShips(Level level, ClipBlockStateContext clipBlockStateContext) {
        return isBlockInLineIncludeShips(level, clipBlockStateContext, true);
    }

    public static BlockHitResult isBlockInLineIncludeShips(Level level, ClipBlockStateContext clipBlockStateContext, boolean shouldTransformHitPos) {
        return isBlockInLineIncludeShips(level, clipBlockStateContext, shouldTransformHitPos, null);
    }

    public static BlockHitResult isBlockInLineIncludeShips(Level level, ClipBlockStateContext clipBlockStateContext, boolean shouldTransformHitPos, Optional<Long> skipShip) {
        BlockHitResult vanillaHit = isBlockInLine(level, clipBlockStateContext);

        if (VSGameUtilsKt.getShipObjectWorld(level) == null) {
            Logger logger = LogManager.getLogger("RaycastUtilsKt");
            logger.error(
                    "shipObjectWorld was empty for level raytrace, this should not be possible! " +
                            "Returning vanilla result."
            );
            return vanillaHit;
        }

        BlockHitResult closestHit = vanillaHit;
        Vec3 closestHitPos = vanillaHit.getLocation();
        double closestHitDist = closestHitPos.distanceToSqr(clipBlockStateContext.getFrom());

        AABBdc clipAABB = new AABBd(VectorConversionsMCKt.toJOML(clipBlockStateContext.getFrom()), VectorConversionsMCKt.toJOML(clipBlockStateContext.getTo())).correctBounds();


        for(Ship ship: VSGameUtilsKt.getShipsIntersecting(level, clipAABB)) {
            if (skipShip.get() == ship.getId()) {
                continue;
            }

            final Matrix4dc worldToShip;
            if(ship instanceof ClientShip clientShip) {
                worldToShip = clientShip.getRenderTransform().getWorldToShip();
            } else {
                worldToShip = ship.getWorldToShip();
            }

            final Matrix4dc shipToWorld;
            if(ship instanceof ClientShip clientShip) {
                shipToWorld = clientShip.getRenderTransform().getShipToWorld();
            } else {
                shipToWorld = ship.getShipToWorld();
            }

            final Vec3 shipStart = VectorConversionsMCKt.toMinecraft(worldToShip.transformPosition(VectorConversionsMCKt.toJOML(clipBlockStateContext.getFrom())));
            final Vec3 shipEnd = VectorConversionsMCKt.toMinecraft(worldToShip.transformPosition(VectorConversionsMCKt.toJOML(clipBlockStateContext.getTo())));

            BlockHitResult shipHit = isBlockInLine(level, new ClipBlockStateContext(shipStart, shipEnd, clipBlockStateContext.isTargetBlock()));
            Vec3 shipHitPos = VectorConversionsMCKt.toMinecraft(shipToWorld.transformPosition(VectorConversionsMCKt.toJOML(shipHit.getLocation())));
            double shipHitDist = shipHitPos.distanceToSqr(clipBlockStateContext.getFrom());

            if (shipHitDist < closestHitDist && shipHit.getType() != HitResult.Type.MISS) {
                closestHit = shipHit;
                closestHitPos = shipHitPos;
                closestHitDist = shipHitDist;
            }
        }

        if (shouldTransformHitPos) {
            ((HitResultAccessor)closestHit).setLocation(closestHitPos);
        }

        return closestHit;
    }

    private static BlockHitResult isBlockInLine(BlockGetter blockGetter, ClipBlockStateContext arg) {
        return (BlockHitResult)traverseBlocks(arg.getFrom(), arg.getTo(), arg, (argx, arg2) -> {
            BlockState blockstate = blockGetter.getBlockState(arg2);
            Vec3 vec3 = argx.getFrom().subtract(argx.getTo());
            return argx.isTargetBlock().test(blockstate) ? new BlockHitResult(argx.getTo(), Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(argx.getTo()), false) : null;
        }, (argx) -> {
            Vec3 vec3 = argx.getFrom().subtract(argx.getTo());
            return BlockHitResult.miss(argx.getTo(), Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(argx.getTo()));
        });
    }
}
