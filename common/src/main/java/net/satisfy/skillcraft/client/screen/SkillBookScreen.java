package net.satisfy.skillcraft.client.screen;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.SkillcraftIdentifier;
import net.satisfy.skillcraft.json.SkillLoader;
import net.satisfy.skillcraft.skill.Skillset;
import net.satisfy.skillcraft.util.SkillComparator;

import java.util.List;
import java.util.Map;

public class SkillBookScreen extends Screen {

    private SkillScrollWidget leftSide;
    private SkillLevelWidget rightSide;
    private final List<SkillButton> skillButtons = Lists.newArrayList();

    protected int x;
    protected int y;
    public final static int WIDTH = 294;
    public final static int HEIGHT = 147;
    private Identifier currentSkill;

    public SkillBookScreen() {
        super(Text.literal("SKILLS").formatted(Formatting.GOLD));
        currentSkill = new SkillcraftIdentifier("build");
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - WIDTH) / 2;
        this.y = (this.height - HEIGHT) / 2;

        createSkillButtons(SkillLoader.REGISTRY_SKILLS);
        leftSide = new SkillScrollWidget(this.x, this.y, this.skillButtons);
        reloadSkill();

        this.addDrawableChild(leftSide);
        this.addDrawableChild(rightSide);
    }

    private void createSkillButtons(Map<Identifier, Skillset> skillsets) {
        int skill = 0;
        for (int i = 0; i < 10; i++) {
        for (Identifier identifier : skillsets.keySet().stream().sorted(new SkillComparator()).toList()) {
            SkillButton skillButton = new SkillButton(
                    x + 26 + (SkillButton.SKILL_BUTTON_WIDTH + 4) * (skill % 3),
                    y + 46 + (SkillButton.SKILL_BUTTON_HEIGHT + 4) * (skill / 3),
                    (button) -> openSkill(identifier),
                    Text.literal(skillsets.get(identifier).getName())
            );
            this.skillButtons.add(skillButton);
            skill++;
        }
        }
    }

    private void openSkill(Identifier identifier) {
        this.currentSkill = identifier;
        reloadSkill();
    }

    private void reloadSkill() {
        this.remove(rightSide);
        rightSide = new SkillLevelWidget(this.x + WIDTH / 2, this.y, currentSkill, this.textRenderer); //TODO make SkillLevelWidget dynamic for each skill ( not every time a new one)
        this.addDrawableChild(rightSide);
    }
}
