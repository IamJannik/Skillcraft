package net.satisfy.skillcraft.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.SkillcraftIdentifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class SkillsScrollWidget extends SkillcraftScrollWidget {
    private static final Identifier BACKGROUND;
    private final List<SkillButton> skillButtons;

    public SkillsScrollWidget(int x, int y, List<SkillButton> skillButtons) {
        super(x, y, BACKGROUND);
        this.skillButtons = skillButtons;
    }

    @Override
    protected boolean contentClicked(double mouseX, double mouseY, int button) {
        for (SkillButton skillButton : this.skillButtons) {
            if (skillButton.mouseClicked(mouseX, mouseY + scrollY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isOverScroll(double mouseX, double mouseY) {
        return mouseX >= (double)(this.x + this.width - 19) && mouseX <= (double)(this.x + this.width - 19 + 4) && mouseY >= (double)this.y + 45 && mouseY < (double)(this.y + 127 + 2);
    }

    @Override
    protected boolean overflows() {
        return getContentsHeight() > (SkillButton.HEIGHT + 5) * 3;
    }

    @Override
    protected int getContentsHeight() {
        return (SkillButton.HEIGHT + 5) * ((this.skillButtons.size() - 1) / 3 + 1);
    }

    @Override
    protected int getScrollWindowHeight() {
        return 127 - 46;
    }

    @Override
    protected void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
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
            int scrollFieldHeigth = this.getScrollWindowHeight() + 2;
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

    static {
        BACKGROUND = new SkillcraftIdentifier("textures/gui/skillcraft_book_left.png");
    }
}
