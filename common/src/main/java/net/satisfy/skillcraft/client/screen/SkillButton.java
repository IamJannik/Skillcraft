package net.satisfy.skillcraft.client.screen;

import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SkillButton extends TexturedButtonWidget {
    public final static int SKILL_BUTTON_WIDTH = 32;
    public final static int SKILL_BUTTON_HEIGHT = 32;
    public SkillButton(int x, int y, Identifier texture, PressAction pressAction, Text text) {
        super(x, y, SKILL_BUTTON_WIDTH, SKILL_BUTTON_HEIGHT, 0, 0, 32, texture, 32, 64, pressAction, text);
    }
}
