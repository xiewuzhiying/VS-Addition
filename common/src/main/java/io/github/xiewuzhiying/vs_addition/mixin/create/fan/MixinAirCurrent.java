package io.github.xiewuzhiying.vs_addition.mixin.create.fan;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.IAirCurrentSource;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import io.github.xiewuzhiying.vs_addition.util.RaycastUtils;
import io.github.xiewuzhiying.vs_addition.util.TransformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(AirCurrent.class)
public abstract class MixinAirCurrent {
    @Shadow public AABB bounds;

    @Shadow(remap = false) @Final public IAirCurrentSource source;

    @Shadow public Direction direction;
    @Shadow(remap = false) public float maxDistance;

    @Shadow(remap = false) public abstract FanProcessingType getTypeAt(float offset);

    @Shadow(remap = false) protected List<Pair<TransportedItemStackHandlerBehaviour, FanProcessingType>> affectedItemHandlers;
    @Unique public Vec3 min;
    @Unique public Vec3 max;
    @Unique public AABB aabb;
    @ModifyExpressionValue(method = "tickAffectedEntities",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;intersects(Lnet/minecraft/world/phys/AABB;)Z"))
    public boolean removeAABBDetect(boolean original){
        return true;
    }
    @Redirect(method = "rebuild",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;getAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;"), remap = false)
    public FanProcessingType getAtInWorld(Level level, BlockPos pos){
        Ship ship = VSGameUtilsKt.getShipManagingPos(level,this.source.getAirCurrentPos());
        if(ship!=null && level.getBlockState(pos).isAir()){
            BlockPos newPos = BlockPos.containing(TransformUtils.toWorldVec3(ship, pos.getCenter()));
            FanProcessingType type = FanProcessingType.getAt(level,newPos);
            if(!(type.equals(AllFanProcessingTypes.NONE))){
                return type;
            }
        }
        Vec3 vec3 = TransformUtils.toWorldVec3(level,pos.getCenter());
        List<Vector3d> vector3dList = VSGameUtilsKt.transformToNearbyShipsAndWorld(level,vec3.x,vec3.y,vec3.z,0.25);
        if(!vector3dList.isEmpty()){
            ship = VSGameUtilsKt.getShipManagingPos(level, vector3dList.get(0));
            if (ship != null) {
                BlockPos newPos = BlockPos.containing(TransformUtils.toShipyardCoordinates(ship, vec3));
                FanProcessingType type = FanProcessingType.getAt(level,newPos);
                if(!(type.equals(AllFanProcessingTypes.NONE))){
                    return type;
                }
            }
        }
        return FanProcessingType.getAt(level,pos);
    }
    @Redirect(method = "tickAffectedEntities",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/utility/VecHelper;getCenterOf(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;"), remap = false)
    public Vec3 transformPosToWorld(Vec3i pos, @Local(argsOnly = true) Level world){
        return TransformUtils.toWorldVec3(world,VecHelper.getCenterOf(pos));
    }
    @Redirect(method = "tickAffectedEntities",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    public void harvester(Entity instance, Vec3 pDeltaMovement,@Local(ordinal = 2) float acceleration,@Local Vec3i flow){
        Level level = this.source.getAirCurrentWorld();
        Ship ship = VSGameUtilsKt.getShipManagingPos(level,this.source.getAirCurrentPos());
        if(ship!=null){
            Vector3d tempVec = new Vector3d();
            ship.getTransform().getShipToWorld().transformDirection(flow.getX(), flow.getY(), flow.getZ(), tempVec);
            Vec3 transformedFlow = VectorConversionsMCKt.toMinecraft(tempVec);
            Vec3 previousMotion = instance.getDeltaMovement();
            double xIn = Mth.clamp(transformedFlow.x * (double)acceleration - previousMotion.x, -5.0, 5.0);
            double yIn = Mth.clamp(transformedFlow.y * (double)acceleration - previousMotion.y, -5.0, 5.0);
            double zIn = Mth.clamp(transformedFlow.z * (double)acceleration - previousMotion.z, -5.0, 5.0);
            instance.setDeltaMovement(previousMotion.add((new Vec3(xIn, yIn, zIn)).scale(0.125)));
        }else {
            instance.setDeltaMovement(pDeltaMovement);
        }
    }
    @Redirect(method = "findEntities",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
    public List<Entity> findWorldEntities(Level instance, Entity entity, AABB originalAABB){
        if(VSGameUtilsKt.getShipManagingPos(this.source.getAirCurrentWorld(),this.source.getAirCurrentPos())!=null){
            transformWorldAABB();
            return clipEntities(instance,min,max);
        }
        return instance.getEntities(entity,originalAABB);
    }
    @Inject(method = "findAffectedHandlers",at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V",shift = At.Shift.AFTER), remap = false)
    public void findWorldAffectedHandlers(CallbackInfo ci,@Local Level level,@Local BlockPos start){
        int limit = (int) (this.maxDistance+1);
        Ship ship = VSGameUtilsKt.getShipManagingPos(level,start);
        for(int i = 1; i <= limit; ++i){
            BlockPos currentPos = start.relative(this.direction,i);
            Vec3 currentVec3 = TransformUtils.toWorldVec3(level,currentPos.getCenter());
            for(Direction direction:Direction.values()){
                Vec3i vec3i = direction.getNormal();
                Vec3 directionVec3 = new Vec3(vec3i.getX(),vec3i.getY(),vec3i.getZ()).scale(1.25);
                if(ship!=null){
                    Vector3d vector3d = new Vector3d();
                    ship.getShipToWorld().transformDirection(vec3i.getX(),vec3i.getY(),vec3i.getZ(),vector3d);
                    directionVec3 = VectorConversionsMCKt.toMinecraft(vector3d);
                }
                BlockHitResult blockHitResult = level.clip(new ClipContext(currentVec3,currentVec3.add(directionVec3), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,null));
                BlockPos pos = blockHitResult.getBlockPos();
                FanProcessingType segmentType = this.getTypeAt((float) (i - 1));
                TransportedItemStackHandlerBehaviour behaviour = BlockEntityBehaviour.get(level, pos, TransportedItemStackHandlerBehaviour.TYPE);
                if (behaviour != null){
                    Ship behaviourShip = VSGameUtilsKt.getShipManagingPos(level,pos);
                    if(behaviourShip!=null){
                        Vector3d upVector3d = new Vector3d();
                        behaviourShip.getShipToWorld().transformDirection(0,1,0,upVector3d);
                        Vec3 vec3 = VectorConversionsMCKt.toMinecraft(upVector3d);
                        double theta = vec3.dot(directionVec3)/(vec3.length()*directionVec3.length());
                        double thetaDeg = Math.toDegrees(Math.acos(theta));
                        if(thetaDeg>165)
                        {
                            FanProcessingType type = FanProcessingType.getAt(level, pos);
                            if (type.equals(AllFanProcessingTypes.NONE)) {
                                type = segmentType;
                            }
                            this.affectedItemHandlers.add(Pair.of(behaviour, type));
                        }
                    }
                }
            }
        }
    }
    @Redirect(method = "rebuild",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/fan/AirCurrent;getFlowLimit(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;FLnet/minecraft/core/Direction;)F"), remap = false)
    private float clipLimitWithCatalyst(Level world, BlockPos start, float max, Direction facing){
        Ship ship = VSGameUtilsKt.getShipManagingPos(world, start);
        if (ship != null) {
            Vector3d startVec = ship.getTransform().getShipToWorld().transformPosition(new Vector3d((double)start.getX() + 0.5, (double)start.getY() + 0.5, (double)start.getZ() + 0.5));
            Vector3d direction = ship.getTransform().getShipToWorld().transformDirection(VectorConversionsMCKt.toJOMLD(facing.getNormal()));
            startVec.add(direction.x, direction.y, direction.z);
            direction.mul(max);
            Vec3 mcStart = VectorConversionsMCKt.toMinecraft(startVec);
            BlockHitResult result = RaycastUtils.clipIncludeShips(world, new ClipContext(mcStart, VectorConversionsMCKt.toMinecraft(startVec.add(direction.x, direction.y, direction.z)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null));
            return (float)result.getLocation().distanceTo(mcStart);
        } else {
            BlockPos end = start.relative(facing, (int)max);
            if (VSGameUtilsKt.getShipsIntersecting(world, new AABB(start.getX(), start.getY(), start.getZ(), (double)end.getX() + 1.0, (double)end.getY() + 1.0, (double)end.getZ() + 1.0)).iterator().hasNext()) {
                Vec3 centerStart = Vec3.atCenterOf(start);
                BlockHitResult result = RaycastUtils.clipIncludeShips(world, new ClipContext(centerStart.add(facing.getStepX(), facing.getStepY(), facing.getStepZ()), Vec3.atCenterOf(end), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null));
                return (float)result.getLocation().distanceTo(centerStart);
            }
        }
        return max;
    }
    @Unique
    public List<Entity> clipEntities(Level level,Vec3 start,Vec3 end){
        List<Entity> entityList = level.getEntities(null,aabb);
        List<Entity> entities = new ArrayList<>();
        for(Entity entity:entityList){
            AABB entityAABB = entity.getBoundingBox();
            if(entity instanceof ItemEntity){
                entityAABB.inflate(0.75);
            }
            Optional<Vec3> hitVec3 = entityAABB.clip(start,end);
            if(hitVec3.isPresent()){
                entities.add(entity);
            }
        }
        return entities;
    }
    @Unique
    public void transformWorldAABB(){
        Ship ship = VSGameUtilsKt.getShipManagingPos(this.source.getAirCurrentWorld(),this.source.getAirCurrentPos());
        if(ship!=null){
            min = TransformUtils.toWorldVec3(ship,this.source.getAirCurrentPos().getCenter());
            Vector3d directionVec = ship.getTransform().getShipToWorld().transformDirection(VectorConversionsMCKt.toJOMLD(this.direction.getNormal())).mul(this.maxDistance+1F);
            max = min.add(VectorConversionsMCKt.toMinecraft(directionVec));
            this.aabb = VSGameUtilsKt.transformAabbToWorld(this.source.getAirCurrentWorld(),this.bounds).inflate(1);
        }
    }
}