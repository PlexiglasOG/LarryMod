package io.github.plexiglasog.larrymod.mixin;

import io.github.plexiglasog.larrymod.entity.LarrymodEntities;
import io.github.plexiglasog.larrymod.entity.custom.LarryEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(CarvedPumpkinBlock.class)
public abstract class CarvedPumpkinBlockMixin {

    @Shadow
    private static void spawnEntity(World world, BlockPattern.Result patternResult, Entity entity, BlockPos pos) {
    }

    @Inject(method = "trySpawnEntity", at = @At("RETURN"))
    private void spawnLarry(World world, BlockPos pos, CallbackInfo ci) {
        if (world.isClient) return;
        if (!(world instanceof ServerWorld serverWorld)) return;

        BlockPattern.Result myPatternResult = getLarryPattern().searchAround(world, pos);
        if (myPatternResult != null) {
            LarryEntity larryEntity = LarrymodEntities.LARRY.create(world);
            spawnEntity(world, myPatternResult, larryEntity, pos);
        }
    }

    // Define your custom block pattern
    @Unique
    private static BlockPattern getLarryPattern() {
        return BlockPatternBuilder.start()
                .aisle("###","#^#", "###")
                .where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.CARVED_PUMPKIN)))
                .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.BLACK_CONCRETE)))
                .build();
    }
}
