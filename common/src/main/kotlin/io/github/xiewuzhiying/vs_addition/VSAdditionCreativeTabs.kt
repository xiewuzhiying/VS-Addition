//package io.github.xiewuzhiying.vs_addition
//
//import com.copycatsplus.copycats.config.FeatureToggle
//import com.copycatsplus.copycats.mixin_interfaces.CreativeTabExpander
//import com.tterrag.registrate.util.entry.ItemProviderEntry
//import net.minecraft.core.NonNullList
//import net.minecraft.world.item.CreativeModeTab
//import net.minecraft.world.item.ItemStack
//import net.minecraft.world.level.ItemLike
//
//
//object VSAdditionCreativeTabs {
//    val MAIN: CreativeModeTab = MainCreativeModeTab()
//    var ITEMS: List<ItemProviderEntry<*>>? = null
//
//    init {
//        ITEMS = java.util.List.of<ItemProviderEntry<*>>(
//            VSAdditionBlocks.COPYCAT_WING
//        )
//    }
//
//    class MainCreativeModeTab :
//        CreativeModeTab((TAB_BUILDING_BLOCKS as CreativeTabExpander).`copycats$expandTabCount`(), "vs_addition.main") {
//        override fun makeIcon(): ItemStack {
//            return VSAdditionBlocks.COPYCAT_WING.asStack()
//        }
//
//        override fun fillItemList(pItems: NonNullList<ItemStack>) {
//            val var2: Iterator<*> = ITEMS!!.iterator()
//            while (var2.hasNext()) {
//                val item = var2.next() as ItemProviderEntry<*>
//                if (FeatureToggle.isEnabled(item.id)) {
//                    (item.get() as ItemLike).asItem().fillItemCategory(this, pItems)
//                }
//            }
//        }
//    }
//}
//
