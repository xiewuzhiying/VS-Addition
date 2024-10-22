package io.github.xiewuzhiying.vs_addition.stuff;

import io.github.xiewuzhiying.vs_addition.VSAdditionConfig;
import me.fallenbreath.conditionalmixin.api.mixin.ConditionTester;

public class ExplosionConditionTester implements ConditionTester {
    @Override
    public boolean isSatisfied(String s) {
        return VSAdditionConfig.SERVER.getExperimental().getExplosion();
    }
}
