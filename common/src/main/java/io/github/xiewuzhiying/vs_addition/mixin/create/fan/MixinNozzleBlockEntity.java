package io.github.xiewuzhiying.vs_addition.mixin.create.fan;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.content.kinetics.fan.NozzleBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import io.github.xiewuzhiying.vs_addition.stuff.EncasedFanConditionTester;
import io.github.xiewuzhiying.vs_addition.util.TransformUtilsKt;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Restriction(
        require = {
                @Condition(type = Condition.Type.TESTER, tester = EncasedFanConditionTester.class)
        }
)
@Mixin(NozzleBlockEntity.class)
public abstract class MixinNozzleBlockEntity extends SmartBlockEntity {
    public MixinNozzleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @ModifyExpressionValue(method = {"tick","lazyTick","canSee"},at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/utility/VecHelper;getCenterOf(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 transformToWorldPos(Vec3 original){
        return TransformUtilsKt.toWorld(original, this.level);
    }
}
