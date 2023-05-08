package net.satisfy.skillcraft.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.satisfy.skillcraft.util.IEntityDataSaver;
import net.satisfy.skillcraft.util.ISkillItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin implements ISkillItem {
    private int requiredLevel = 0;

    @Override
    public boolean hasRequiredLevel(PlayerEntity player, Item item) {
        NbtCompound nbtCompound = ((IEntityDataSaver)player).getPersistentData();
        return nbtCompound.getInt("level") >= requiredLevel;
    }

    @Override
    public int getRequiredLevel() {
        return requiredLevel;
    }

    @Override
    public void setRequiredLevel(int level) {
        this.requiredLevel = level;
    }
}
