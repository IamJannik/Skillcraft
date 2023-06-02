package net.satisfy.skillcraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.SkillcraftIdentifier;
import net.satisfy.skillcraft.json.SkillLoader;
import net.satisfy.skillcraft.networking.SkillcraftNetworking;
import net.satisfy.skillcraft.skill.Skillset;
import net.satisfy.skillcraft.util.IEntityDataSaver;

import java.util.List;

import static net.satisfy.skillcraft.util.SkillcraftUtil.createPacketBuf;

@Environment(EnvType.CLIENT)
public class LevelWidget extends DrawableHelper implements Drawable, Element, Selectable {
    private final int x;
    private final int y;
    public final static int WIDTH = 147;
    public final static int HEIGHT = 163;
    private static final Identifier BACKGROUND;
    private Skillset skillset;
    private int currentLevel = 0;
    private final NbtCompound persistentData;
    private final LevelButton levelUpButton;
    private final LevelButton levelUpMaxButton;
    private final PlayerEntity player;
    private final TextRenderer textRenderer;


    public LevelWidget(int x, int y, Identifier skillId, TextRenderer textRenderer) {
        this.x = x;
        this.y = y;
        skillset = SkillLoader.REGISTRY_SKILLS.get(skillId);
        this.textRenderer = textRenderer;

        assert MinecraftClient.getInstance().player != null;
        this.player = MinecraftClient.getInstance().player;
        this.persistentData = ((IEntityDataSaver) this.player).getPersistentData();

        reloadLevel(((IEntityDataSaver)player).getPersistentData().getInt(skillset.getId().toString()));

        levelUpButton = new LevelButton(this.x + 31, y + 126, levelButton -> levelUp(1), Text.of("+1"));
        levelUpMaxButton = new LevelButton(this.x + 75, y + 126,levelButton -> levelUp(maxLevelUp()), Text.of("+MAX"));
        reloadButtons();
    }

    public void setSkillset(Identifier skillId) {
        this.skillset = SkillLoader.REGISTRY_SKILLS.get(skillId);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        itemRenderer.renderGuiItemIcon(skillset.getIcon(), this.x + WIDTH / 2 - 10, y + 6);

        drawCenteredText(matrices, textRenderer, skillset.getName(), x + WIDTH / 2, y + 28, 0xA09B83);

        renderText(matrices, currentLevel, this.y + 44);

        int nextLevel = currentLevel + 1;
        if (!skillset.isMax(nextLevel)) {
            renderText(matrices, nextLevel, this.y + 86);
            levelUpButton.render(matrices, mouseX, mouseY, delta);
            levelUpMaxButton.render(matrices, mouseX, mouseY, delta);
        } else {
            drawCenteredText(matrices, textRenderer, "MAX CONGRATS!", x + WIDTH / 2, y + 96, 0xA09B83);
        }
    }

    private void renderText(MatrixStack matrices, int level, int y) {
        textRenderer.draw(matrices, skillset.getLevelName(level), x + 20, y, 0xA09B83);
        List<OrderedText> descriptionTexts = textRenderer.wrapLines(Text.literal(skillset.getLevelDescription(level)), 135);
        if (descriptionTexts.size() > 0) {
            matrices.push();
            matrices.scale(0.8f, 0.8f, 1.0F);
            textRenderer.draw(matrices, descriptionTexts.get(0), (x + 20) * (1f / 0.8f), (y + textRenderer.fontHeight) * (1f / 0.8f), 0xA09B83);
            matrices.pop();
        }
        renderItems(level, y + textRenderer.fontHeight * 2);
    }

    private void renderItems(int level, int y) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        RenderSystem.applyModelViewMatrix();
        int item = 0;
        for (Item unlockItem : skillset.getUnlockItems(level)) {
            itemRenderer.renderGuiItemIcon(unlockItem.getDefaultStack(), x + 20 * (item + 1), y);
            item++;
            if (item >= 5) break;
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
        return levelUpButton.mouseClicked(mouseX, mouseY, button) || levelUpMaxButton.mouseClicked(mouseX, mouseY, button);
    }

    private void levelUp(int amount) {
        if (amount <= 0) {
            return;
        }
        boolean creative = player.isCreative();
        int currentLevel = persistentData.getInt(skillset.getId().toString());
        int cost = skillset.getLevelCost(currentLevel, amount);

        if (!skillset.isMax(currentLevel + amount) && (creative || cost <= player.experienceLevel)) {
            if (!creative) {
                player.addExperienceLevels(-cost);
            }

            PacketByteBuf buf = createPacketBuf();
            buf.writeString(skillset.getId().toString());
            buf.writeInt(amount);
            buf.writeInt(creative ? 0 : cost);
            NetworkManager.sendToServer(SkillcraftNetworking.SKILL_LEVEL_UP_ID, buf);

            reloadLevel(currentLevel + amount);
            reloadButtons();
        }
    }

    private int maxLevelUp() {
        int currentLevel = persistentData.getInt(skillset.getId().toString());
        return skillset.getLevelAmount(currentLevel, player.experienceLevel, player.isCreative());
    }

    public void reload() {
        reloadLevel();
        reloadButtons();
    }

    private void reloadLevel() {
        reloadLevel(persistentData.getInt(skillset.getId().toString()));

    }

    private void reloadLevel(int level) {
        currentLevel = level;
    }

    private void reloadButtons() {
        int costOne = skillset.getLevelCost(currentLevel, 1);
        levelUpButton.setMessage( Text.literal(!player.isCreative() && costOne > player.experienceLevel ? costOne + "l" : "+1" + "/" + costOne + "l"));
        int amount = maxLevelUp();
        levelUpMaxButton.setMessage(Text.literal(amount == 0 ? "--" : "+" + amount + "/" + skillset.getLevelCost(currentLevel, amount) + "l"));
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
