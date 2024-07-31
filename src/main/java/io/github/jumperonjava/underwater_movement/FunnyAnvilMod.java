package io.github.jumperonjava.underwater_movement;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.slf4j.LoggerFactory;

public class FunnyAnvilMod implements ClientModInitializer {
    public static boolean isAnvilThing(Object entity) {
        if(!(entity instanceof ClientPlayerEntity player)){
            return false;
        }
//        if(player.isSwimming()){
//            return false;
//        }

        var hasAnvil = false;

        if (
                player.getMainHandStack().isOf(Items.ANVIL)         || player.getOffHandStack().isOf(Items.ANVIL)         ||
                player.getMainHandStack().isOf(Items.CHIPPED_ANVIL) || player.getOffHandStack().isOf(Items.CHIPPED_ANVIL) ||
                player.getMainHandStack().isOf(Items.DAMAGED_ANVIL) || player.getOffHandStack().isOf(Items.DAMAGED_ANVIL)) {
            hasAnvil = true;
        }

        var quartzFound = false;
        var lodestoneFound = false;

        for(float i=0;i>=-3.5;i-=0.5){
            for(int j=-1;j<=1;j++){
                for(int k=-1;k<=1;k++){
                    var playerPos = player.getPos();
                    var world = player.getWorld();
                    var offset = playerPos.add(j,i,k);
                    var blockpos = new BlockPos((int) Math.floor(offset.x), (int) Math.floor(offset.y), (int) Math.floor(offset.z));
                    var block = world.getBlockState(blockpos).getBlock();
                    if(block == Blocks.NETHER_QUARTZ_ORE){
                        quartzFound = true;
                    }
                }
            }
        }

        float maxRadius = 12;
        for(int i = (int) -maxRadius; i<=maxRadius; i++){
            for(int j = (int) -maxRadius; j<=maxRadius; j++){
                for(int k = (int) -maxRadius; k<=maxRadius; k++){

                    var playerPos = player.getPos();
                    var world = player.getWorld();
                    var offset = playerPos.add(j,i,k);
                    var blockpos = new BlockPos((int) Math.floor(offset.x), (int) Math.floor(offset.y), (int) Math.floor(offset.z));
                    var block = world.getBlockState(blockpos).getBlock();
                    if(block == Blocks.LODESTONE){
                        var radius = 0;
                        if(world.getBlockState(blockpos.down()).getBlock() == Blocks.NOTE_BLOCK){
                            radius = world.getBlockState(blockpos.down()).get(NoteBlock.NOTE)/2;
                        }
                        lodestoneFound = lodestoneFound || playerPos.distanceTo(offset) < radius;
                    }
                }
            }
        }

        LoggerFactory.getLogger("eee").info("{}",hasAnvil);
        return quartzFound || lodestoneFound || hasAnvil;

    }

    @Override
    public void onInitializeClient() {
    }
}
