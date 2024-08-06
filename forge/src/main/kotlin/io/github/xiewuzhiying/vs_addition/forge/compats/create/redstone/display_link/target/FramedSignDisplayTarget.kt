package io.github.xiewuzhiying.vs_addition.forge.compats.create.redstone.display_link.target

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext
import com.simibubi.create.content.redstone.displayLink.target.DisplayTarget
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats
import net.minecraft.network.chat.MutableComponent
import xfacthd.framedblocks.common.blockentity.FramedSignBlockEntity

class FramedSignDisplayTarget : DisplayTarget() {
    override fun acceptText(line: Int, text: List<MutableComponent>, context: DisplayLinkContext) {
        val be = context.targetBlockEntity as? FramedSignBlockEntity ?: return

        var changed = false
        var i = 0
        while (i < text.size && i + line < 4) {
            if (i == 0) reserve(i + line, be, context)
            if (i > 0 && isReserved(i + line, be, context)) break

            be.setLine(i + line, text[i])
            changed = true
            i++
        }

        if (changed) context.level().sendBlockUpdated(context.targetPos, be.blockState, be.blockState, 2)
    }

    override fun provideStats(context: DisplayLinkContext): DisplayTargetStats {
        return DisplayTargetStats(4, 15, this)
    }
}
