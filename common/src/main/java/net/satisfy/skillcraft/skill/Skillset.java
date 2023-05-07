package net.satisfy.skillcraft.skill;

import java.util.ArrayList;

public class Skillset {
    public final String name;
    private final ArrayList<SkillLevel> levels;

    public Skillset(String name, ArrayList<SkillLevel> levels) {
        this.name = name;
        this.levels = levels;
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
        return "Skillset: " + "name: " + name + '\'' + ", levels: " + levels + ';';
    }
}
