package net.bmjo.skillcraft.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public interface ISkillBlock {
    boolean hasRequiredLevel(PlayerEntity player, Block block);
    void setRequiredLevel(int level);
    void setSkillKey(Identifier key);
}
