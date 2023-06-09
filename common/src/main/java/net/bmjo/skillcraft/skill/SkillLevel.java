package net.bmjo.skillcraft.skill;

import net.minecraft.util.Identifier;

public record SkillLevel(Identifier skill, int level) {
    public SkillLevel(Identifier skill, int level) {
        this.skill = skill;
        this.level = level;
    }

    @Override
    public Identifier skill() {
        return skill;
    }

    @Override
    public int level() {
        return level;
    }
}
