package io.github.xiewuzhiying.vs_addition.mixin.vs_clockwork.fan;

import com.bawnorton.mixinsquared.TargetHandler;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.valkyrienskies.clockwork.content.propulsion.singleton.fan.EncasedFanCreateData;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

@Pseudo
@Mixin(value = EncasedFanBlockEntity.class, priority = 1500)
public abstract class MixinEncasedFanBlockEntity extends KineticBlockEntity {
    public MixinEncasedFanBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @TargetHandler(
            mixin = "org.valkyrienskies.clockwork.mixin.content.fan.MixinEncasedFanTileEntity",
            name = "Lorg/valkyrienskies/clockwork/mixin/content/fan/MixinEncasedFanTileEntity;vs_clockwork$handleController()V"
    )
    @ModifyVariable(
            method = "@MixinSquared:Handler",
            at = @At("STORE"),
            ordinal = 0
    )
    private EncasedFanCreateData injected(EncasedFanCreateData data) {
        Direction direction = this.getBlockState().getValue(BlockStateProperties.FACING);
        if(direction == Direction.EAST || direction == Direction.SOUTH || direction == Direction.UP) {
            Vector3dc pos = VectorConversionsMCKt.toJOMLD(this.worldPosition);
            Vector3dc axis = VectorConversionsMCKt.toJOMLD(((Direction)this.getBlockState().getValue(BlockStateProperties.FACING)).getNormal()).negate();
            return new EncasedFanCreateData(pos, axis, (double)this.speed);
        }
        return data;
    }
}
