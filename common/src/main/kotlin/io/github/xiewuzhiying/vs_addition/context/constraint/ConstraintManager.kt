package io.github.xiewuzhiying.vs_addition.context.constraint

import io.github.xiewuzhiying.vs_addition.VSAdditionMod
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.minecraft.nbt.CompoundTag
import org.valkyrienskies.core.apigame.constraints.VSConstraint
import org.valkyrienskies.core.apigame.constraints.VSConstraintAndId
import org.valkyrienskies.core.apigame.constraints.VSConstraintId
import org.valkyrienskies.core.apigame.world.ServerShipWorldCore

abstract class ConstraintManager(open val core: ServerShipWorldCore) {

    val constraintGroups = Int2ObjectOpenHashMap<ConstraintGroup>()

    fun addConstraintGroup(group: ConstraintGroup) {
        groupIdCounter += 1
        this.constraintGroups[groupIdCounter] = group
    }

    fun removeConstraintGroup(id: Int) {
        this.constraintGroups[id]?.let { this.onRemoveConstraintGroup(id, it) }
        this.constraintGroups.remove(id)
    }

    fun removeAllConstraintGroups() {
        this.onRemoveAllConstraintGroups(this.constraintGroups)
        this.constraintGroups.forEach { (id, group) ->
            this.onRemoveConstraintGroup(id, group)
        }
        this.constraintGroups.clear()
    }

    fun writeCompoundTag(tag: CompoundTag) {
        val nbt = CompoundTag()
        constraintGroups.forEach { (id, value) ->
            nbt.put(id.toString(), getCompoundTag(value))
        }
        tag.put(ID, nbt)
    }

    fun readCompoundTag(tag: CompoundTag) {
        val nbt = tag.getCompound(ID)
        nbt.allKeys.forEach {
            constraintGroups[it.toInt()] = createFormCompoundTag(nbt.getCompound(it))
        }
    }

    open fun getCompoundTag(group: ConstraintGroup): CompoundTag {
        return group.compoundTag
    }

    open fun createFormCompoundTag(tag: CompoundTag): ConstraintGroup {
        return ConstraintGroup.createFromTag(tag)
    }

    open fun onBodiesBeDeleted(core: ServerShipWorldCore, constraintAndId: VSConstraintAndId) {

    }

    open fun onBroken(core: ServerShipWorldCore, constraintAndId: VSConstraintAndId) {

    }

    fun removeConstraint(id: Int) {
        this.core.removeConstraint(id)
    }

    fun VSConstraint.createConstraint() : VSConstraintId? {
        return core.createNewConstraint(this)
    }

    open fun onRemoveConstraintGroup(id: Int, group: ConstraintGroup) {
        group.constraintIds.forEach {
            this.removeConstraint(it)
        }
    }

    open fun onRemoveAllConstraintGroups(map: Int2ObjectOpenHashMap<ConstraintGroup>) {

    }

    companion object {
        const val ID = "${VSAdditionMod.MOD_ID}_constraint_manager"
        var groupIdCounter = 0
    }
}
