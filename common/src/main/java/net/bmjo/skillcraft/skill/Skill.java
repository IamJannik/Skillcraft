package net.bmjo.skillcraft.skill;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Skill {
    private final Identifier id;
    @Nullable
    private final String name;
    @Nullable
    private final String description;
    @Nullable
    private final Item icon;
    private final Map<Integer, SkillLevel> levels;

    public Skill(Identifier id, @Nullable String name, @Nullable String description, @Nullable Item icon, Map<Integer, SkillLevel> levels) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.levels = levels;
    }

    public Identifier getId() {
        return id;
    }

    public String getName() {
        return name == null ? id.getPath() : name;
    }

    public String getDescription() {
        return description == null ? "Add a description to the Json File." : description;
    }

    public ItemStack getIcon() {
        return icon == null ? Items.STONE_PICKAXE.getDefaultStack() : icon.getDefaultStack();
    }

    public int getMaxLevel() {
        return levels.keySet().stream().max(Comparator.comparingInt(x -> x)).orElse(0);
    }

    public boolean isMax(final int level) {
        return level > getMaxLevel();
    }

     public int getLevelCost(int currentLevel, final int amount) {
        int cost = 0;

        for (int i = 0; i < amount; i++) {
            cost += this.nextLevelCost(++currentLevel);
        }

        return cost;
    }

    protected int nextLevelCost(final int level) {
        // Formula for calculating the required xp/level for the next level.
        return isMax(level) ? 0 : ((level * level) / (getMaxLevel() + 2)) + 2;
    }

    public String getLevelName(final int level) {
        return isMax(level) ? "Max Level" : levels.containsKey(level) ? levels.get(level).name() : "Level " + level;
    }

    public String getLevelDescription(final int level) {
        return isMax(level) ? "Congrats, you have reached the max level!" : levels.containsKey(level) ? levels.get(level).description() : "";
    }

    public List<Item> getUnlockItems(final int level) {
        return levels.containsKey(level) ? levels.get(level).unlockItems() : Lists.newArrayList();
    }

    @Override
    public String toString() {
        return "Skill: " + " ID: " + id + " / name: " + getName() +  ", levels: " + levels + ';';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skill skill)) return false;
        return id.equals(skill.id) && Objects.equals(this.getName(), skill.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName());
    }
}
