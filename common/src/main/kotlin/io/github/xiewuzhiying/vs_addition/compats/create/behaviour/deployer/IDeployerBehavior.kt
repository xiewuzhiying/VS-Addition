package io.github.xiewuzhiying.vs_addition.compats.create.behaviour.deployer

import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour
import com.simibubi.create.foundation.gui.AllIcons
import com.simibubi.create.foundation.utility.Lang

interface IDeployerBehavior {
    fun `vs_addition$getWorkingMode`(): ScrollOptionBehaviour<WorkigMode>? {
        return null
    }

    enum class WorkigMode(private val icon: AllIcons) : INamedIconOptions {
        ORIGINAL(AllIcons.I_MOVE_PLACE_RETURNED),
        WITH_SHIP(AllIcons.I_MOVE_PLACE),
        ;

        private val translationKey = "vs_addition.working_mode." + Lang.asId(name)

        override fun getIcon(): AllIcons {
            return icon
        }

        override fun getTranslationKey(): String {
            return translationKey
        }
    }
}
