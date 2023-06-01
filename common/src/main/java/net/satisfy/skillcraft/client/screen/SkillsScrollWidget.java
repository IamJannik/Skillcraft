package net.satisfy.skillcraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.satisfy.skillcraft.SkillcraftIdentifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class SkillsScrollWidget extends ClickableWidget {
    private static final Identifier BACKGROUND;
    private final List<SkillButton> skillButtons; //TODO currently only up to 15 skills :/
    private double scrollY;
    private boolean scrollbarDragged;
    private final int scrollFieldHeight = 127 - 46;

    public SkillsScrollWidget(int x, int y, List<SkillButton> skillButtons) {
        super(x, y, 147, 163, Text.of(""));
        this.skillButtons = skillButtons;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.visible) {
            return false;
        }
        boolean over = this.isOver(mouseX, mouseY);
        boolean overScroll = this.isOverScroll(mouseX, mouseY);
        this.setFocused(over || overScroll);
        if (over || overScroll) {
            if (overScroll && button == 0) {
                this.scrollbarDragged = true;
                return true;
            }
            for (SkillButton skillButton : this.skillButtons) {
                if (skillButton.mouseClicked(mouseX, mouseY + scrollY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.scrollbarDragged = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.visible && this.isFocused() && this.overflows() && this.scrollbarDragged) {
            if (mouseY < (double)this.y) {
                this.setScrollY(0.0);
            } else if (mouseY > (double)(this.y + this.height)) {
                this.setScrollY(this.getMaxScrollY());
            } else {
                int scrollbarHeight = this.getScrollbarHeight();
                double scroll = scrollbarHeight == 0 ? 1 : Math.max(1, this.getMaxScrollY() / (this.scrollFieldHeight - scrollbarHeight));
                this.setScrollY(this.scrollY + deltaY * scroll);
            }

            return true;
        }
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.visible && this.isOver(mouseX, mouseY) && this.overflows()) {
            int deltaYPerScroll = 32;
            this.setScrollY(this.scrollY - amount * deltaYPerScroll);
            return true;
        }
        return false;
    }

    private boolean overflows() {
        return getContentsHeight() > SkillButton.SKILL_BUTTON_HEIGHT + 5 * 3;
    }

    private void setScrollY(double scrollY) {
        this.scrollY = MathHelper.clamp(scrollY, 0.0, this.getMaxScrollY());
    }

    protected int getMaxScrollY() {
        return Math.max(0, this.scrollFieldHeight + 2);
    }

    private boolean isOver(double mouseX, double mouseY) {
        return mouseX >= (double)this.x && mouseX < (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY < (double)(this.y + this.height);
    }

    private boolean isOverScroll(double mouseX, double mouseY) {
        return mouseX >= (double)(this.x + this.width - 19) && mouseX <= (double)(this.x + this.width - 19 + 4) && mouseY >= (double)this.y + 45 && mouseY < (double)(this.y + 127 + 2);
    }

    private int getScrollbarHeight() {
        return MathHelper.clamp((int)((float)((this.scrollFieldHeight + 2) * (this.scrollFieldHeight + 2)) / (float)this.getContentsHeight()), 16, this.scrollFieldHeight + 2);
    }

    private int getContentsHeight() {
        return (SkillButton.SKILL_BUTTON_HEIGHT + 5) * ((this.skillButtons.size() - 1) / 3 + 1);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.hovered = isOver(mouseX, mouseY);

        renderForeground(matrices, mouseX, mouseY, delta);
    }

    public void renderBackground(MatrixStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        this.drawTexture(matrices, this.x, this.y, 0, 0, this.width, this.height);
    }

    public void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, "SKILLS", this.x + this.width / 2, this.y + 32, 10525571);
        enableScissor(this.x + 26, this.y + 46, this.x + 124, this.y + 127);
        matrices.push();
        matrices.translate(0.0, -this.scrollY, 0.0);
        this.renderButtons(matrices, mouseX, mouseY + (int)this.scrollY, delta);
        matrices.pop();
        disableScissor();
        this.renderScrollButton(matrices);
    }

    private void renderButtons(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (SkillButton skillButton : this.skillButtons) {
            skillButton.render(matrices, mouseX, mouseY, delta);
        }
    }

    private void renderScrollButton(MatrixStack matrices) {
        if (this.overflows()) {
            int scrollFieldHeigth = this.scrollFieldHeight + 2;
            int height = this.getScrollbarHeight();
            int left = this.x + this.width - 19;
            int right = this.x + this.width - 19 + 4;
            int top = Math.max(this.y + 45, (int) this.scrollY * (scrollFieldHeigth - height) / this.getMaxScrollY() + this.y + 45);
            int bottom = top + height;
            drawHorizontalLine(matrices, left, right, top, 0xffA09B83);//TOP
            drawVerticalLine(matrices, left, top, bottom, 0xffA09B83);//LEFT
            drawHorizontalLine(matrices, left, right, bottom, 0xffA09B83);//BOTTOM
            drawVerticalLine(matrices, right, top, bottom, 0xffA09B83);//RIGHT
        }
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    static {
        BACKGROUND = new SkillcraftIdentifier("textures/gui/skillcraft_book_left.png");
    }
}
