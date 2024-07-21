package io.github.xiewuzhiying.vs_addition.mixin.create.fan;

import com.simibubi.create.content.kinetics.fan.NozzleBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.utility.VecHelper;
import io.github.xiewuzhiying.vs_addition.util.TransformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NozzleBlockEntity.class)
public abstract class MixinNozzleBlockEntity extends SmartBlockEntity {
    public MixinNozzleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Redirect(method = {"tick","lazyTick","canSee"},at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/utility/VecHelper;getCenterOf(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;"),remap = false)
    public Vec3 transformToWorldPos(Vec3i pos){
        return TransformUtils.toWorldVec3(this.level,VecHelper.getCenterOf(pos));
    }
}
