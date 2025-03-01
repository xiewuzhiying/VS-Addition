package io.github.xiewuzhiying.vs_addition.compats.vmod.schem

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.simibubi.create.AllBlocks
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity
import io.github.xiewuzhiying.vs_addition.context.airpocket.FakeAirPocketController
import io.github.xiewuzhiying.vs_addition.mixin.create.kinetics.mechanicalArm.ArmBlockEntityMixinAccessor
import io.github.xiewuzhiying.vs_addition.mixinducks.create.mechanical_arm.ArmInteractionPointMixinDuck
import io.github.xiewuzhiying.vs_addition.util.getAABBFromCenterAndExtent
import io.github.xiewuzhiying.vs_addition.util.getBodyId
import io.github.xiewuzhiying.vs_addition.util.toBlockPos
import io.github.xiewuzhiying.vs_addition.util.toVector3d
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.spaceeye.valkyrien_ship_schematics.ShipSchematic
import net.spaceeye.valkyrien_ship_schematics.containers.CompoundTagSerializable
import net.spaceeye.valkyrien_ship_schematics.interfaces.ISerializable
import net.spaceeye.vmod.compat.schem.SchemCompatItem
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.impl.shadow.FK
import org.valkyrienskies.core.impl.shadow.FW
import org.valkyrienskies.core.impl.shadow.FI
import org.valkyrienskies.core.impl.shadow.FH
import org.valkyrienskies.mod.common.shipObjectWorld

class VSAdditionSchemCompat : SchemCompatItem {

    init {
        ShipSchematic.registerCopyPasteEvents("vs_addition_compat", ::onCopyEvent, ::onPasteBeforeEvent)
    }

    fun getMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        return mapper
            .registerModule(FK())
            .registerModule(FW())
            .registerModule(FI())
            .registerModule(FH())
            .registerKotlinModule()
            .setVisibility(
                mapper.visibilityChecker
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                    .withIsGetterVisibility(JsonAutoDetect.Visibility.ANY)
                    .withSetterVisibility(JsonAutoDetect.Visibility.ANY)
            ).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    override fun onCopy(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        ships: List<ServerShip>,
        be: BlockEntity?,
        tag: CompoundTag?,
        cancelBlockCopying: () -> Unit
    ) {
        if (tag == null) { return }

        if ( state.block == AllBlocks.MECHANICAL_ARM.get() && be is ArmBlockEntity) {
            be as ArmBlockEntityMixinAccessor

            val oldInputs = ListTag()
            for (i in 0 until be.inputs.size) {
                val inputPoint = be.inputs[i]
                val tag2 = CompoundTag()
                val blockPos = inputPoint.pos
                tag2.putInt("index", i)
                tag2.putLong("shipId", level.getBodyId(blockPos))
                tag2.putLong("blockPos", blockPos.asLong())
                oldInputs.add(tag2)
            }

            val oldOutputs = ListTag()
            for (i in 0 until be.outputs.size) {
                val outputPoint = be.outputs[i]
                val tag2 = CompoundTag()
                val blockPos = outputPoint.pos
                tag2.putInt("index", i)
                tag2.putLong("shipId", level.getBodyId(blockPos))
                tag2.putLong("blockPos", blockPos.asLong())
                oldOutputs.add(tag2)
            }

            tag.put("VMOD_INJECT_old_inputs", oldInputs)
            tag.put("VMOD_INJECT_old_outputs", oldOutputs)

        }
    }

    override fun onPaste(
        level: ServerLevel,
        oldToNewId: Map<Long, Long>,
        tag: CompoundTag,
        state: BlockState,
        delayLoading: (Boolean) -> Unit,
        afterPasteCallbackSetter: ((be: BlockEntity?) -> Unit) -> Unit
    ) {
        if ( state.block == AllBlocks.MECHANICAL_ARM.get() && tag.contains("VMOD_INJECT_old_inputs") && tag.contains("VMOD_INJECT_old_outputs")) {
            val oldInputs = tag.get("VMOD_INJECT_old_inputs") as ListTag
            val oldOutputs = tag.get("VMOD_INJECT_old_outputs") as ListTag

            val newInputs = oldInputs.mapNotNull { inputPoint ->
                if (inputPoint !is CompoundTag) return@mapNotNull null
                val oldPos = (BlockPos.of(inputPoint.getLong("blockPos"))).toVector3d

                val newOtherShip = level.shipObjectWorld.allShips.getById(oldToNewId[inputPoint.getLong("shipId")]!!)!!

                val newPos = oldPos.sub(getCenterPos(oldPos.x().toInt(), oldPos.z().toInt())).add(getCenterPos(newOtherShip.chunkClaim.xMiddle * 16, newOtherShip.chunkClaim.zMiddle * 16))

                return@mapNotNull Pair(inputPoint.getInt("index"), newPos.toBlockPos)
            }

            val newOutputs = oldOutputs.mapNotNull { outputPoint ->
                if (outputPoint !is CompoundTag) return@mapNotNull null
                val oldPos = (BlockPos.of(outputPoint.getLong("blockPos"))).toVector3d

                val newOtherShip = level.shipObjectWorld.allShips.getById(oldToNewId[outputPoint.getLong("shipId")]!!)!!

                val newPos = oldPos.sub(getCenterPos(oldPos.x().toInt(), oldPos.z().toInt()))
                    .add(getCenterPos(newOtherShip.chunkClaim.xMiddle * 16, newOtherShip.chunkClaim.zMiddle * 16))

                return@mapNotNull Pair(outputPoint.getInt("index"), newPos.toBlockPos)
            }


            tag.remove("VMOD_INJECT_old_inputs")
            tag.remove("VMOD_INJECT_old_outputs")

            afterPasteCallbackSetter {
                it as ArmBlockEntityMixinAccessor
                it.inputs.forEach { arm ->
                    arm as ArmBlockEntityMixinAccessor
                    newInputs.forEach { (index, blockPos) ->
                        (arm.inputs[index] as ArmInteractionPointMixinDuck).setPos(blockPos)
                    }
                    newOutputs.forEach { (index, blockPos) ->
                        (arm.outputs[index] as ArmInteractionPointMixinDuck).setPos(blockPos)
                    }
                }
            }
        }
    }

    private fun onCopyEvent(level: ServerLevel, shipsToBeSaved: List<ServerShip>, globalMap: MutableMap<String, Any>, unregister: () -> Unit): ISerializable {
        val tag = CompoundTag()

        val tagData = ListTag()

        val mapper = getMapper()

        shipsToBeSaved
            .mapNotNull { Pair(it.id, it.getAttachment(FakeAirPocketController::class.java) ?: return@mapNotNull null) }
            .forEach {(shipId, pocketData) ->
                val item = CompoundTag()

                item.putLong("oldId", shipId)
                val attachmentData = mapper.writeValueAsBytes(pocketData)
                item.putByteArray("attachmentData", attachmentData)

                tagData.add(item)
            }

        tag.put("data", tagData)

        return CompoundTagSerializable(tag)
    }

    private fun onPasteBeforeEvent(level: ServerLevel, loadedShips: List<Pair<ServerShip, Long>>, file: ISerializable?, globalMap: MutableMap<String, Any>, unregister: () -> Unit) {
        file ?: return
        val data = CompoundTagSerializable()
        data.deserialize(file.serialize())
        val tag = data.tag ?: return

        val mapper = getMapper()

        val oldToShip = loadedShips.associate { Pair(it.second, it.first) }

        (tag["data"] as ListTag).map {
            it as CompoundTag

            val oldId = it.getLong("oldId")

            val attachment = mapper.readValue(it.getByteArray("attachmentData"), FakeAirPocketController::class.java)

            val atShip = oldToShip[oldId]!!
            attachment.map.map { (key, item) ->

                val center = item.center(org.joml.Vector3d())

                val beShip = oldToShip[oldId]!!

                val newPos = center.sub(getCenterPos(center.x().toInt(), center.z().toInt())).add(getCenterPos(beShip.chunkClaim.xMiddle * 16,beShip.chunkClaim.zMiddle * 16))

                Pair(key, getAABBFromCenterAndExtent(newPos, item.extent(org.joml.Vector3d())))
            }.forEach { (key, data) -> attachment.map[key] = data }
            atShip.saveAttachment(FakeAirPocketController::class.java, attachment)
        }
    }

    private fun getCenterPos(x: Int, z: Int) = org.joml.Vector3d((((x / 16 / 256 - 1) * 256 + 128) * 16).toDouble(), 0.0, (((z / 16 / 256) * 256 + 128) * 16).toDouble())
}