package io.github.xiewuzhiying.vs_addition.forge.asm;

import com.chocohead.mm.Asm;
import io.github.xiewuzhiying.vs_addition.asm.VSAdditionPatch;

@Asm
public class EarlyRiser implements Runnable {
    @Override
    public void run() {
        VSAdditionPatch.INSTANCE.run();
    }
}
