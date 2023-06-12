package net.bmjo.skillcraft.client.screen;

import com.google.common.collect.Lists;
import net.bmjo.skillcraft.Skillcraft;
import net.bmjo.skillcraft.client.SkillcraftClient;
import net.bmjo.skillcraft.skill.Skill;
import net.bmjo.skillcraft.util.SkillComparator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SkillBookScreen extends Screen {
    protected int x;
    protected int y;
    public final static int WIDTH = 294;
    public final static int HEIGHT = 147;
    private Identifier currentSkill;
    private LevelsScrollWidget skillLevelsWidget;

    public SkillBookScreen() {
        super(Text.translatable("skillcraft.skillbook.skills").formatted(Formatting.GOLD));
        this.currentSkill = SkillcraftClient.lastBookSkill;
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - WIDTH) / 2;
        this.y = (this.height - HEIGHT) / 2;

        SkillsScrollWidget skillsWidget = new SkillsScrollWidget(this.x, this.y, this.createSkillButtons(Skillcraft.SKILLS));
        this.skillLevelsWidget = new LevelsScrollWidget(this.x + WIDTH / 2, this.y, this.currentSkill, this.textRenderer);
        this.reloadSkill(this.currentSkill);

        this.addDrawableChild(skillsWidget);
        this.addDrawableChild(this.skillLevelsWidget);
    }

    private List<SkillButton> createSkillButtons(Map<Identifier, Skill> skills) {
        List<SkillButton> skillButtons = Lists.newArrayList();
        int skillNr = 0;
        for (Identifier identifier : skills.keySet().stream().sorted(new SkillComparator()).toList()) {
            SkillButton skillButton = new SkillButton(
                    this.x + 26 + (SkillButton.WIDTH + 4) * (skillNr % 3),
                    this.y + 46 + (SkillButton.HEIGHT + 4) * (skillNr / 3),
                    (button) -> this.reloadSkill(identifier),
                    Text.literal(skills.get(identifier).getName())
            );
            skillButtons.add(skillButton);
            skillNr++;
        }
        return skillButtons;
    }

    private void reloadSkill(Identifier skill) {
        this.currentSkill = skill;
        SkillcraftClient.lastBookSkill = this.currentSkill;
        this.skillLevelsWidget.setSkill(skill);
    }
}
