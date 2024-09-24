package io.github.xiewuzhiying.vs_addition.stuff;

import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import me.fallenbreath.conditionalmixin.api.mixin.ConditionTester;

public class EncasedFanConditionTester implements ConditionTester {
    @Override
    public boolean isSatisfied(String s) {
        return VSAdditionConfig.SERVER.getCreate().getEncasedFanMixin();
    }
}
