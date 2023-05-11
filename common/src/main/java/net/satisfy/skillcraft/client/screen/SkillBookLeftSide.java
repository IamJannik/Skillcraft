package net.satisfy.skillcraft.client.screen;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.skill.Skillset;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;

public class SkillBookLeftSide extends ScrollableWidget {

    private final List<SkillButton> buttons = Lists.newArrayList();
    private final int buttonWidth = 32;
    private final int buttonHeight = 32;

    public SkillBookLeftSide(int x, int y, Map<Identifier, Skillset> skillsets) {
        super(x, y, 128, 128, Text.of(""));
        createButtons(skillsets);
    }


    public void createButtons(Map<Identifier, Skillset> skillsets) {
        int skill = 0;
        for (Identifier identifier : skillsets.keySet()) {
            System.out.println(identifier);
            SkillButton skillButton = new SkillButton(
                    x + 5 + (buttonWidth + 5) * (skill % 3),
                    y + 5 + (buttonHeight + 5) * (skill / 3),
                    new Identifier(identifier.getNamespace(),"textures/skills/" + identifier.getPath() + ".png"), //TODO auch andere also ID in Skillset zum Indetifiere mit mod
                    (button) -> openSkill(identifier), //TODO auch zu Indetifiere mit mod
                    Text.literal(skillsets.get(identifier).getName())
            );
            this.buttons.add(skillButton);
            skill++;
        }
    }

    private void openSkill(Identifier id) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean result = super.mouseClicked(mouseX, mouseY, button);
        for (SkillButton skillButton : this.buttons) {
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
        for (SkillButton skillButton : buttons) {
            skillButton.render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    protected int getContentsHeight() {
        return (buttonHeight + 5) * (buttons.size() / 3 + 1);
    }

    @Override
    protected boolean overflows() {
        return true;
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 0;//TODO
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
