package io.github.xiewuzhiying.vs_addition.compats.create.behaviour.deployer;

import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.Lang;

public interface IDeployerBehavior {
    default ScrollOptionBehaviour<WorkigMode> vs_addition$getWorkingMode() {
        return null;
    }

    enum WorkigMode implements INamedIconOptions {

        ORIGINAL(AllIcons.I_MOVE_PLACE_RETURNED),
        WITH_SHIP(AllIcons.I_MOVE_PLACE),

        ;

        private String translationKey;
        private AllIcons icon;

        WorkigMode(AllIcons icon) {
            this.icon = icon;
            translationKey = "vs_addition.working_mode." + Lang.asId(name());
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
