package net.satisfy.skillcraft.client.screen;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
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

@Environment(EnvType.CLIENT)
public class LevelScrollWidget extends SkillcraftScrollWidget {
    private static final Identifier BACKGROUND;
    private List<SkillLevelWidget> levels;
    private Skillset skill;
    private int currentLevel = 0;
    private final NbtCompound persistentData;
    private final LevelButton levelUpButton;
    private final LevelButton levelUpMaxButton;
    private final PlayerEntity player;
    private final TextRenderer textRenderer;

    public LevelScrollWidget(int x, int y, Identifier skillId, TextRenderer textRenderer) {
        super(x, y, BACKGROUND);
        this.textRenderer = textRenderer;

        assert MinecraftClient.getInstance().player != null;
        this.player = MinecraftClient.getInstance().player;
        this.persistentData = ((IEntityDataSaver) this.player).getPersistentData();

        this.setSkill(skillId);

        levelUpButton = new LevelButton(this.x + 31, y + 126, levelButton -> levelUp(1), Text.of("+1"));
        levelUpMaxButton = new LevelButton(this.x + 75, y + 126,levelButton -> levelUp(maxLevelUp()), Text.of("+MAX"));
        reloadButtons();
    }

    public void setSkill(Identifier skillId) {
        this.skill = SkillLoader.REGISTRY_SKILLS.get(skillId);
        createLevels();
        this.reloadLevel(((IEntityDataSaver)player).getPersistentData().getInt(skill.getId().toString()));
    }

    private void levelUp(int amount) {
        if (amount <= 0) {
            return;
        }
        boolean creative = player.isCreative();
        int currentLevel = persistentData.getInt(skill.getId().toString());
        int cost = skill.getLevelCost(currentLevel, amount);

        if (!skill.isMax(currentLevel + amount) && (creative || cost <= player.experienceLevel)) {
            if (!creative) {
                player.addExperienceLevels(-cost);
            }

            PacketByteBuf buf = createPacketBuf();
            buf.writeString(skill.getId().toString());
            buf.writeInt(amount);
            buf.writeInt(creative ? 0 : cost);
            NetworkManager.sendToServer(SkillcraftNetworking.SKILL_LEVEL_UP_ID, buf);

            this.reloadLevel(currentLevel + amount);
            this.reloadButtons();
        }
    }

    private int maxLevelUp() {
        int currentLevel = persistentData.getInt(skill.getId().toString());
        return skill.getLevelAmount(currentLevel, player.experienceLevel, player.isCreative());
    }

    private void createLevels() {
        List<SkillLevelWidget> skillLevels = Lists.newArrayList();
        for (int level = 0; level <= skill.getMaxLevel(); level++) {
            skillLevels.add(new SkillLevelWidget(this.x + 17, (this.y + 42) + (SkillLevelWidget.HEIGHT + 4) * level, skill, level, textRenderer));
        }
        this.levels = skillLevels;
    }

    private void reloadLevel(int level) {
        currentLevel = level;
        this.scrollY = currentLevel * 42;
        int widget = 0;
        for (SkillLevelWidget levelWidget : levels) {
            if (widget == currentLevel) {
                levelWidget.setState(true, false);
            } else {
                levelWidget.setState(false, widget > currentLevel);
            }
            widget++;
        }
    }

    private void reloadButtons() {
        int costOne = skill.getLevelCost(currentLevel, 1);
        levelUpButton.setMessage( Text.literal(!player.isCreative() && costOne > player.experienceLevel ? costOne + "l" : "+1" + "/" + costOne + "l"));
        int amount = maxLevelUp();
        levelUpMaxButton.setMessage(Text.literal(amount == 0 ? "--" : "+" + amount + "/" + skill.getLevelCost(currentLevel, amount) + "l"));
    }

    //SCROLL

    @Override
    protected boolean contentClicked(double mouseX, double mouseY, int button) {
        return levelUpButton.mouseClicked(mouseX, mouseY, button) || levelUpMaxButton.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected boolean isOverScroll(double mouseX, double mouseY) {
        return mouseX >= (double)(this.x + this.width - 19) && mouseX <= (double)(this.x + this.width - 19 + 4) && mouseY >= (double)this.y + 42 && mouseY < (double)(this.y + 121);
    }

    @Override
    protected boolean overflows() {
        return getContentsHeight() > (SkillLevelWidget.HEIGHT + 5) * 2;
    }

    @Override
    protected int getContentsHeight() {
        return (SkillLevelWidget.HEIGHT + 5) * ((this.levels.size() - 1) + 1);
    }

    @Override
    protected int getScrollWindowHeight() {
        return 78;
    }

    @Override
    protected void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        itemRenderer.renderGuiItemIcon(skill.getIcon(), this.x + this.width / 2 - 10, y + 6);

        drawCenteredText(matrices, textRenderer, skill.getName(), x + this.width / 2, y + 28, 0xA09B83);

        enableScissor(this.x + 17, this.y + 42, this.x + 126, this.y + 122);
        matrices.push();
        matrices.translate(0.0, -this.scrollY, 0.0);
        this.renderLevels(matrices, mouseX, mouseY + (int)this.scrollY, delta);
        matrices.pop();
        disableScissor();
        this.renderScrollButton(matrices);
        this.renderButtons(matrices, mouseX, mouseY, delta);
    }

    private void renderLevels(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (SkillLevelWidget levelWidget : this.levels) {
            levelWidget.render(matrices, mouseX, mouseY, delta);
            levelWidget.renderItems(scrollY);
        }
    }

    private void renderScrollButton(MatrixStack matrices) {
        if (this.overflows()) {
            int scrollFieldHeigth = this.getScrollWindowHeight() + 2;
            int height = this.getScrollbarHeight();
            int left = this.x + this.width - 19;
            int right = this.x + this.width - 19 + 4;
            int top = Math.max(this.y + 42, (int) this.scrollY * (scrollFieldHeigth - height) / this.getMaxScrollY() + this.y + 42);
            int bottom = top + height;
            drawHorizontalLine(matrices, left, right, top, 0xffA09B83);//TOP
            drawVerticalLine(matrices, left, top, bottom, 0xffA09B83);//LEFT
            drawHorizontalLine(matrices, left, right, bottom, 0xffA09B83);//BOTTOM
            drawVerticalLine(matrices, right, top, bottom, 0xffA09B83);//RIGHT
        }
    }

    private void renderButtons(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!skill.isMax(currentLevel + 1)) {
            levelUpButton.render(matrices, mouseX, mouseY, delta);
            levelUpMaxButton.render(matrices, mouseX, mouseY, delta);
        }
    }

    static {
        BACKGROUND = new SkillcraftIdentifier("textures/gui/skillcraft_book_right.png");
    }
}
