package net.satisfy.skillcraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.SkillcraftIdentifier;
import net.satisfy.skillcraft.json.SkillLoader;

public class SkillBookScreen extends Screen {

    private SkillBookLeftSide leftSide;
    private SkillBookRightSide rightSide;

    protected int x;
    protected int y;
    protected final int backgroundWidth = 256;
    protected final int backgroundHeight = 128;
    private static final Identifier BACKGROUND;
    private Identifier currentSkill;
    public SkillBookScreen() {
        super(Text.literal("SKILLS").formatted(Formatting.RED));
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        this.addDrawableChild(leftSide = new SkillBookLeftSide(this.x, this.y, SkillLoader.REGISTRY_SKILLS));
        this.addDrawableChild(rightSide = new SkillBookRightSide(this.x + 128, this.y, new SkillcraftIdentifier("combat"), this.textRenderer)); //TODO erst wenn Skill ausgeählt wird
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        leftSide.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        this.drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    static {
        BACKGROUND = new SkillcraftIdentifier("textures/skills/gui/left.png");
    }
}
