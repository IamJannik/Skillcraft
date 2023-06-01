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
import org.apache.commons.compress.utils.Lists;

import java.util.List;

import static net.satisfy.skillcraft.util.SkillcraftUtil.createPacketBuf;

@Environment(EnvType.CLIENT)
public class LevelUpWidget extends DrawableHelper implements Drawable, Element, Selectable {
    private final int x;
    private final int y;
    public final static int WIDTH = 147;
    public final static int HEIGHT = 163;
    private static final Identifier BACKGROUND;
    private Skillset skillset;
    private int currentLevel = 0;
    private final NbtCompound persistentData;
    private final List<LevelButton> levelUpButtons = Lists.newArrayList();
    private final PlayerEntity player;
    private final TextRenderer textRenderer;


    public LevelUpWidget(int x, int y, Identifier skillId, TextRenderer textRenderer) {
        this.x = x;
        this.y = y;
        skillset = SkillLoader.REGISTRY_SKILLS.get(skillId);
        this.textRenderer = textRenderer;

        assert MinecraftClient.getInstance().player != null;
        this.player = MinecraftClient.getInstance().player;
        this.persistentData = ((IEntityDataSaver) this.player).getPersistentData();

        reloadLevel(((IEntityDataSaver)player).getPersistentData().getInt(skillset.getId().toString()));
        createButtons();
    }

    public void setSkillset(Identifier skillId) {
        this.skillset = SkillLoader.REGISTRY_SKILLS.get(skillId);
    }

    private void createButtons() {
        levelUpButtons.add(new LevelButton(this.x + 31, y + 125, levelButton -> levelUp(1), Text.of("+1")));
        levelUpButtons.add(new LevelButton(this.x + 75, y + 125,levelButton -> levelUpMax(), Text.of("+MAX")));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        drawCenteredText(matrices, textRenderer, skillset.getName(), x + WIDTH / 2, y + 28, 10525571);

        renderText(matrices, currentLevel, this.y + 46);

        int nextLevel = currentLevel + 1;
        if (!skillset.isMax(nextLevel)) {
            renderText(matrices, nextLevel, this.y + 84);
            for (LevelButton levelButton : levelUpButtons) {
                levelButton.render(matrices, mouseX, mouseY, delta);
            }
        } else {
            drawCenteredText(matrices, textRenderer, "CONGRATS!", x + WIDTH / 2, y + 96, 10525571);
        }
    }

    private void renderText(MatrixStack matrices, int level, int y) {
        textRenderer.draw(matrices, skillset.getLevelName(level), x + 20, y, 10525571);
        List<OrderedText> descriptionTexts = textRenderer.wrapLines(Text.literal(skillset.getLevelDescription(level)), 140);
        if (descriptionTexts.size() > 0) {
            matrices.push();
            matrices.scale(0.8f, 0.8f, 0.8f);
            textRenderer.draw(matrices, descriptionTexts.get(0), (x + 20) * (1f / 0.8f), (y + textRenderer.fontHeight) * (1f / 0.8f), 10525571);
            matrices.pop();
        }
        renderItems(level, y + textRenderer.fontHeight * 2);
    }

    private void renderItems(int level, int y) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        int item = 0;
        for (Item unlockItem : skillset.getUnlockItems(level)) {
            itemRenderer.renderGuiItemIcon(unlockItem.getDefaultStack(), x + 20 * (item + 1), y);
            item++;
            if (item >= 5) return;
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

    public void reloadLevel() {
        reloadLevel(persistentData.getInt(skillset.getId().toString()));
    }

    public void reloadLevel(int level) {
        currentLevel = level;
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
        }
    }

    private void levelUpMax() {
        int currentLevel = persistentData.getInt(skillset.getId().toString());
        int amount = skillset.getLevelAmount(currentLevel, player.experienceLevel, player.isCreative());

        levelUp(amount);
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
