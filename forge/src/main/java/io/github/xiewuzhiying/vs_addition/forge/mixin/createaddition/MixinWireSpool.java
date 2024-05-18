package io.github.xiewuzhiying.vs_addition.forge.mixin.createaddition;

import com.mrh0.createaddition.blocks.connector.ConnectorType;
import com.mrh0.createaddition.energy.IWireNode;
import com.mrh0.createaddition.energy.WireConnectResult;
import com.mrh0.createaddition.energy.WireType;
import com.mrh0.createaddition.item.WireSpool;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(WireSpool.class)
public class MixinWireSpool {
    @Redirect(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mrh0/createaddition/energy/IWireNode;connect(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ILnet/minecraft/core/BlockPos;ILcom/mrh0/createaddition/energy/WireType;)Lcom/mrh0/createaddition/energy/WireConnectResult;"
            )
    )
    public WireConnectResult connect(Level world, BlockPos pos1, int node1, BlockPos pos2, int node2, WireType type) {
        BlockEntity te1 = world.getBlockEntity(pos1);
        BlockEntity te2 = world.getBlockEntity(pos2);
        if (te1 != null && te2 != null && te1 != te2) {
            if (te1 instanceof IWireNode) {
                IWireNode wn1 = (IWireNode)te1;
                if (te2 instanceof IWireNode) {
                    IWireNode wn2 = (IWireNode)te2;
                    if (node1 >= 0 && node2 >= 0) {
                        int maxLength = Math.min(wn1.getMaxWireLength(), wn2.getMaxWireLength());
                        if (VSGameUtilsKt.squaredDistanceBetweenInclShips(world, pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ()) > (double)(maxLength * maxLength)) {
                            return WireConnectResult.LONG;
                        }

                        if (wn1.hasConnectionTo(pos2)) {
                            return WireConnectResult.EXISTS;
                        }

                        if (wn1.getConnectorType() == ConnectorType.Large && wn2.getConnectorType() == ConnectorType.Large && type == WireType.COPPER) {
                            return WireConnectResult.REQUIRES_HIGH_CURRENT;
                        }

                        wn1.setNode(node1, node2, wn2.getPos(), type);
                        wn2.setNode(node2, node1, wn1.getPos(), type);
                        return WireConnectResult.getLink(wn2.isNodeInput(node2), wn2.isNodeOutput(node2));
                    }

                    return WireConnectResult.COUNT;
                }
            }

            return WireConnectResult.INVALID;
        } else {
            return WireConnectResult.INVALID;
        }
    }
}
