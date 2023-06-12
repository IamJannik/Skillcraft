package net.bmjo.skillcraft.skill;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record SkillLevel(Identifier skill, int level) implements Comparable<SkillLevel> {
    @Override
    public Identifier skill() {
        return this.skill;
    }

    @Override
    public int level() {
        return this.level;
    }

    @Override
    public String toString() {
        return this.skill.toString() + ": " + this.level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkillLevel skillLevel)) return false;
        return this.skill.equals(skillLevel.skill) && this.level == skillLevel.level();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.skill, this.level);
    }

    @Override
    public int compareTo(@NotNull SkillLevel skillLevel) {
        int i = Integer.compare(this.level, skillLevel.level);
        if (i == 0) {
            i = skillLevel.skill.compareTo(this.skill);
        }

        return i;
    }
}
