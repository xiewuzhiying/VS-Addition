package io.github.xiewuzhiying.vs_addition.forge.compats.create.content.redstone.link

import com.simibubi.create.AllItems
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
import com.simibubi.create.foundation.utility.RaycastHelper
import io.github.xiewuzhiying.vs_addition.compats.create.content.redstone.link.DualLinkBehaviour
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.phys.Vec3
import net.minecraftforge.common.util.FakePlayer
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.LogicalSide

object DualLinkHandler {
    @JvmStatic
    fun onBlockActivated(event: PlayerInteractEvent.RightClickBlock) {
        val world = event.level
        val pos = event.pos
        val player = event.entity
        val hand = event.hand

        if (player.isShiftKeyDown || player.isSpectator) return

        val behaviour = BlockEntityBehaviour.get(world, pos, DualLinkBehaviour.TYPE)
            ?: return

        val heldItem = player.getItemInHand(hand)
        val ray = RaycastHelper.rayTraceRange(world, player, 10.0) ?: return
        if (AllItems.LINKED_CONTROLLER.isIn(heldItem)) return
        if (AllItems.WRENCH.isIn(heldItem)) return

        val fakePlayer = player is FakePlayer
        var fakePlayerChoice = false

        if (fakePlayer) {
            val blockState = world.getBlockState(pos)
            val localHit = ray.location
                .subtract(Vec3.atLowerCornerOf(pos))
                .add(
                    Vec3.atLowerCornerOf(
                        ray.direction
                            .normal
                    )
                        .scale(.25)
                )
            fakePlayerChoice = localHit.distanceToSqr(behaviour.firstSlot.getLocalOffset(blockState)) > localHit
                .distanceToSqr(behaviour.secondSlot.getLocalOffset(blockState))
        }

        for (first in mutableListOf<Boolean>(false, true)) {
            if (behaviour.testHit(first, ray.location) || fakePlayer && fakePlayerChoice == first) {
                if (event.side != LogicalSide.CLIENT) behaviour.setFrequency(first, heldItem)
                event.isCanceled = true
                event.cancellationResult = InteractionResult.SUCCESS
                world.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .25f, .1f)
            }
        }
    }
}
