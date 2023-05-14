package net.satisfy.skillcraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class SkillScrollWidget extends ClickableWidget {
    private final List<SkillButton> skillButtons;
    private double scrollY;
    private boolean scrollbarDragged;

    public SkillScrollWidget(int x, int y, List<SkillButton> skillButtons) {
        super(x, y, SkillBookScreen.WIDTH / 2, SkillBookScreen.HEIGHT, Text.of(""));
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
            boolean result = super.mouseClicked(mouseX, mouseY, button);
            for (SkillButton skillButton : this.skillButtons) {
                if (skillButton.mouseClicked(mouseX, mouseY + scrollY, button)) {
                    return true;
                }
            }
            return result;
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
        if (this.visible && this.isFocused() && this.scrollbarDragged) {
            if (mouseY < (double)this.y) {
                this.setScrollY(0.0);
            } else if (mouseY > (double)(this.y + this.height)) {
                this.setScrollY(this.getMaxScrollY());
            } else {
                int i = this.getScrollbarHeight();
                double d = i == 0 ? 1 : Math.max(1, this.getMaxScrollY() / (this.height - i)); //TODO divide by ZERO
                this.setScrollY(this.scrollY + deltaY * d);
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.visible && this.isOver(mouseX, mouseY)) {
            int deltaYPerScroll = 32;
            this.setScrollY(this.scrollY - amount * deltaYPerScroll);
            return true;
        } else {
            return false;
        }
    }

    protected void setScrollY(double scrollY) {
        this.scrollY = MathHelper.clamp(scrollY, 0.0, this.getMaxScrollY());
    }

    protected int getMaxScrollY() {
        return Math.max(0, this.height);
    }

    private boolean isOver(double mouseX, double mouseY) {
        return mouseX >= (double)this.x && mouseX < (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY < (double)(this.y + this.height); //TODO oder einfach hover?
    }

    private boolean isOverScroll(double mouseX, double mouseY) {
        //TODO over Scroll Btn
        return mouseX >= (double)(this.x + this.width) && mouseX <= (double)(this.x + this.width + 8) && mouseY >= (double)this.y && mouseY < (double)(this.y + this.height);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        enableScissor(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1);
        matrices.push();
        matrices.translate(0.0, -this.scrollY, 0.0);
        this.renderButtons(matrices, mouseX, mouseY + (int)scrollY, delta);
        matrices.pop();
        disableScissor();
        this.renderScrollButton();
    }

    private void renderButtons(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (SkillButton skillButton : this.skillButtons) {
            skillButton.render(matrices, mouseX, mouseY, delta);
        }
    }

    private void renderScrollButton() {
        //TODO
        int i = this.getScrollbarHeight();
        int j = this.x + this.width;
        int k = this.x + this.width + 8;
        int l = Math.max(this.y, (int)this.scrollY * (this.height - i) / this.getMaxScrollY() + this.y);
        int m = l + i;
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(j, m, 0.0).color(128, 128, 128, 255).next();
        bufferBuilder.vertex(k, m, 0.0).color(128, 128, 128, 255).next();
        bufferBuilder.vertex(k, l, 0.0).color(128, 128, 128, 255).next();
        bufferBuilder.vertex(j, l, 0.0).color(128, 128, 128, 255).next();
        bufferBuilder.vertex(j, (m - 1), 0.0).color(192, 192, 192, 255).next();
        bufferBuilder.vertex((k - 1), (m - 1), 0.0).color(192, 192, 192, 255).next();
        bufferBuilder.vertex((k - 1), l, 0.0).color(192, 192, 192, 255).next();
        bufferBuilder.vertex(j, l, 0.0).color(192, 192, 192, 255).next();
        tessellator.draw();
    }

    private int getScrollbarHeight() {
        return MathHelper.clamp((int)((float)(this.height * this.height) / (float)this.getContentsHeight()), 32, this.height);
    }

    protected int getContentsHeight() {
        return (SkillButton.SKILL_BUTTON_HEIGHT + 5) * (skillButtons.size() / 3 + 1);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
