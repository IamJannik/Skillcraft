package net.bmjo.skillcraft.skill;

import net.minecraft.util.Identifier;

public record SkillLevel(Identifier skill, int level) {
    @Override
    public Identifier skill() {
        return skill;
    }

    @Override
    public int level() {
        return level;
    }
}
