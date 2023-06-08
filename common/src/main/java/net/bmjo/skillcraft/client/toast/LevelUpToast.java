package net.bmjo.skillcraft.client.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bmjo.skillcraft.SkillcraftIdentifier;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.bmjo.skillcraft.skill.Skillset;
import net.minecraft.util.Identifier;

import java.util.List;

public class LevelUpToast implements Toast {
    private static final Identifier TEXTURE = new SkillcraftIdentifier("textures/gui/skill_toast.png");
    private static final long DURATION = 5000L;
    private final Skillset skillset;
    private final int skillLevel;

    public LevelUpToast(Skillset skillset, int skillLevel) {
        this.skillset = skillset;
        this.skillLevel = skillLevel;
    }
    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        manager.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight());
        manager.getClient().textRenderer.draw(matrices, Text.literal(skillset.getName() + ": " + this.skillset.getLevelName(skillLevel)), 16.0F, 6.0F, 0xA09B83); //0x257a3c);

        MatrixStack matrixStack = RenderSystem.getModelViewStack();

        matrixStack.push();
        matrixStack.scale(0.6F, 0.6F, 1.0F);
        RenderSystem.applyModelViewMatrix();
        manager.getClient().getItemRenderer().renderInGui(skillset.getIcon(), 3, 3);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.scale(0.7F, 0.7F, 1.0F);
        RenderSystem.applyModelViewMatrix();
        List<ItemStack> itemIcons = this.skillset.getUnlockItems(skillLevel).stream().map(Item::getDefaultStack).toList();
        int item = 0;
        for (ItemStack itemIcon : itemIcons) {
            manager.getClient().getItemRenderer().renderInGui(itemIcon, 22 + 16 * item, 22);
            if (item >= 7) break;
            item++;
        }
        matrixStack.pop();

        return startTime >= DURATION ? Visibility.HIDE : Visibility.SHOW;
    }
}
