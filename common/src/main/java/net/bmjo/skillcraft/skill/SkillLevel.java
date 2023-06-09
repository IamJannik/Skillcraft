package net.bmjo.skillcraft.skill;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public record SkillLevel(int level, @Nullable String name, @Nullable String description, ArrayList<Item> unlockItems, ArrayList<Block> unlockBlocks, @Nullable Item reward) {
    public SkillLevel(int level, @Nullable String name, @Nullable String description, @Nullable ArrayList<Item> unlockItems, @Nullable ArrayList<Block> unlockBlocks, @Nullable Item reward) {
        this.level = level;
        this.name = name;
        this.description = description;
        this.unlockItems = unlockItems == null ? Lists.newArrayList() : unlockItems;
        this.unlockBlocks = unlockBlocks == null ? Lists.newArrayList() : unlockBlocks;
        this.reward = reward;
    }

    @Override
    public int level() {
        return level;
    }

    @Override
    public String name() {
        return name != null ? name : "Level " + level;
    }

    @Override
    public String description() {
        return description != null ? description : "Add Description in JSON";
    }

    @Override
    public String toString() {
        return "SkillLevel: " + "level=" + level + ", name='" + name() + '\'' + ", description='" + description() + '\'' + ';';
    }
}
