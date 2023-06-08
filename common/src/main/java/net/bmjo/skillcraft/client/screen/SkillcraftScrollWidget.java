package net.bmjo.skillcraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public abstract class SkillcraftScrollWidget extends ClickableWidget {
    private final Identifier BACKGROUND;
    protected int scrollY;
    private boolean scrollbarDragged;
    private int startEase;
    private int time;

    public SkillcraftScrollWidget(int x, int y, Identifier background) {
        super(x, y, 147, 163, Text.of(""));
        this.BACKGROUND = background;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.hovered = isOver(mouseX, mouseY);

        this.ease();
        renderForeground(matrices, mouseX, mouseY, delta);
    }

    private void ease() {
        int duration = 20;
        if (this.scrollY % this.getYPerScroll() == 0 || scrollbarDragged || time > duration) {
            this.time = 0;
            this.startEase = 0;
            return;
        }
        if (this.time == 0) {
            this.startEase = scrollY;
        }

        int modEase = this.startEase % this.getYPerScroll();
        if (modEase < getYPerScroll() / 2) {
            int difference = getYPerScroll() / 2 - modEase;
            this.scrollY = startEase - (int)(difference * easeInOutSine((double)time / duration));
        } else {
            int difference = getYPerScroll() - modEase;
            this.scrollY = startEase + (int)(difference * easeInOutSine((double)time / duration));
        }

        this.time++;
    }

    private void renderBackground(MatrixStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        this.drawTexture(matrices, this.x, this.y, 0, 0, this.width, this.height);
    }

    @Override
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
            return contentClicked(mouseX, mouseY, button);
        }
        return false;
    }

    protected boolean contentClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.scrollbarDragged = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.visible && this.isFocused() && this.overflows() && this.scrollbarDragged) {
            if (mouseY < (double)this.y) {
                this.setScrollY(0);
            } else if (mouseY > (double)(this.y + this.height)) {
                this.setScrollY(this.getMaxScrollY());
            } else {
                int scrollbarHeight = this.getScrollbarHeight();
                int scroll = scrollbarHeight == 0 ? 1 : Math.max(1, this.getMaxScrollY() / (this.getScrollWindowHeight() - scrollbarHeight));
                this.setScrollY(this.scrollY + (int) (deltaY * scroll));
                this.time = 0;
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.visible && this.isOver(mouseX, mouseY) && this.overflows()) {
            this.setScrollY(this.scrollY - (int)(amount * getYPerScroll()));
            this.time = 0;
            return true;
        }
        return false;
    }

    private void setScrollY(int scrollY) {
        this.scrollY = MathHelper.clamp(scrollY, 0, this.getMaxScrollY());
    }

    protected int getMaxScrollY() {
        return Math.max(0, this.getContentsHeight());
    }

    protected boolean isOver(double mouseX, double mouseY) {
        return mouseX >= (double)this.x && mouseX < (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY < (double)(this.y + this.height);
    }

    protected int getScrollbarHeight() {
        return MathHelper.clamp((int)((float)((this.getScrollWindowHeight() + 2) * (this.getScrollWindowHeight() + 2)) / (float)this.getContentsHeight()), 16, this.getScrollWindowHeight() + 2);
    }

    private double easeInOutSine(double time) {
        return -0.5 * (Math.cos(Math.PI * time) - 1);
    }

    abstract protected void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta);
    abstract protected boolean overflows();
    abstract protected boolean isOverScroll(double mouseX, double mouseY);
    abstract protected int getYPerScroll();
    abstract protected int getContentsHeight();
    abstract protected int getScrollWindowHeight();

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
