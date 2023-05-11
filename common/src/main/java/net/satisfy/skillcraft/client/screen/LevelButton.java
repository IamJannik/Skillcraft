package net.satisfy.skillcraft.client.screen;

import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.SkillcraftIdentifier;

public class LevelButton extends TexturedButtonWidget {
    private final int levels;
    private final static Identifier TEXTURE = new SkillcraftIdentifier("");
    public LevelButton(int x, int y, int levels, PressAction pressAction, Text text) {
        super(x, y, 32, 32, 0, 0, 32, TEXTURE, 32, 64, pressAction, text);
        this.levels = levels;
    }

    public int getLevels() {
        return levels;
    }
}
