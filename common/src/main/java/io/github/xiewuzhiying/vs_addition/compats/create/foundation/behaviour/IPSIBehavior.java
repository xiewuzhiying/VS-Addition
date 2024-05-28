package io.github.xiewuzhiying.vs_addition.compats.create.foundation.behaviour;

import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.Lang;

public interface IPSIBehavior {
    default ScrollOptionBehaviour<WorkigMode> getWorkingMode() {
        return null;
    }

    default void startTransferringTo(PortableStorageInterfaceBlockEntity pi, float distance){}

    default void stopTransferring() {}

    default boolean canTransfer() { return false; }
    static enum WorkigMode implements INamedIconOptions {

        ORIGINAL(AllIcons.I_MOVE_PLACE_RETURNED),
        WITH_SHIP(AllIcons.I_MOVE_PLACE),

        ;

        private String translationKey;
        private AllIcons icon;

        private WorkigMode(AllIcons icon) {
            this.icon = icon;
            translationKey = "psi.working_mode." + Lang.asId(name());
        }

        public com.simibubi.create.foundation.gui.AllIcons getIcon() {
            return icon;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }

    }
}
