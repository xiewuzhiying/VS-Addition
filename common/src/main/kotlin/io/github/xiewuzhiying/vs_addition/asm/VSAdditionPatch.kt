package io.github.xiewuzhiying.vs_addition.asm

import com.chocohead.mm.api.ClassTinkerers
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

object VSAdditionPatch {
    fun run() {
        ClassTinkerers.addTransformation(
            "edn.stratodonut.tallyho.client.ClientEvents"
        ) { classNode: ClassNode ->
            classNode.methods.removeIf { method: MethodNode ->
                method.name.contains("onBleedout")
            }
        }
    }
}