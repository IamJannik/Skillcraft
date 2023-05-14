package net.satisfy.skillcraft.client.screen;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class SkillBookLeftSide extends ScrollableWidget {

    private final List<SkillButton> skillButtons;

    public SkillBookLeftSide(int x, int y, List<SkillButton> skillButtons) {
        super(x, y, SkillBookScreen.WIDTH / 2, SkillBookScreen.HEIGHT, Text.of(""));
        this.skillButtons = skillButtons;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean result = super.mouseClicked(mouseX, mouseY, button);
        for (SkillButton skillButton : this.skillButtons) {
            if (skillButton.mouseClicked(mouseX + getScrollY(), mouseY + getScrollY(), button)) {
                return true;
            }
        }
        return result;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //super.render(matrices, mouseX, mouseY, delta);
        this.renderContents(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (SkillButton skillButton : this.skillButtons) {
            skillButton.render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    protected int getContentsHeight() {
        return (SkillButton.SKILL_BUTTON_HEIGHT + 5) * (skillButtons.size() / 3 + 1);
    }

    @Override
    protected boolean overflows() {
        return true;
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 10.0;//TODO
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
