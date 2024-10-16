package io.xiewuzhiying.vs_addition.fabric.compats.create.content.redstone.link

import com.simibubi.create.AllItems
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
import com.simibubi.create.foundation.utility.AdventureUtil
import com.simibubi.create.foundation.utility.RaycastHelper
import net.fabricmc.fabric.api.entity.FakePlayer
import io.github.xiewuzhiying.vs_addition.compats.create.content.redstone.link.DualLinkBehaviour
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

object DualLinkHandler {
    @JvmStatic
    fun onBlockActivated(
        player: Player,
        world: Level,
        hand: InteractionHand,
        blockRayTraceResult: BlockHitResult
    ): InteractionResult {
        val pos = blockRayTraceResult.blockPos
        if (player.isShiftKeyDown || player.isSpectator) return InteractionResult.PASS

        val behaviour = BlockEntityBehaviour.get(world, pos, DualLinkBehaviour.TYPE)
            ?: return InteractionResult.PASS
        if (AdventureUtil.isAdventure(player)) return InteractionResult.PASS

        val heldItem = player.getItemInHand(hand)
        val ray = RaycastHelper.rayTraceRange(world, player, 10.0)
            ?: return InteractionResult.PASS
        if (AllItems.LINKED_CONTROLLER.isIn(heldItem)) return InteractionResult.PASS
        if (AllItems.WRENCH.isIn(heldItem)) return InteractionResult.PASS

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

        for (first in mutableListOf(false, true)) {
            if (behaviour.testHit(first, ray.location) || fakePlayer && fakePlayerChoice == first) {
                if (!world.isClientSide) behaviour.setFrequency(first, heldItem)
                world.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .25f, .1f)
                return InteractionResult.SUCCESS
            }
        }
        return InteractionResult.PASS
    }
}