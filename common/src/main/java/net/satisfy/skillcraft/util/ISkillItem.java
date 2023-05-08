package net.satisfy.skillcraft.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

public interface ISkillItem {
    boolean hasRequiredLevel(PlayerEntity player, Item item);
    int getRequiredLevel();
    void setRequiredLevel(int level);

}
