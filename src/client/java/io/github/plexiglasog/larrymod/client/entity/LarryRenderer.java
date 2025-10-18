package io.github.plexiglasog.larrymod.client.entity;

import io.github.plexiglasog.larrymod.Larrymod;
import io.github.plexiglasog.larrymod.entity.custom.LarryEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class LarryRenderer extends MobEntityRenderer<LarryEntity, LarryModel<LarryEntity>> {
    public LarryRenderer(EntityRendererFactory.Context context) {
        super(context, new LarryModel<>(context.getPart(LarryModel.LARRY)), 0.5f);
    }

    @Override
    public Identifier getTexture(LarryEntity entity) {
        return Identifier.of(Larrymod.MOD_ID, "textures/entity/larry.png");
    }

    @Override
    public void render(LarryEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

        if(livingEntity.isBaby()){
            matrixStack.scale(0.5f,0.5f,0.5f);
        } else{
            matrixStack.scale(1f,1f,1f);
        }


        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
