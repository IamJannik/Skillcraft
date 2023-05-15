package net.satisfy.skillcraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.SkillcraftIdentifier;
import net.satisfy.skillcraft.json.SkillLoader;
import net.satisfy.skillcraft.networking.SkillcraftNetworking;
import net.satisfy.skillcraft.skill.Skillset;
import net.satisfy.skillcraft.util.IEntityDataSaver;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

import static net.satisfy.skillcraft.util.SkillcraftUtil.createPacketBuf;

public class SkillLevelWidget extends DrawableHelper implements Drawable, Element, Selectable {
    private final Identifier skillId;
    private final Skillset skillset;
    private final TextRenderer textRenderer;
    private final PlayerEntity player;
    private final TextFieldWidget currentLevel;
    private TextFieldWidget nextLevel;
    private final List<LevelButton> levelUpButtons = Lists.newArrayList();
    private final NbtCompound persistentData;
    private final int x;
    private final int y;
    public final static int WIDTH = 147;
    public final static int HEIGHT = 163;
    private static final Identifier BACKGROUND;

    public SkillLevelWidget(int x, int y, Identifier skillId, TextRenderer textRenderer) {
        this.x = x;
        this.y = y;
        this.skillId = skillId;
        skillset = SkillLoader.REGISTRY_SKILLS.get(skillId);
        this.textRenderer = textRenderer;

        currentLevel = new TextFieldWidget(textRenderer, this.x + 24, this.y + 48, 103, 33, Text.of("Ups, something went wrong"));
        currentLevel.setDrawsBackground(false);

        nextLevel = new TextFieldWidget(textRenderer, this.x + 24, this.y + 84, 103, 33, Text.of("Ups, something went wrong"));
        nextLevel.setDrawsBackground(false);

        assert MinecraftClient.getInstance().player != null;
        this.player = MinecraftClient.getInstance().player;
        this.persistentData = ((IEntityDataSaver) this.player).getPersistentData(); //TODO when join sync

        reloadText(persistentData.getInt(this.skillId.toString()));
        createButtons();
    }

    private void reloadText(int level) {
        currentLevel.setText(skillset.getLevelDescription(level));
        nextLevel.setText(skillset.getLevelDescription(level + 1));
    }

    private void createButtons() {
        levelUpButtons.add(new LevelButton(this.x + 31, y + 125, levelButton -> levelUP(1), Text.of("+1")));
        levelUpButtons.add(new LevelButton(this.x + 75, y + 125,levelButton -> levelUP(-1), Text.of("+MAX")));//TODO MAX
    }

    private void levelUP(int amount) {
        boolean creative = player.isCreative();
        int level = persistentData.getInt(skillId.toString());
        int cost = skillset.getLevelCost(level, amount);

        if (creative || cost <= player.experienceLevel) {
            if (!creative) {
                player.addExperienceLevels(-cost);
            }

            reloadText(level + amount);

            PacketByteBuf buf = createPacketBuf();
            buf.writeString(skillId.toString());
            buf.writeInt(amount);
            buf.writeInt(cost);
            NetworkManager.sendToServer(SkillcraftNetworking.SKILL_LEVEL_ID, buf);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        drawCenteredText(matrices, textRenderer, skillset.getName(), x + WIDTH / 2, y + 28, 10525571);
        currentLevel.render(matrices, mouseX, mouseY, delta);
        nextLevel.render(matrices, mouseX, mouseY, delta);


        for (LevelButton levelButton : levelUpButtons) {
            levelButton.render(matrices, mouseX, mouseY, delta);
        }
    }

    public void renderBackground(MatrixStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        this.drawTexture(matrices, this.x, this.y, 0, 0, WIDTH, HEIGHT);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (LevelButton levelButton : this.levelUpButtons) {
            if (levelButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    static {
        BACKGROUND = new SkillcraftIdentifier("textures/gui/skillcraft_book_right.png");
    }
}
