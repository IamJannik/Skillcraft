package net.satisfy.skillcraft.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.satisfy.skillcraft.util.IEntityDataSaver;
import net.satisfy.skillcraft.util.ISkillItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin implements ISkillItem {
    private int requiredLevel = 0;
    private String skillKey = "combat";

    @Override
    public boolean hasRequiredLevel(PlayerEntity player, Item item) {
        NbtCompound nbtCompound = ((IEntityDataSaver)player).getPersistentData();
        boolean enough = nbtCompound.getInt(skillKey) >= requiredLevel;
        player.sendMessage(enough ?
                Text.literal("You have reached the required level to use " + item.getName() + ". (" + requiredLevel + ")").formatted(Formatting.GOLD) :
                Text.literal("You haven't reached the required level to use " + item.getName() + ". (" + requiredLevel + ")").formatted(Formatting.RED));
        return enough;
    }

    @Override
    public int getRequiredLevel() {
        return requiredLevel;
    }

    @Override
    public void setRequiredLevel(int level) {
        this.requiredLevel = level;
    }

    @Override
    public void setSkillKey(String skillKey) {
        this.skillKey = skillKey;
    }
}
