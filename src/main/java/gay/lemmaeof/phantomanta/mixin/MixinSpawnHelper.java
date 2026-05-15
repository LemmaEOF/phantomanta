package gay.lemmaeof.phantomanta.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NaturalSpawner.class)
public class MixinSpawnHelper {
    @Inject(method = "isValidEmptySpawnBlock", at = @At("HEAD"), cancellable = true)
    private static void allowPhantomSpawnInWater(BlockGetter blockView, BlockPos pos, BlockState state, FluidState fluidState, EntityType<?> entityType, CallbackInfoReturnable<Boolean> info) {
        //bit jank but will have to do - makes it so phantoms can only spawn when there *is* a fluid state present
        if (entityType == EntityType.PHANTOM) {
            if (fluidState.is(FluidTags.WATER) && !state.isCollisionShapeFullBlock(blockView, pos) && !state.isSignalSource()) {
                info.setReturnValue(state.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE) ? false : !entityType.isBlockDangerous(state));
            } else {
                info.setReturnValue(false);
            }
        }
    }
}
