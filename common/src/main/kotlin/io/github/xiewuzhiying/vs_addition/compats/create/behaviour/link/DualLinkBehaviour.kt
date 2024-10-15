package io.github.xiewuzhiying.vs_addition.compats.create.behaviour.link

import com.simibubi.create.Create
import com.simibubi.create.content.equipment.clipboard.ClipboardCloneable
import com.simibubi.create.content.redstone.link.IRedstoneLinkable
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform
import com.simibubi.create.foundation.utility.Couple
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import org.apache.commons.lang3.tuple.Pair
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.util.toJOML
import org.valkyrienskies.mod.common.util.toMinecraft
import java.util.function.Function
import java.util.function.IntConsumer
import java.util.function.IntSupplier


open class DualLinkBehaviour protected constructor(
    be: SmartBlockEntity?,
    slots: Pair<ValueBoxTransform?, ValueBoxTransform?>
) :
    BlockEntityBehaviour(be), IRedstoneLinkable,
    ClipboardCloneable {
    internal enum class Mode {
        TRANSMIT, RECEIVE
    }

    var frequencyFirst: RedstoneLinkNetworkHandler.Frequency
    var frequencyLast: RedstoneLinkNetworkHandler.Frequency
    var firstSlot: ValueBoxTransform
    var secondSlot: ValueBoxTransform
    var textShift: Vec3

    var newPosition: Boolean = true
    private var mode: Mode? = null
    private var transmission: IntSupplier? = null
    private var signalCallback: IntConsumer? = null

    init {
        frequencyFirst = RedstoneLinkNetworkHandler.Frequency.EMPTY
        frequencyLast = RedstoneLinkNetworkHandler.Frequency.EMPTY
        firstSlot = slots.left!!
        secondSlot = slots.right!!
        textShift = Vec3.ZERO
    }

    fun moveText(shift: Vec3): DualLinkBehaviour {
        textShift = shift
        return this
    }

    fun copyItemsFrom(behaviour: DualLinkBehaviour?) {
        if (behaviour == null) return
        frequencyFirst = behaviour.frequencyFirst
        frequencyLast = behaviour.frequencyLast
    }

    override fun isListening(): Boolean {
        return mode == Mode.RECEIVE
    }

    override fun getTransmittedStrength(): Int {
        return if (mode == Mode.TRANSMIT) transmission!!.asInt else 0
    }

    override fun setReceivedStrength(networkPower: Int) {
        if (!newPosition) return
        signalCallback!!.accept(networkPower)
    }

    fun notifySignalChange() {
        Create.REDSTONE_LINK_NETWORK_HANDLER.updateNetworkOf(world, this)
    }

    override fun initialize() {
        super.initialize()
        if (world.isClientSide) return
        handler.addToNetwork(world, this)
        newPosition = true
    }

    override fun getNetworkKey(): Couple<RedstoneLinkNetworkHandler.Frequency> {
        return Couple.create(frequencyFirst, frequencyLast)
    }

    override fun unload() {
        super.unload()
        if (world.isClientSide) return
        handler.removeFromNetwork(world, this)
    }

    override fun isSafeNBT(): Boolean {
        return true
    }

    override fun write(nbt: CompoundTag, clientPacket: Boolean) {
        super.write(nbt, clientPacket)
        nbt.put(
            "DualFrequencyFirst", frequencyFirst.stack
                .save(CompoundTag())
        )
        nbt.put(
            "DualFrequencyLast", frequencyLast.stack
                .save(CompoundTag())
        )
        nbt.putLong(
            "DualLastKnownPosition", blockEntity.blockPos
                .asLong()
        )
    }

    override fun read(nbt: CompoundTag, clientPacket: Boolean) {
        val positionInTag = blockEntity.blockPos
            .asLong()
        val positionKey = nbt.getLong("DualLastKnownPosition")
        newPosition = positionInTag != positionKey

        super.read(nbt, clientPacket)
        frequencyFirst = RedstoneLinkNetworkHandler.Frequency.of(ItemStack.of(nbt.getCompound("DualFrequencyFirst")))
        frequencyLast = RedstoneLinkNetworkHandler.Frequency.of(ItemStack.of(nbt.getCompound("DualFrequencyLast")))
    }

    fun setFrequency(first: Boolean, stack: ItemStack) {
        val stack2 = stack.copy()
        stack2.count = 1
        val toCompare = if (first) frequencyFirst.stack else frequencyLast.stack
        val changed = !ItemStack.isSameItemSameTags(stack2, toCompare)

        if (changed) handler.removeFromNetwork(world, this)

        if (first) frequencyFirst = RedstoneLinkNetworkHandler.Frequency.of(stack2)
        else frequencyLast = RedstoneLinkNetworkHandler.Frequency.of(stack2)

        if (!changed) return

        blockEntity.sendData()
        handler.addToNetwork(world, this)
    }

    override fun getType(): BehaviourType<*> {
        return TYPE
    }

    private val handler: RedstoneLinkNetworkHandler
        get() = Create.REDSTONE_LINK_NETWORK_HANDLER

    class SlotPositioning(
        var offsets: Function<BlockState, org.apache.commons.lang3.tuple.Pair<Vec3, Vec3>>,
        var rotation: Function<BlockState, Vec3>
    ) {
        var scale: Float = 1f

        fun scale(scale: Float): SlotPositioning {
            this.scale = scale
            return this
        }
    }

    fun testHit(first: Boolean, hit: Vec3): Boolean {
        val state = blockEntity.blockState

        val level: Level? = this.world
        var pos1 = hit
        var pos2 = Vec3.atLowerCornerOf(blockEntity.blockPos)

        if (level != null) {
            val ship1: Ship? = level.getShipManagingPos(pos1)
            val ship2: Ship? = level.getShipManagingPos(pos2)
            if (ship1 != null && ship2 == null) {
                pos2 = ship1.worldToShip.transformPosition(pos2.toJOML()).toMinecraft()
            } else if (ship1 == null && ship2 != null) {
                pos1 = ship2.worldToShip.transformPosition(pos1.toJOML()).toMinecraft()
            }
        }

        val localHit = pos1.subtract(pos2)
        return (if (first) firstSlot else secondSlot).testHit(state, localHit)
    }

    override fun isAlive(): Boolean {
        val level: Level = world
        val pos = pos
        if (blockEntity.isChunkUnloaded) return false
        if (blockEntity.isRemoved) return false
        if (!level.isLoaded(pos)) return false
        return level.getBlockEntity(pos) === blockEntity
    }

    override fun getLocation(): BlockPos {
        return pos
    }

    override fun getClipboardKey(): String {
        return "Frequencies"
    }

    override fun writeToClipboard(tag: CompoundTag, side: Direction?): Boolean {
        tag.put(
            "First", frequencyFirst.stack
                .save(CompoundTag())
        )
        tag.put(
            "Last", frequencyLast.stack
                .save(CompoundTag())
        )
        return true
    }

    override fun readFromClipboard(tag: CompoundTag, player: Player?, side: Direction?, simulate: Boolean): Boolean {
        if (!tag.contains("First") || !tag.contains("Last")) return false
        if (simulate) return true
        setFrequency(true, ItemStack.of(tag.getCompound("First")))
        setFrequency(false, ItemStack.of(tag.getCompound("Last")))
        return true
    }

    companion object {
        @JvmStatic
        val TYPE: BehaviourType<DualLinkBehaviour> = BehaviourType()

        @JvmStatic
        fun receiver(
            be: SmartBlockEntity?, slots: Pair<ValueBoxTransform?, ValueBoxTransform?>,
            signalCallback: IntConsumer?
        ): DualLinkBehaviour {
            val behaviour = DualLinkBehaviour(be, slots)
            behaviour.signalCallback = signalCallback
            behaviour.mode = Mode.RECEIVE
            return behaviour
        }

        @JvmStatic
        fun transmitter(
            be: SmartBlockEntity?, slots: Pair<ValueBoxTransform?, ValueBoxTransform?>,
            transmission: IntSupplier?
        ): DualLinkBehaviour {
            val behaviour = DualLinkBehaviour(be, slots)
            behaviour.transmission = transmission
            behaviour.mode = Mode.TRANSMIT
            return behaviour
        }
    }
}
