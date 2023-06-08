package net.bmjo.skillcraft.skill;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SkillLevel {
    public final int level;
    @Nullable
    private final String name;
    @Nullable
    private final String description;
    private final ArrayList<Item> unlockItems;
    private final ArrayList<Block> unlockBlocks;
    @Nullable
    private final Item reward;

    public SkillLevel(int level, @Nullable String name, @Nullable String description, @Nullable ArrayList<Item> unlockItems, @Nullable ArrayList<Block> unlockBlocks, @Nullable Item reward) {
        this.level = level;
        this.name = name;
        this.description = description;
        this.unlockItems = unlockItems == null ? Lists.newArrayList() : unlockItems;
        this.unlockBlocks = unlockBlocks == null ? Lists.newArrayList() : unlockBlocks;
        this.reward = reward;
    }

    public String getName() {
        return name != null ? name : "Level " + level;
    }

    public String getDescription() {
        return description != null ? description : "Add Description in JSON";
    }

    public ArrayList<Item> getUnlockItems() {
        return this.unlockItems;
    }

    public ArrayList<Block> getUnlockBlocks() {
        return this.unlockBlocks;
    }

    @Nullable
    public Item getReward() {
        return this.reward;
    }

    @Override
    public String toString() {
        return "SkillLevel: " +  "level=" + level + ", name='" + getName() + '\'' + ", description='" + getDescription() + '\'' + ';';
    }
}
