package net.bmjo.skillcraft.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.bmjo.skillcraft.SkillcraftIdentifier;

public class LevelButton extends TexturedButtonWidget {
    private final static Identifier TEXTURE = new SkillcraftIdentifier("textures/gui/skillcraft_book_right.png");
    public LevelButton(int x, int y, PressAction pressAction, Text text) {
        super(x, y, 77, 15, 153, 30, 19, TEXTURE, 256, 256, pressAction, text);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        super.renderButton(matrices, mouseX, mouseY, delta);

        textRenderer.draw(matrices, "Level UP", this.x + 4, this.y + 4, 0xffffff);
        Text message = this.getMessage();
        textRenderer.draw(matrices, message, this.x + 65 - (message.getString().length() * 6), this.y + 4, 0xffffff);
    }
}
