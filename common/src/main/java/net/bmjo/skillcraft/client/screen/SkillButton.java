package net.bmjo.skillcraft.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.bmjo.skillcraft.SkillcraftIdentifier;

public class SkillButton extends TexturedButtonWidget {
    public final static int WIDTH = 30;
    public final static int HEIGHT = 24;
    public final static Identifier TEXTURE = new SkillcraftIdentifier("textures/gui/skillcraft_book_left.png");

    public SkillButton(int x, int y, PressAction pressAction, Text text) {
        super(x, y, WIDTH, HEIGHT, 154, 36, 28, TEXTURE, 256, 256, pressAction, text);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);
        matrices.push();
        Text skillName = this.getMessage();
        float scale = -0.05f * Math.max(skillName.getString().length(), 1) + 1; //calculate the scale of the Text
        matrices.scale(scale, scale, scale);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        drawCenteredText(
                matrices,
                textRenderer,
                skillName,
                (int) ((this.x + this.width / 2) * (1f / scale)), //center the Text in the Button
                (int) ((this.y + (this.height - textRenderer.fontHeight * scale) / 2) * (1f / scale)), //finds the right height for Text
                -1
        );
        matrices.pop();
    }
}
