package io.github.jumperonjava.underwater_movement;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;

public class FunnyAnvilMod implements ClientModInitializer {
    public static boolean isAnvilThing(Object entity) {
        if(!(entity instanceof ClientPlayerEntity player)){
            return false;
        }
        if(!player.hasStatusEffect(StatusEffects.CONDUIT_POWER)){
            return false;
        }
        if (!player.getMainHandStack().isOf(Items.ANVIL) && !player.getOffHandStack().isOf(Items.ANVIL) &&
            !player.getMainHandStack().isOf(Items.CHIPPED_ANVIL) && !player.getOffHandStack().isOf(Items.CHIPPED_ANVIL) &&
            !player.getMainHandStack().isOf(Items.DAMAGED_ANVIL) && !player.getOffHandStack().isOf(Items.DAMAGED_ANVIL))
                return false;
        return true;

    }

    @Override
    public void onInitializeClient() {
    }
}
