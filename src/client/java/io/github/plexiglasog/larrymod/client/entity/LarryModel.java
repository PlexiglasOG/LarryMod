package io.github.plexiglasog.larrymod.client.entity;

import io.github.plexiglasog.larrymod.Larrymod;
import io.github.plexiglasog.larrymod.entity.custom.LarryEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class LarryModel<T extends LarryEntity> extends SinglePartEntityModel<T> {

    public static final EntityModelLayer LARRY = new EntityModelLayer(Identifier.of(Larrymod.MOD_ID, "larry"), "main");

    private final ModelPart bb_main;
    public LarryModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-24.0F, -48.0F, 1.0F, 48.0F, 48.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 96, 48);
    }
    @Override
    public void setAngles(LarryEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        bb_main.render(matrices, vertexConsumer, light, overlay, color);
    }

    @Override
    public ModelPart getPart() {
        return bb_main;
    }
}
