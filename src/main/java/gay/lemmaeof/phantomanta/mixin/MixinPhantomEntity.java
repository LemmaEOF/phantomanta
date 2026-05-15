package gay.lemmaeof.phantomanta.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhantomEntity.class)
public class MixinPhantomEntity extends MobEntity {
	@Shadow
	private @Nullable BlockPos circlingCenter;

	protected MixinPhantomEntity(EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "travel", at = @At("HEAD"), cancellable = true)
	private void makeSwim(Vec3d movementVector, CallbackInfo info) {
		if (this.isTravellingInFluid(this.getEntityWorld().getFluidState(this.getBlockPos()))) {
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
		if (this.getEntityWorld() != null && this.circlingCenter != null && !this.getEntityWorld().isClient() && this.getEntityWorld().getFluidState(this.circlingCenter).isEmpty()) {
			this.circlingCenter = getEntityWorld().getTopPosition(Heightmap.Type.OCEAN_FLOOR, this.circlingCenter).up(random.nextInt(20));
		}
	}
}