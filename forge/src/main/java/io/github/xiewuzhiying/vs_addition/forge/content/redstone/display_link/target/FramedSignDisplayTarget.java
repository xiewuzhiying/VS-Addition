package io.github.xiewuzhiying.vs_addition.forge.content.redstone.display_link.target;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTarget;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import xfacthd.framedblocks.common.blockentity.FramedSignBlockEntity;

import java.util.List;

public class FramedSignDisplayTarget extends DisplayTarget {

    @Override
    public void acceptText(int line, List<MutableComponent> text, DisplayLinkContext context) {
        BlockEntity be = context.getTargetBlockEntity();
        if (!(be instanceof FramedSignBlockEntity sign))
            return;

        boolean changed = false;
        for (int i = 0; i < text.size() && i + line < 4; i++) {
            if (i == 0)
                reserve(i + line, sign, context);
            if (i > 0 && isReserved(i + line, sign, context))
                break;

            sign.setLine(i + line, text.get(i));
            changed = true;
        }

        if (changed)
            context.level().sendBlockUpdated(context.getTargetPos(), sign.getBlockState(), sign.getBlockState(), 2);
    }

    @Override
    public DisplayTargetStats provideStats(DisplayLinkContext context) {
        return new DisplayTargetStats(4, 15, this);
    }

}
