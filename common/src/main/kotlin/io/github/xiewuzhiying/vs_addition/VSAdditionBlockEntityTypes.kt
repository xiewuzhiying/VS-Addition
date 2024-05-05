//package io.github.xiewuzhiying.vs_addition
//
//import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity
//import com.tterrag.registrate.builders.BlockEntityBuilder
//import net.minecraft.core.BlockPos
//import net.minecraft.world.level.block.entity.BlockEntityType
//import net.minecraft.world.level.block.state.BlockState
//
//class VSAdditionBlockEntityTypes {
//    private val REGISTRATE = VSAdditionMod.getRegistrate()
//
//    val COPYCAT = REGISTRATE.blockEntity<CopycatBlockEntity>("copycat",
//        BlockEntityBuilder.BlockEntityFactory<CopycatBlockEntity?> { type: BlockEntityType<CopycatBlockEntity?>?, pos: BlockPos?, state: BlockState? ->
//            CopycatBlockEntity(
//                type,
//                pos,
//                state
//            )
//        })
//        .validBlocks(
//            VSAdditionBlocks
//        )
//        .register()
//
//
//    fun register() {}
//}