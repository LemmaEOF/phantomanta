package gay.lemmaeof.phantomanta.mixin;

import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PhantomSpawner.class)
public class MixinPhantomSpawner {
    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;above(I)Lnet/minecraft/core/BlockPos;"))
    private int adjustSpawnRange(int original) {
        //makes spawns both up and down possible so that they have a better chance to spawn in water
        return original - 28;
    }
}
