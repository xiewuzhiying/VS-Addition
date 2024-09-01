package io.github.xiewuzhiying.vs_addition.stuff;

import dev.architectury.platform.Platform;
import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import me.fallenbreath.conditionalmixin.api.mixin.ConditionTester;

public class InteractiveConditionTester implements ConditionTester {
    @Override
    public boolean isSatisfied(String s) {
        return !VSAdditionConfig.COMMON.getInsteadCreateInteractiveDeployer() && Platform.isModLoaded("create_interactive");
    }
}
