//package io.github.xiewuzhiying.vs_addition.forge.compat.computercraft;
//
//import dan200.computercraft.api.peripheral.IPeripheral;
//import io.github.xiewuzhiying.vs_addition.forge.compat.computercraft.peripherals.CompactCannonMountPeripheral;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//import riftyboi.cbcmodernwarfare.cannon_control.compact_mount.CompactCannonMountBlockEntity;
//import riftyboi.cbcmodernwarfare.index.CBCModernWarfareBlocks;
//
//import javax.annotation.Nullable;
//
//public class PeripheralForge {
//
//    private boolean c(BlockState arg1, Block arg2) { return arg1.getBlock() == arg2; }
//
//    @Nullable
//    public IPeripheral getPeripheralForge(Level level, BlockPos blockPos, Direction direction){
//        BlockState s = level.getBlockState(blockPos);
//        BlockEntity be = level.getBlockEntity(blockPos);
//        if (c(s, CBCModernWarfareBlocks.COMPACT_MOUNT.get())) {
//            return new CompactCannonMountPeripheral("cbcmf_compact_cannon_mount", (CompactCannonMountBlockEntity) be, level, blockPos, direction);
//        } else {
//            return null;
//        }
//    }
//}
