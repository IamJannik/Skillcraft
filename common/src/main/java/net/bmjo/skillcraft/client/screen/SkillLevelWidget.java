package net.bmjo.skillcraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bmjo.skillcraft.SkillcraftIdentifier;
import net.bmjo.skillcraft.skill.Skill;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class SkillLevelWidget extends DrawableHelper implements Drawable, Element, Selectable {
    private static final Identifier BACKGROUND;
    public final static int WIDTH = 109;
    public final static int HEIGHT = 38;
    private final int x;
    private final int y;
    private final Skill skill;
    private final int level;
    private final TextRenderer textRenderer;
    private boolean current;
    private boolean locked;


    public SkillLevelWidget(int x, int y, Skill skill, int level, TextRenderer textRenderer) {
        this.x = x;
        this.y = y;
        this.skill = skill;
        this.level = level;
        this.textRenderer = textRenderer;
    }

    public void setState(boolean current, boolean locked) {
        this.current = current;
        this.locked = locked;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.textRenderer.draw(matrices, this.skill.getLevelName(this.level), this.x + 4, this.y + 3, 0xA09B83);
        List<OrderedText> descriptionTexts = this.textRenderer.wrapLines(Text.literal(this.skill.getLevelDescription(this.level)), 135);
        if (!descriptionTexts.isEmpty()) {
            matrices.push();
            matrices.scale(0.8f, 0.8f, 1.0F);
            this.textRenderer.draw(matrices, descriptionTexts.get(0), (this.x + 4) * (1f / 0.8f), (this.y + 3 + this.textRenderer.fontHeight) * (1f / 0.8f), 0xA09B83);
            matrices.pop();
        }
    }

    private void renderBackground(MatrixStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        drawTexture(matrices, this.x, this.y, 0, this.locked ? HEIGHT : this.current ? HEIGHT * 2 : 0, WIDTH, HEIGHT, WIDTH, HEIGHT * 3);
    }

    public void renderItems(double scrollY) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        RenderSystem.applyModelViewMatrix();
        int item = 0;
        for (Item unlockItem : this.skill.getUnlockItems(this.level)) {
            itemRenderer.renderGuiItemIcon(unlockItem.getDefaultStack(), this.x + 4 + 20 * (item), (int) ((this.y + this.textRenderer.fontHeight * 2 + 1) - scrollY));
            item++;
            if (item >= 5) break;
        }
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    static {
        BACKGROUND = new SkillcraftIdentifier("textures/gui/skillcraft_level.png");
    }
}
