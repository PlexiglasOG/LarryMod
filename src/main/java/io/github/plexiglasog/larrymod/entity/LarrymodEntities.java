package io.github.plexiglasog.larrymod.entity;

import io.github.plexiglasog.larrymod.Larrymod;
import io.github.plexiglasog.larrymod.entity.custom.LarryEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LarrymodEntities {

    public static final EntityType<LarryEntity> LARRY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Larrymod.MOD_ID, "larry"),
            EntityType.Builder.create(LarryEntity::new, SpawnGroup.CREATURE)
                    .dimensions(3f,3f)
                    .build()
    );

    public static void registerMobEntites(){

    }
}
