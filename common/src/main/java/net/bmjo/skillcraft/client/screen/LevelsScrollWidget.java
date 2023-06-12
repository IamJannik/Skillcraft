package net.bmjo.skillcraft.client.screen;

import dev.architectury.networking.NetworkManager;
import net.bmjo.skillcraft.SkillcraftIdentifier;
import net.bmjo.skillcraft.json.SkillLoader;
import net.bmjo.skillcraft.networking.SkillcraftNetworking;
import net.bmjo.skillcraft.skill.Skill;
import net.bmjo.skillcraft.util.IEntityDataSaver;
import net.bmjo.skillcraft.util.SkillcraftUtil;
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
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Environment(EnvType.CLIENT)
public class LevelsScrollWidget extends SkillcraftScrollWidget {
    private static final Identifier BACKGROUND;
    private Skill skill;
    private List<SkillLevelWidget> levels;
    private int currentLevel;
    private final NbtCompound persistentData;
    private final LevelButton levelUpButton;
    private final PlayerEntity player;
    private final TextRenderer textRenderer;

    public LevelsScrollWidget(int x, int y, Identifier skillId, TextRenderer textRenderer) {
        super(x, y, BACKGROUND);
        this.textRenderer = textRenderer;

        assert MinecraftClient.getInstance().player != null;
        this.player = MinecraftClient.getInstance().player;
        this.persistentData = ((IEntityDataSaver) this.player).getPersistentData();

        this.levelUpButton = new LevelButton(this.x + 33, y + 126, levelButton -> this.levelUp(), Text.of("0"));

        this.setSkill(skillId);
    }

    public void setSkill(Identifier skillId) {
        this.skill = SkillLoader.REGISTRY_SKILLS.get(skillId);
        this.createLevels();
        this.reloadLevel(((IEntityDataSaver) this.player).getPersistentData().getInt(this.skill.getId().toString()));
        this.reloadButtons();
    }

    private void levelUp() {
        boolean creative = this.player.isCreative();
        int currentLevel = this.persistentData.getInt(this.skill.getId().toString());
        int cost = this.skill.getLevelCost(currentLevel, 1);

        if (!this.skill.isMax(currentLevel + 1) && (creative || cost <= this.player.experienceLevel)) {
            if (!creative) {
                this.player.addExperienceLevels(-cost);
            }

            PacketByteBuf buf = SkillcraftUtil.createPacketBuf();
            buf.writeString(this.skill.getId().toString());
            buf.writeInt(1);
            buf.writeInt(creative ? 0 : cost);
            NetworkManager.sendToServer(SkillcraftNetworking.SKILL_LEVEL_UP_ID, buf);

            this.reloadLevel(currentLevel + 1);
            this.reloadButtons();
        }
    }

    private void createLevels() {
        List<SkillLevelWidget> skillLevels = Lists.newArrayList();
        for (int level = 0; level <= this.skill.getMaxLevel(); level++) {
            skillLevels.add(new SkillLevelWidget(this.x + 17, (this.y + 42) + (SkillLevelWidget.HEIGHT + 4) * level, this.skill, level, this.textRenderer));
        }
        this.levels = skillLevels;
    }

    private void reloadLevel(int level) {
        this.currentLevel = level;
        this.scrollY = this.currentLevel * 42;
        int widget = 0;
        for (SkillLevelWidget levelWidget : this.levels) {
            if (widget == this.currentLevel) {
                levelWidget.setState(true, false);
            } else {
                levelWidget.setState(false, widget > this.currentLevel);
            }
            widget++;
        }
    }

    private void reloadButtons() {
        int cost = this.skill.getLevelCost(this.currentLevel, 1);
        this.levelUpButton.setMessage(Text.literal(String.valueOf(cost)));
    }

    //SCROLL

    @Override
    protected boolean contentClicked(double mouseX, double mouseY, int button) {
        return this.levelUpButton.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected boolean isOverScroll(double mouseX, double mouseY) {
        return mouseX >= (double)(this.x + this.width - 19) && mouseX <= (double)(this.x + this.width - 19 + 4) && mouseY >= (double)this.y + 42 && mouseY < (double)(this.y + 121);
    }

    @Override
    protected boolean overflows() {
        return this.getContentsHeight() + 2 > (SkillLevelWidget.HEIGHT + 4) * 2;
    }

    @Override
    protected int getYPerScroll() {
        return SkillLevelWidget.HEIGHT + 4;
    }

    @Override
    protected int getContentsHeight() {
        return (SkillLevelWidget.HEIGHT + 4) * (this.levels.size() - 2);
    }//TODO bei 2 geht nicht

    @Override
    protected int getScrollWindowHeight() {
        return 78;
    }

    @Override
    protected void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        itemRenderer.renderGuiItemIcon(this.skill.getIcon(), this.x + this.width / 2 - 10, this.y + 6);

        drawCenteredText(matrices, this.textRenderer, this.skill.getName(), this.x + this.width / 2, this.y + 28, 0xA09B83);

        enableScissor(this.x + 17, this.y + 42, this.x + 126, this.y + 122);
        matrices.push();
        matrices.translate(0.0, -this.scrollY, 0.0);
        this.renderLevels(matrices, mouseX, mouseY + this.scrollY, delta);
        matrices.pop();
        disableScissor();
        this.renderScrollButton(matrices);
        this.renderButtons(matrices, mouseX, mouseY, delta);
    }

    private void renderLevels(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (SkillLevelWidget levelWidget : this.levels) {
            levelWidget.render(matrices, mouseX, mouseY, delta);
            levelWidget.renderItems(this.scrollY);
        }
    }

    private void renderScrollButton(MatrixStack matrices) {
        if (this.overflows()) {
            int scrollFieldHeight = this.getScrollWindowHeight() + 2;
            int height = this.getScrollbarHeight();
            int left = this.x + this.width - 19;
            int right = this.x + this.width - 19 + 4;
            int top = Math.max(this.y + 42, this.scrollY * (scrollFieldHeight - height) / this.getMaxScrollY() + this.y + 42);
            int bottom = top + height;
            this.drawHorizontalLine(matrices, left, right, top, 0xffA09B83);//TOP
            this.drawVerticalLine(matrices, left, top, bottom, 0xffA09B83);//LEFT
            this.drawHorizontalLine(matrices, left, right, bottom, 0xffA09B83);//BOTTOM
            this.drawVerticalLine(matrices, right, top, bottom, 0xffA09B83);//RIGHT
        }
    }

    private void renderButtons(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!this.skill.isMax(this.currentLevel + 1)) {
            this.levelUpButton.render(matrices, mouseX, mouseY, delta);
        }
    }

    static {
        BACKGROUND = new SkillcraftIdentifier("textures/gui/skillcraft_book_right.png");
    }
}
