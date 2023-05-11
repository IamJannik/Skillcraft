package net.satisfy.skillcraft.client.screen;

import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SkillButton extends TexturedButtonWidget {

    public SkillButton(int x, int y, Identifier texture, PressAction pressAction, Text text) {
        super(x, y, 32, 32, 0, 0, 32, texture, 32, 64, pressAction, text);
    }
}
