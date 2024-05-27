package io.github.xiewuzhiying.vs_addition.compats.create.PortableInterface;

import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlockEntity;

public interface IPortableInterfaceBE {

    default void startTransferringTo(PortableStorageInterfaceBlockEntity pi, float distance){}

    default void stopTransferring() {}

    default boolean canTransfer() { return false; }
}
