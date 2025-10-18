package io.github.plexiglasog.larrymod.items;

import io.github.plexiglasog.larrymod.Larrymod;
import io.github.plexiglasog.larrymod.entity.LarrymodEntities;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LarrymodItems {

    public static final Item LARRY_SPAWN_EGG = registerItem("larry_spawn_egg",
            new SpawnEggItem(
                    LarrymodEntities.LARRY,
            0x004153,
                    0xe20074,
                    new Item.Settings())
    );

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(Larrymod.MOD_ID, name), item);
    }

    public  static void registerItems(){

    }
}
