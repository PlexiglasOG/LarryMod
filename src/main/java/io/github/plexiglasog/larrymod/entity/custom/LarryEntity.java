package io.github.plexiglasog.larrymod.entity.custom;

import io.github.plexiglasog.larrymod.Larrymod;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;

import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.nbt.NbtCompound;


import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LarryEntity extends HostileEntity implements Angerable {

    private int angerTime;

    private static final UniformIntProvider ANGER_PASSING_COOLDOWN_RANGE = TimeHelper.betweenSeconds(4, 6);
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);

    private static final TrackedData<Boolean> BABY = DataTracker.registerData(LarryEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final Identifier BABY_SPEED_MODIFIER_ID = Identifier.ofVanilla("baby");
    private static final EntityAttributeModifier BABY_SPEED_BONUS = new EntityAttributeModifier(
            BABY_SPEED_MODIFIER_ID, 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
    );
    @Nullable
    private UUID angryAt;
    private int angerPassingCooldown;

    public LarryEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 5.0D, false));
        this.goalSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
    }

    public static DefaultAttributeContainer.Builder createAttributes(){
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.7)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3650)
                ;
    }

    @Override
    protected void mobTick() {
        this.tickAngerLogic((ServerWorld)this.getWorld(), true);
        if (this.getTarget() != null) {
            this.tickAngerPassing();
        }

        if (this.hasAngerTime()) {
            this.playerHitTimer = this.age;
        }

        super.mobTick();
    }

    @Override
    public void setBaby(boolean baby) {
        this.getDataTracker().set(BABY, baby);
        if (this.getWorld() != null && !this.getWorld().isClient) {
            EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            entityAttributeInstance.removeModifier(BABY_SPEED_MODIFIER_ID);
            if (baby) {
                entityAttributeInstance.addTemporaryModifier(BABY_SPEED_BONUS);
            }
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(BABY, false);
    }

    @Override
    public boolean isBaby() {
        return this.getDataTracker().get(BABY);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return Larrymod.LARRY_SOUND_EVENT;
    }


    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean success = super.damage(source, amount);
        if (success && source.getAttacker() instanceof PlayerEntity player) {
            this.setAngryAt(player.getUuid());
            this.chooseRandomAngerTime();
            this.setTarget(player);
        }
        return success;
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Override
    public int getAngerTime() {
        return this.angerTime;
    }

    @Override
    public @Nullable UUID getAngryAt() {
        return angryAt;
    }

    public boolean isAngryAt(PlayerEntity player) {
        return this.shouldAngerAt(player);
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }


    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target instanceof PlayerEntity) {
            this.setAttacking((PlayerEntity)target);
        }

        super.setTarget(target);
    }

    private void tickAngerPassing() {
        if (this.angerPassingCooldown > 0) {
            this.angerPassingCooldown--;
        } else {

            this.angerPassingCooldown = ANGER_PASSING_COOLDOWN_RANGE.get(this.random);
        }
    }

    @Override
    public boolean shouldAngerAt(LivingEntity entity) {
        if(entity instanceof PlayerEntity && this.canTarget(entity)){
            return entity.getType() == EntityType.PLAYER && this.isUniversallyAngry(entity.getWorld()) || entity.getUuid().equals(this.getAngryAt());
        } else {
            return Angerable.super.shouldAngerAt(entity);
        }
    }

    public static boolean shouldBeBaby(Random random) {
        return random.nextFloat() < 0.05F;
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsBaby", this.isBaby());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setBaby(nbt.getBoolean("IsBaby"));
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        Random random = world.getRandom();
        entityData = super.initialize(world, difficulty, spawnReason, entityData);
        if (entityData == null) {
            entityData = new LarryEntity.LarryData(shouldBeBaby(random));
        }
        if (entityData instanceof LarryEntity.LarryData larryData) {
            if (larryData.baby) {
                this.setBaby(true);
            }

        }


        return entityData;
    }

    public static class LarryData implements EntityData {
        public final boolean baby;

        public LarryData(boolean baby) {
            this.baby = baby;
        }
    }
}
