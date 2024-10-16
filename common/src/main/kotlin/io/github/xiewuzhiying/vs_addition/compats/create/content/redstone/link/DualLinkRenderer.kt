package io.github.xiewuzhiying.vs_addition.compats.create.content.redstone.link

import com.mojang.blaze3d.vertex.PoseStack
import com.simibubi.create.CreateClient
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBox
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxRenderer
import com.simibubi.create.foundation.utility.Iterate
import com.simibubi.create.foundation.utility.Lang
import com.simibubi.create.foundation.utility.VecHelper
import com.simibubi.create.infrastructure.config.AllConfigs
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import org.valkyrienskies.mod.common.toShipRenderCoordinates


object DualLinkRenderer {
    @JvmStatic
    fun tick() {
        val mc = Minecraft.getInstance()
        val world = mc.level ?: return
        val target = mc.hitResult
        if (target == null || target !is BlockHitResult) return

        val result = target
        val pos = result.blockPos

        val behaviour = BlockEntityBehaviour.get(world, pos, DualLinkBehaviour.TYPE)
            ?: return

        val freq1: Component = Lang.translateDirect("logistics.firstFrequency")
        val freq2: Component = Lang.translateDirect("logistics.secondFrequency")

        for (first in Iterate.trueAndFalse) {
            val bb = AABB(Vec3.ZERO, Vec3.ZERO).inflate(.25)
            val label: Component = if (first) freq1 else freq2
            val hit = behaviour.testHit(first, target.getLocation())
            val transform = if (first) behaviour.firstSlot else behaviour.secondSlot

            val box = ValueBox(label, bb, pos).passive(!hit)
            val empty = behaviour.networkKey[first]
                .stack
                .isEmpty

            if (!empty) box.wideOutline()

            CreateClient.OUTLINER.showValueBox(Pair(first, pos), box.transform(transform))
                .highlightFace(result.direction)

            if (!hit) continue

            val tip: MutableList<MutableComponent> = ArrayList()
            tip.add(label.copy())
            tip.add(
                Lang.translateDirect(if (empty) "logistics.filter.click_to_set" else "logistics.filter.click_to_replace")
            )
            CreateClient.VALUE_SETTINGS_HANDLER.showHoverTip(tip)
        }
    }

    @JvmStatic
    fun renderOnBlockEntity(
        be: SmartBlockEntity?, partialTicks: Float, ms: PoseStack,
        buffer: MultiBufferSource?, light: Int, overlay: Int
    ) {
        if (be == null || be.isRemoved) return

        val cameraEntity: Entity = Minecraft.getInstance().cameraEntity!!
        val max = AllConfigs.client().filterItemRenderDistance.f
        val result: Vec3 = Minecraft.getInstance().level.toShipRenderCoordinates(VecHelper.getCenterOf(be.blockPos),
            cameraEntity.position())
        if ((!be.isVirtual) && result
                .distanceToSqr(VecHelper.getCenterOf(be.blockPos)) > (max * max)
        ) return

        val behaviour = be.getBehaviour(DualLinkBehaviour.TYPE)
            ?: return

        for (first in Iterate.trueAndFalse) {
            val transform = if (first) behaviour.firstSlot else behaviour.secondSlot
            val stack = if (first) behaviour.frequencyFirst.stack else behaviour.frequencyLast.stack

            ms.pushPose()
            transform.transform(be.blockState, ms)
            ValueBoxRenderer.renderItemIntoValueBox(stack, ms, buffer, light, overlay)
            ms.popPose()
        }
    }
}
