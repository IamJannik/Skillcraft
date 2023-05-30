package net.satisfy.skillcraft.client.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.satisfy.skillcraft.skill.Skillset;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CantUseToast implements Toast {
    private static final Identifier TEXTURE = new Identifier("textures/gui/toasts.png");
    private static final int titleColor = 14686475;
    private static final int textColor = 9830400;
    @NotNull
    private final Skillset skill;
    private final Item item;
    private final int level;
    private boolean soundPlayed;

    public CantUseToast(@NotNull Skillset skillset, Item item, int level) {
        this.skill = skillset;
        this.item = item;
        this.level = level;
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) { //TODO variables renmane
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        manager.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight());
        List<OrderedText> lines = manager.getClient().textRenderer.wrapLines(Text.literal("Needs " + skill.getName() + " lv. " + level + "."), 125);
        if (lines.size() == 1) {
            manager.getClient().textRenderer.draw(matrices, item.getName(), 30.0F, 7.0F, titleColor);
            manager.getClient().textRenderer.draw(matrices, lines.get(0), 30.0F, 18.0F, textColor);
        } else {
            if (startTime < 1500L) {
                int k = MathHelper.floor(MathHelper.clamp((float)(1500L - startTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                manager.getClient().textRenderer.draw(matrices, item.getName(), 30.0F, 11.0F, titleColor | k);
            } else {
                int k = MathHelper.floor(MathHelper.clamp((float)(startTime - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int halfHeight = this.getHeight() / 2;
                int y = halfHeight - lines.size() * 9 / 2;

                for (Iterator<OrderedText> lineIterator = lines.iterator(); lineIterator.hasNext(); y += 9) {
                    OrderedText line = lineIterator.next();
                    manager.getClient().textRenderer.draw(matrices, line, 30.0F, (float) y, textColor | k);
                    Objects.requireNonNull(manager.getClient().textRenderer);
                }
            }
        }

        if (!this.soundPlayed) {
            this.soundPlayed = true;
            manager.getClient().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_PACKED_MUD_PLACE, 1.0F, 1.0F));
        }

        manager.getClient().getItemRenderer().renderInGui(item.getDefaultStack(), 8, 8);
        return startTime >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }

    public void reload() {
        this.soundPlayed = false;
    }

    @Override
    public Object getType() {
        return this;
    }
}
