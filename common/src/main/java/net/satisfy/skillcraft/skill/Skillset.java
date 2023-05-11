package net.satisfy.skillcraft.skill;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class Skillset {
    private final Identifier id;
    @Nullable
    public final String name;
    @Nullable
    private final String description;
    private final ArrayList<SkillLevel> levels;

    public Skillset(Identifier id, @Nullable String name, @Nullable String description, ArrayList<SkillLevel> levels) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public int getMaxLevel() {
        return levels.size() - 1;
    }

    public boolean isMax(int level) {
        return level >= getMaxLevel();
    }

    public int nextLevelCost(int level) {
        // Formula for calculating the required xp/level for the next level.
        return isMax(level) ? ((level * level) / (getMaxLevel() + 2)) + 2 : 0;
    }

    public String getLevelDescription(int level) {
        return isMax(level) ? levels.get(level).getDescription() : "Congrats, you have reached the max level!";
    }

    @Override
    public String toString() {
        return "Skillset: " + " ID: " + id + " / name: " + getName() +  ", levels: " + levels + ';';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skillset skillset)) return false;
        return id.equals(skillset.id) && Objects.equals(this.getName(), skillset.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName());
    }
}
