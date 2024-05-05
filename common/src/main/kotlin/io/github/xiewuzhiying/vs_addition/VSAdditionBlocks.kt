//package io.github.xiewuzhiying.vs_addition
//
//import com.copycatsplus.copycats.CopycatRegistrate
//import com.copycatsplus.copycats.config.FeatureToggle
//import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart
//import com.copycatsplus.copycats.datagen.CCLootGen
//import com.simibubi.create.foundation.data.BuilderTransformers
//import com.simibubi.create.foundation.data.CreateRegistrate
//import com.simibubi.create.foundation.data.ModelGen
//import com.tterrag.registrate.builders.BlockBuilder
//import com.tterrag.registrate.providers.DataGenContext
//import com.tterrag.registrate.providers.RegistrateBlockstateProvider
//import com.tterrag.registrate.util.nullness.NonNullFunction
//import dev.architectury.injectables.annotations.ExpectPlatform
//import io.github.xiewuzhiying.vs_addition.content.wing.CopycatWingBlock
//import io.github.xiewuzhiying.vs_addition.content.wing.CopycatWingModel
//import net.minecraft.client.resources.model.BakedModel
//import net.minecraft.world.item.BlockItem
//import net.minecraft.world.level.block.Block
//import net.minecraft.world.level.block.state.BlockBehaviour
//
//@SuppressWarnings("unused")
//class VSAdditionBlocks {
//    private val REGISTRATE: CopycatRegistrate = VSAdditionMod.getRegistrate()
//
//
//    val COPYCAT_WING = REGISTRATE.block<CopycatWingBlock>("copycat_wing",
//        NonNullFunction<BlockBehaviour.Properties, CopycatWingBlock> { pProperties: BlockBehaviour.Properties ->
//            CopycatWingBlock(
//                pProperties
//            )
//        })
//        .transform<Block, CopycatWingBlock, CreateRegistrate, BlockBuilder<CopycatWingBlock, CreateRegistrate>>(
//            BuilderTransformers.copycat<CopycatWingBlock, CreateRegistrate>()
//        )
//        .transform<Block, CopycatWingBlock, CreateRegistrate, BlockBuilder<CopycatWingBlock, CreateRegistrate>>(
//            FeatureToggle.register<Block, CopycatWingBlock, CreateRegistrate, BlockBuilder<CopycatWingBlock, CreateRegistrate>>()
//        )
//        .onRegister(CreateRegistrate.blockModel<CopycatWingBlock> {
//            NonNullFunction<BakedModel?, BakedModel> { model: BakedModel? ->
//                SimpleCopycatPart.create(
//                    model,
//                    CopycatWingModel()
//                )
//            }
//        })
//        .loot(CCLootGen.build<CopycatWingBlock>(CCLootGen.lootForLayers<CopycatWingBlock>()))
//        .item()
//        .transform<Block, CopycatWingBlock, CreateRegistrate, BlockBuilder<CopycatWingBlock, CreateRegistrate>>(
//            ModelGen.customItemModel<BlockItem, BlockBuilder<CopycatWingBlock, CreateRegistrate>>(
//                "copycat_base",
//                "wing"
//            )
//        )
//        .register()
//
//    @ExpectPlatform
//    fun getWrappedBlockState(c: DataGenContext<Block?, out Block?>?, p: RegistrateBlockstateProvider?, name: String?) {
//        throw AssertionError()
//    }
//
//    fun register() {}
//}