package net.satisfy.skillcraft.skill;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SkillLevel {
    public final int level;
    @Nullable
    private final String name;
    @Nullable
    private final String description;

    private final ArrayList<Item> unlock;
    @Nullable
    private final Item reward;

    public SkillLevel(int level, @Nullable String name, @Nullable String description, @Nullable ArrayList<Item> unlock, @Nullable Item reward) {
        this.level = level;
        this.name = name;
        this.description = description;
        this.unlock = unlock == null ? Lists.newArrayList() : unlock;
        this.reward = reward;
    }

    public String getName() {
        return name != null ? name : "Level " + level;
    }

    public String getDescription() {
        return description != null ? description : "Add Description in JSON";
    }

    public ArrayList<Item> getUnlockItems() {
        return this.unlock;
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
