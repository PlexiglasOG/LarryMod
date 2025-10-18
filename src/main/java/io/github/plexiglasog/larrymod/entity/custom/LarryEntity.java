package io.github.plexiglasog.larrymod.entity.custom;

import io.github.plexiglasog.larrymod.Larrymod;
import io.github.plexiglasog.larrymod.entity.LarrymodEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LarryEntity extends HostileEntity implements Angerable {

    private int angerTime;

    private static final UniformIntProvider ANGER_PASSING_COOLDOWN_RANGE = TimeHelper.betweenSeconds(4, 6);
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
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
    protected @Nullable SoundEvent getAmbientSound() {
        return Larrymod.LARRY_SOUND_EVENT;
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

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }
}
