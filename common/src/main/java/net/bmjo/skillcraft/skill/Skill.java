package net.bmjo.skillcraft.skill;

import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Skill {
    private final Identifier id;
    @Nullable
    private final String name;
    @Nullable
    private final String description;
    @Nullable
    private final Item icon;
    private final Map<Integer, Level> levels;

    public Skill(Identifier id, @Nullable String name, @Nullable String description, @Nullable Item icon, Map<Integer, Level> levels) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.levels = levels;
    }

    public Identifier getId() {
        return this.id;
    }

    public String getName() {
        return this.name == null ? this.id.getPath() : this.name;
    }

    public String getDescription() {
        return this.description == null ? "Add a description to the Json File." : this.description;
    }

    public ItemStack getIcon() {
        return this.icon == null ? Items.STONE_PICKAXE.getDefaultStack() : this.icon.getDefaultStack();
    }

    public int getMaxLevel() {
        return this.levels.keySet().stream().max(Comparator.comparingInt(x -> x)).orElse(0);
    }

    public boolean isMax(final int level) {
        return level > this.getMaxLevel();
    }

     public int getLevelCost(int currentLevel, final int amount) {
         int cost = 0;
         int level = currentLevel;

         for (int i = 0; i < amount; i++) {
             cost += this.nextLevelCost(++level);
         }

         return cost;
     }

    protected int nextLevelCost(final int level) {
        // Formula for calculating the required xp/level for the next level.
        return this.isMax(level) ? 0 : ((level * level) / (this.getMaxLevel() + 2)) + 2;
    }

    public String getLevelName(final int level) {
        return this.isMax(level) ? "Max Level" : this.levels.containsKey(level) ? this.levels.get(level).name() : "Level " + level;
    }

    public String getLevelDescription(final int level) {
        return this.isMax(level) ? "Congrats, you have reached the max level!" : this.levels.containsKey(level) ? this.levels.get(level).description() : "";
    }

    public Set<Item> getUnlockItems(final int level) {
        return this.levels.containsKey(level) ? this.levels.get(level).unlockItems() : Sets.newHashSet();
    }

    public Map<Integer, Level> getLevels() {
        return this.levels;
    }

    public void combineSkill(Skill skill) {
        Map<Integer, Level> levels = skill.getLevels();
        for (Level level : levels.values()) {
            this.combineLevel(level);
        }
    }

    private void combineLevel(Level level) {
        int levelNr = level.level();
        if (!this.levels.containsKey(levelNr)) {
            this.levels.put(levelNr, level);
        } else {
            Level originalLevel = this.levels.get(levelNr);
            for (Item item : level.unlockItems()) {
                originalLevel.unlockItems().add(item);
            }
        }
    }

    @Override
    public String toString() {
        return "Skill: " + " ID: " + this.id + " / name: " + this.getName() + ", levels: " + this.levels + ';';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skill skill)) return false;
        return this.id.equals(skill.id) && Objects.equals(this.getName(), skill.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.getName());
    }
}
