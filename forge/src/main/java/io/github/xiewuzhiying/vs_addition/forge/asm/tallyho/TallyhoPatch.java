package io.github.xiewuzhiying.vs_addition.forge.asm.tallyho;

import com.chocohead.mm.Asm;
import com.chocohead.mm.api.ClassTinkerers;

@Asm
public class TallyhoPatch implements Runnable {
    @Override
    public void run() {
        ClassTinkerers.addTransformation("edn.stratodonut.tallyho.client.ClientEvents", classNode -> classNode.methods.removeIf(method -> method.name.contains("onBleedout")));
    }
}
