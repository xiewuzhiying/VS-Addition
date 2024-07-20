package io.github.xiewuzhiying.vs_addition.mixin.valkyrienskies;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.valkyrienskies.core.apigame.ShipTeleportData;
import org.valkyrienskies.core.impl.game.ShipTeleportDataImpl;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.command.VSCommands;

@Mixin(VSCommands.class)
public abstract class MixinVSCommands {

    @ModifyArg(
            method = {
                    "registerServerCommands$lambda$4",
                    "registerServerCommands$lambda$6",
                    "registerServerCommands$lambda$8",
                    "registerServerCommands$lambda$10"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/valkyrienskies/core/apigame/VSCore;teleportShip(Lorg/valkyrienskies/core/api/world/ServerShipWorld;Lorg/valkyrienskies/core/api/ships/ServerShip;Lorg/valkyrienskies/core/apigame/ShipTeleportData;)V"
            ),
            index = 2,
            remap = false
    )
    private static ShipTeleportData replaceShipTeleportData1(ShipTeleportData oldData, @Local(argsOnly = true) CommandContext<?> it, @Share("shipTeleportData") LocalRef<ShipTeleportData> shipTeleportData) {
        shipTeleportData.set(new ShipTeleportDataImpl(oldData.getNewPos(), oldData.getNewRot(), oldData.getNewVel(), oldData.getNewOmega(), VSGameUtilsKt.getDimensionId(((CommandSourceStack)it.getSource()).getLevel()), oldData.getNewScale()));
        return shipTeleportData.get();
    }

    @ModifyArg(
            method = {
                    "registerServerCommands$lambda$4",
                    "registerServerCommands$lambda$6",
                    "registerServerCommands$lambda$8",
                    "registerServerCommands$lambda$10"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"
            ),
            index = 1
    )
    private static Object[] replaceShipTeleportData2(Object[] objects, @Share("shipTeleportData") LocalRef<ShipTeleportData> shipTeleportData) {
        objects[1] = shipTeleportData.get();
        return objects;
    }
}