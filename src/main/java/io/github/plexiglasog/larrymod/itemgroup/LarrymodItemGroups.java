package io.github.plexiglasog.larrymod.itemgroup;

import io.github.plexiglasog.larrymod.items.LarrymodItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class LarrymodItemGroups {

    private static void registerItemsToVanillaItemGroup(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
            content.addAfter(Items.CAT_SPAWN_EGG, LarrymodItems.LARRY_SPAWN_EGG);
        });
    }


    public static void initialize(){
        registerItemsToVanillaItemGroup();
    }
}
