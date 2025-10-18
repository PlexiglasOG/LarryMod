package io.github.plexiglasog.larrymod.client;

import io.github.plexiglasog.larrymod.client.entity.LarryModel;
import io.github.plexiglasog.larrymod.client.entity.LarryRenderer;
import io.github.plexiglasog.larrymod.entity.LarrymodEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class LarrymodClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EntityModelLayerRegistry.registerModelLayer(LarryModel.LARRY, LarryModel::getTexturedModelData);
        EntityRendererRegistry.register(LarrymodEntities.LARRY, LarryRenderer::new);
    }
}
