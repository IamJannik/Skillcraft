package net.bmjo.skillcraft.skill;

import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public record Level(int level, @Nullable String name, @Nullable String description, Set<Item> unlockItems,
                    @Nullable Item reward) {
    public Level(int level, @Nullable String name, @Nullable String description, @Nullable Set<Item> unlockItems, @Nullable Item reward) {
        this.level = level;
        this.name = name;
        this.description = description;
        this.unlockItems = unlockItems == null ? Sets.newHashSet() : unlockItems;
        this.reward = reward;
    }

    @Override
    public int level() {
        return this.level;
    }

    @Override
    public String name() {
        return this.name != null ? this.name : "Level " + this.level;
    }

    @Override
    public String description() {
        return this.description != null ? this.description : "";
    }

    @Override
    public String toString() {
        return "Level: " + "level=" + this.level + ", name='" + this.name() + '\'' + ", description='" + this.description() + '\'' + ';';
    }
}
