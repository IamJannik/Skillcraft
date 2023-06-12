package net.bmjo.skillcraft.client.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bmjo.skillcraft.SkillcraftIdentifier;
import net.bmjo.skillcraft.skill.Skill;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class LevelUpToast implements Toast {
    private static final Identifier TEXTURE = new SkillcraftIdentifier("textures/gui/skill_toast.png");
    private static final long DURATION = 5000L;
    private final Skill skill;
    private final int skillLevel;

    public LevelUpToast(Skill skill, int skillLevel) {
        this.skill = skill;
        this.skillLevel = skillLevel;
    }
    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        manager.getClient().textRenderer.draw(matrices, Text.literal(this.skill.getName() + ": " + this.skill.getLevelName(this.skillLevel)), 16.0F, 6.0F, 0x007100);

        MatrixStack matrixStack = RenderSystem.getModelViewStack();

        matrixStack.push();
        matrixStack.scale(0.6F, 0.6F, 1.0F);
        RenderSystem.applyModelViewMatrix();
        manager.getClient().getItemRenderer().renderInGui(this.skill.getIcon(), 8, 8);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.scale(0.7F, 0.7F, 1.0F);
        RenderSystem.applyModelViewMatrix();
        List<ItemStack> itemIcons = this.skill.getUnlockItems(this.skillLevel).stream().map(Item::getDefaultStack).toList();
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
