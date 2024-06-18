package io.github.xiewuzhiying.vs_addition.compats.create.behaviour.Link;

import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.clipboard.ClipboardCloneable;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class DualLinkBehaviour extends BlockEntityBehaviour implements IRedstoneLinkable, ClipboardCloneable {
    public static final BehaviourType<DualLinkBehaviour> TYPE = new BehaviourType<>();

    enum Mode {
        TRANSMIT, RECEIVE
    }

    RedstoneLinkNetworkHandler.Frequency frequencyFirst;
    RedstoneLinkNetworkHandler.Frequency frequencyLast;
    public ValueBoxTransform firstSlot;
    public ValueBoxTransform secondSlot;
    Vec3 textShift;

    public boolean newPosition;
    private Mode mode;
    private IntSupplier transmission;
    private IntConsumer signalCallback;

    protected DualLinkBehaviour(SmartBlockEntity be, Pair<ValueBoxTransform, ValueBoxTransform> slots) {
        super(be);
        frequencyFirst = RedstoneLinkNetworkHandler.Frequency.EMPTY;
        frequencyLast = RedstoneLinkNetworkHandler.Frequency.EMPTY;
        firstSlot = slots.getLeft();
        secondSlot = slots.getRight();
        textShift = Vec3.ZERO;
        newPosition = true;
    }

    public static DualLinkBehaviour receiver(SmartBlockEntity be, Pair<ValueBoxTransform, ValueBoxTransform> slots,
                                             IntConsumer signalCallback) {
        DualLinkBehaviour behaviour = new DualLinkBehaviour(be, slots);
        behaviour.signalCallback = signalCallback;
        behaviour.mode = Mode.RECEIVE;
        return behaviour;
    }

    public static DualLinkBehaviour transmitter(SmartBlockEntity be, Pair<ValueBoxTransform, ValueBoxTransform> slots,
                                                IntSupplier transmission) {
        DualLinkBehaviour behaviour = new DualLinkBehaviour(be, slots);
        behaviour.transmission = transmission;
        behaviour.mode = Mode.TRANSMIT;
        return behaviour;
    }

    public DualLinkBehaviour moveText(Vec3 shift) {
        textShift = shift;
        return this;
    }

    public void copyItemsFrom(DualLinkBehaviour behaviour) {
        if (behaviour == null)
            return;
        frequencyFirst = behaviour.frequencyFirst;
        frequencyLast = behaviour.frequencyLast;
    }

    @Override
    public boolean isListening() {
        return mode == Mode.RECEIVE;
    }

    @Override
    public int getTransmittedStrength() {
        return mode == Mode.TRANSMIT ? transmission.getAsInt() : 0;
    }

    @Override
    public void setReceivedStrength(int networkPower) {
        if (!newPosition)
            return;
        signalCallback.accept(networkPower);
    }

    public void notifySignalChange() {
        Create.REDSTONE_LINK_NETWORK_HANDLER.updateNetworkOf(getWorld(), this);
    }

    @Override
    public void initialize() {
        super.initialize();
        if (getWorld().isClientSide)
            return;
        getHandler().addToNetwork(getWorld(), this);
        newPosition = true;
    }

    @Override
    public Couple<RedstoneLinkNetworkHandler.Frequency> getNetworkKey() {
        return Couple.create(frequencyFirst, frequencyLast);
    }

    @Override
    public void unload() {
        super.unload();
        if (getWorld().isClientSide)
            return;
        getHandler().removeFromNetwork(getWorld(), this);
    }

    @Override
    public boolean isSafeNBT() {
        return true;
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        nbt.put("DualFrequencyFirst", frequencyFirst.getStack()
                .save(new CompoundTag()));
        nbt.put("DualFrequencyLast", frequencyLast.getStack()
                .save(new CompoundTag()));
        nbt.putLong("DualLastKnownPosition", blockEntity.getBlockPos()
                .asLong());
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        long positionInTag = blockEntity.getBlockPos()
                .asLong();
        long positionKey = nbt.getLong("DualLastKnownPosition");
        newPosition = positionInTag != positionKey;

        super.read(nbt, clientPacket);
        frequencyFirst = RedstoneLinkNetworkHandler.Frequency.of(ItemStack.of(nbt.getCompound("DualFrequencyFirst")));
        frequencyLast = RedstoneLinkNetworkHandler.Frequency.of(ItemStack.of(nbt.getCompound("DualFrequencyLast")));
    }

    public void setFrequency(boolean first, ItemStack stack) {
        stack = stack.copy();
        stack.setCount(1);
        ItemStack toCompare = first ? frequencyFirst.getStack() : frequencyLast.getStack();
        boolean changed = !ItemStack.isSameItemSameTags(stack, toCompare);

        if (changed)
            getHandler().removeFromNetwork(getWorld(), this);

        if (first)
            frequencyFirst = RedstoneLinkNetworkHandler.Frequency.of(stack);
        else
            frequencyLast = RedstoneLinkNetworkHandler.Frequency.of(stack);

        if (!changed)
            return;

        blockEntity.sendData();
        getHandler().addToNetwork(getWorld(), this);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    private RedstoneLinkNetworkHandler getHandler() {
        return Create.REDSTONE_LINK_NETWORK_HANDLER;
    }

    public static class SlotPositioning {
        Function<BlockState, Pair<Vec3, Vec3>> offsets;
        Function<BlockState, Vec3> rotation;
        float scale;

        public SlotPositioning(Function<BlockState, Pair<Vec3, Vec3>> offsetsForState,
                               Function<BlockState, Vec3> rotationForState) {
            offsets = offsetsForState;
            rotation = rotationForState;
            scale = 1;
        }

        public SlotPositioning scale(float scale) {
            this.scale = scale;
            return this;
        }

    }

    public boolean testHit(Boolean first, Vec3 hit) {
        BlockState state = blockEntity.getBlockState();
        Vec3 localHit = hit.subtract(Vec3.atLowerCornerOf(blockEntity.getBlockPos()));
        return (first ? firstSlot : secondSlot).testHit(state, localHit);
    }

    @Override
    public boolean isAlive() {
        Level level = getWorld();
        BlockPos pos = getPos();
        if (blockEntity.isChunkUnloaded())
            return false;
        if (blockEntity.isRemoved())
            return false;
        if (!level.isLoaded(pos))
            return false;
        return level.getBlockEntity(pos) == blockEntity;
    }

    @Override
    public BlockPos getLocation() {
        return getPos();
    }

    @Override
    public String getClipboardKey() {
        return "Frequencies";
    }

    @Override
    public boolean writeToClipboard(CompoundTag tag, Direction side) {
        tag.put("First", frequencyFirst.getStack()
                .save(new CompoundTag()));
        tag.put("Last", frequencyLast.getStack()
                .save(new CompoundTag()));
        return true;
    }

    @Override
    public boolean readFromClipboard(CompoundTag tag, Player player, Direction side, boolean simulate) {
        if (!tag.contains("First") || !tag.contains("Last"))
            return false;
        if (simulate)
            return true;
        setFrequency(true, ItemStack.of(tag.getCompound("First")));
        setFrequency(false, ItemStack.of(tag.getCompound("Last")));
        return true;
    }

}
