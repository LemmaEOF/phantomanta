package gay.lemmaeof.phantomanta.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.class)
public class MixinSpawnHelper {
    @Inject(method = "isClearForSpawn", at = @At("HEAD"), cancellable = true)
    private static void allowPhantomSpawnInWater(BlockView blockView, BlockPos pos, BlockState state, FluidState fluidState, EntityType<?> entityType, CallbackInfoReturnable<Boolean> info) {
        //bit jank but will have to do - makes it so phantoms can only spawn when there *is* a fluid state present
        if (entityType == EntityType.PHANTOM) {
            if (fluidState.isIn(FluidTags.WATER) && !state.isFullCube(blockView, pos) && !state.emitsRedstonePower()) {
                info.setReturnValue(state.isIn(BlockTags.PREVENT_MOB_SPAWNING_INSIDE) ? false : !entityType.isInvalidSpawn(state));
            } else {
                info.setReturnValue(false);
            }
        }
    }
}
