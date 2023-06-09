package net.bmjo.skillcraft.util;

import net.bmjo.skillcraft.skill.SkillLevel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

public interface ISkillItem {
    boolean hasRequiredLevel(PlayerEntity player, Item item);
    void addSkillLevel(SkillLevel skillLevel);
}
