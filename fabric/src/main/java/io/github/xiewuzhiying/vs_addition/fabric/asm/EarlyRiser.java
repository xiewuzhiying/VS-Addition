package io.github.xiewuzhiying.vs_addition.fabric.asm;

import io.github.xiewuzhiying.vs_addition.asm.VSAdditionPatch;

public class EarlyRiser implements Runnable {
    @Override
    public void run() {
        VSAdditionPatch.INSTANCE.run();
    }
}
