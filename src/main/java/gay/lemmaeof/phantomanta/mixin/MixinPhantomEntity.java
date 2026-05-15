package gay.lemmaeof.phantomanta.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Phantom.class)
public class MixinPhantomEntity extends Mob {
	@Shadow
	private @Nullable BlockPos anchorPoint;

	protected MixinPhantomEntity(EntityType<? extends Mob> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "travel", at = @At("HEAD"), cancellable = true)
	private void makeSwim(Vec3 movementVector, CallbackInfo info) {
		if (this.shouldTravelInFluid(this.level().getFluidState(this.getOnPos()))) {
			this.travelFlying(movementVector, 0.4f, 0.02f, 0.02f);
		} else {
			super.travel(movementVector);
		}
		//clearly inject head unconditional cancel
		info.cancel();
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void makeTryToSwim(CallbackInfo info) {
		//this kinda sucks
		if (this.level() != null && this.anchorPoint != null && !this.level().isClientSide() && this.level().getFluidState(this.anchorPoint).isEmpty()) {
			this.anchorPoint = level().getHeightmapPos(Heightmap.Types.OCEAN_FLOOR, this.anchorPoint).above(random.nextInt(20));
		}
	}
}