package io.github.jumperonjava.underwater_movement.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.jumperonjava.underwater_movement.FunnyAnvilMod.isAnvilThing;

@Mixin(Entity.class)
public abstract class EntityMixin {
    private boolean intentionalSwimming;

    @Shadow public abstract void setFlag(int index, boolean value);

    @Shadow protected abstract void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    @Shadow public abstract EntityPose getPose();

    @Shadow protected boolean submergedInWater;

    @Shadow protected boolean touchingWater;

    @Shadow @Final protected DataTracker dataTracker;

    @Shadow @Final protected static TrackedData<EntityPose> POSE;

    @Shadow public abstract ActionResult interact(PlayerEntity player, Hand hand);

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Inject(method = "isTouchingWater",cancellable = true,at= @At("HEAD"))
    void nope(CallbackInfoReturnable<Boolean> cir){
        if(isAnvilThing(this))
        {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "isSubmergedInWater",cancellable = true,at= @At("HEAD"))
    void iSaidNo(CallbackInfoReturnable<Boolean> cir){
        if(isAnvilThing(this))
        {
            cir.setReturnValue(false);
        }
    }
    @Redirect(method = "updateSwimming",at = @At(value = "INVOKE",target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    boolean iDontTouchWater(FluidState instance, TagKey<Fluid> tag){
        if(tag == (FluidTags.WATER) && isAnvilThing(this)){
            return false;
        }
        return instance.isIn(tag);
    }
    @Inject(method = "setSwimming",at =@At("HEAD"),cancellable = true)
    void nope(boolean swimming, CallbackInfo cir){
        if(isAnvilThing(this))
        {
            cir.cancel();
        }
    }
    @Inject(method = "isSwimming",cancellable = true,at= @At("HEAD"))
    void iCantSwim(CallbackInfoReturnable<Boolean> cir){
        if(isAnvilThing(this))
        {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "getPose",cancellable = true,at= @At("HEAD"))
    void not(CallbackInfoReturnable<EntityPose> cir){
        if(isAnvilThing(this) && submergedInWater && touchingWater)
        {
            if(dataTracker.get(POSE) == EntityPose.SWIMMING)
                cir.setReturnValue(EntityPose.STANDING);
        }
    }
    @Inject(method = "setPose",cancellable = true,at= @At("HEAD"))
    void low(EntityPose pose, CallbackInfo ci) {
    }
}
