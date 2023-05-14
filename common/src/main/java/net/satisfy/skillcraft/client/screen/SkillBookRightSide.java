package net.satisfy.skillcraft.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.json.SkillLoader;
import net.satisfy.skillcraft.skill.Skillset;
import net.satisfy.skillcraft.util.IEntityDataSaver;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class SkillBookRightSide extends DrawableHelper implements Drawable, Element, Selectable {
    private final Identifier skillId;
    private final Skillset skillset;
    private final TextRenderer textRenderer;
    private TextFieldWidget currentLevel;
    private Text nextLevel;
    private final List<LevelButton> levelUpButton = Lists.newArrayList();
    private final NbtCompound persistentData;
    private final int x;
    private final int y;

    public SkillBookRightSide(int x, int y, Identifier skillId, TextRenderer textRenderer) {
        this.x = x;
        this.y = y;
        this.skillId = skillId;
        skillset = SkillLoader.REGISTRY_SKILLS.get(skillId);
        this.textRenderer = textRenderer;

        currentLevel = new TextFieldWidget(textRenderer, x + 5, y + 5, 128, 64, Text.of("Ups, something went wrong"));
        currentLevel.setDrawsBackground(false);

        assert MinecraftClient.getInstance().player != null;
        this.persistentData = ((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData();
        reloadText();
        createButtons();
    }

    private void reloadText() {
        int level = persistentData.getInt(skillId.toString());
        currentLevel.setText(skillset.getLevelDescription(level));
        nextLevel = Text.literal(skillset.getLevelDescription(level + 1));
    }

    private void createButtons() {
        int x = 0;
        levelUpButton.add(new LevelButton(this.x + 64 * x++, y , 1, levelButton -> levelUP(1), Text.of("+1")));
        levelUpButton.add(new LevelButton(this.x + 64 * x++, y , 5, levelButton -> levelUP(5), Text.of("+5")));
        levelUpButton.add(new LevelButton(this.x + 64 * x, y , -1,levelButton -> levelUP(-1), Text.of("+MAX")));//TODO MAX
    }

    private void levelUP(int amount) {
        if (canLevelUP()) {
            //TODO
        }
    }

    private boolean canLevelUP() {
        return false;//TODO
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, textRenderer, skillset.getName(), x, y, 1);
        currentLevel.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, nextLevel, x, y + 64, 1);
    }


    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
