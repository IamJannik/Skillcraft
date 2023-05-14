package net.satisfy.skillcraft.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.SkillcraftIdentifier;
import net.satisfy.skillcraft.json.SkillLoader;
import net.satisfy.skillcraft.skill.Skillset;

import java.util.List;
import java.util.Map;

public class SkillBookScreen extends Screen {

    private SkillScrollWidget leftSide;
    private SkillBookRightSide rightSide;
    private final List<SkillButton> skillButtons = Lists.newArrayList();

    protected int x;
    protected int y;
    public final static int WIDTH = 256;
    public final static int HEIGHT = 128;
    private static final Identifier BACKGROUND;
    private Identifier currentSkill = new SkillcraftIdentifier("combat");
    public SkillBookScreen() {
        super(Text.literal("SKILLS").formatted(Formatting.RED));
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - WIDTH) / 2;
        this.y = (this.height - HEIGHT) / 2;

        createSkillButtons(SkillLoader.REGISTRY_SKILLS);
        leftSide = new SkillScrollWidget(this.x, this.y, this.skillButtons);
        rightSide = new SkillBookRightSide(this.x + 128, this.y, currentSkill, this.textRenderer);

        this.addDrawableChild(leftSide);
        this.addDrawableChild(rightSide); //TODO erst wenn Skill ausgeählt wird
    }

    private void createSkillButtons(Map<Identifier, Skillset> skillsets) {
        int skill = 0;
        for (int i = 0; i < skillsets.keySet().size() * 10; i++) {

        for (Identifier identifier : skillsets.keySet()) {
            System.out.println(identifier);
            SkillButton skillButton = new SkillButton(
                    x + 5 + (SkillButton.SKILL_BUTTON_WIDTH + 5) * (skill % 3),
                    y + 5 + (SkillButton.SKILL_BUTTON_HEIGHT + 5) * (skill / 3),
                    new Identifier(identifier.getNamespace(), "textures/skills/" + identifier.getPath() + ".png"),
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
        rightSide = new SkillBookRightSide(this.x + 128, this.y, currentSkill, this.textRenderer);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        this.drawTexture(matrices, this.x, this.y, 0, 0, WIDTH, HEIGHT);
    }

    static {
        BACKGROUND = new SkillcraftIdentifier("textures/skills/gui/left.png");
    }
}
