package io.github.plexiglasog.larrymod;

import io.github.plexiglasog.larrymod.entity.LarrymodEntities;
import io.github.plexiglasog.larrymod.entity.custom.LarryEntity;
import io.github.plexiglasog.larrymod.entity.ritual.LarrymodSpawnRituals;
import io.github.plexiglasog.larrymod.itemgroup.LarrymodItemGroups;
import io.github.plexiglasog.larrymod.items.LarrymodItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Larrymod implements ModInitializer {

    public static final String MOD_ID = "larrymod";

    public static final Identifier LARRY_SOUND_ID = Identifier.of(MOD_ID, "larry");
    public static SoundEvent LARRY_SOUND_EVENT = SoundEvent.of(LARRY_SOUND_ID);
    public static final Identifier LARRY_FULL_SOUND_ID = Identifier.of(MOD_ID, "larry_full");
    public static SoundEvent LARRY_FULL_SOUND_EVENT = SoundEvent.of(LARRY_FULL_SOUND_ID);

    @Override
    public void onInitialize() {

        FabricDefaultAttributeRegistry.register(LarrymodEntities.LARRY, LarryEntity.createAttributes());
        LarrymodItems.registerItems();
        LarrymodEntities.registerMobEntites();
        LarrymodItemGroups.initialize();
        LarrymodSpawnRituals.initialize();
    }
}
