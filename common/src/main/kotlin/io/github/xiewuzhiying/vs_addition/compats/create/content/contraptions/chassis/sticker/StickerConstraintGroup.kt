package io.github.xiewuzhiying.vs_addition.compats.create.content.contraptions.chassis.sticker

import io.github.xiewuzhiying.vs_addition.context.constraint.ConstraintGroup
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import org.valkyrienskies.core.apigame.constraints.VSConstraintId

open class StickerConstraintGroup(override val constraintIds: Iterable<VSConstraintId>, open val blockPos: BlockPos) : ConstraintGroup(constraintIds) {
    override val compoundTag : CompoundTag
        get() {
            val tag = super.compoundTag
            tag.putLong("blockPos", blockPos.asLong())
            return tag
        }

    companion object {
        @JvmStatic
        fun createFromTag(tag: CompoundTag): StickerConstraintGroup {
            return StickerConstraintGroup(getConstraintsFromTag(tag), BlockPos.of(tag.getLong("blockPos")))
        }
    }
}