package net.bmjo.skillcraft.json;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.bmjo.skillcraft.skill.Level;
import net.bmjo.skillcraft.skill.Skill;
import net.bmjo.skillcraft.skill.SkillLevel;
import net.bmjo.skillcraft.util.ISkillBlock;
import net.bmjo.skillcraft.util.ISkillItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SkillConvertor {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();

    public static Skill convertSkill(JsonObject jsonObject) {
        Identifier id = new Identifier(jsonObject.get("id").getAsString());
        @Nullable
        String name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : null;
        @Nullable
        String description = jsonObject.has("description") ? jsonObject.get("description").getAsString() : null;
        @Nullable
        Item icon = jsonObject.has("icon") ? JsonHelper.asItem(jsonObject.get("icon"), jsonObject.get("icon").getAsString()) : null;
        Map<Integer, Level> levelsUnsorted = Maps.newTreeMap();
        getLevels(jsonObject, id).forEach(level -> levelsUnsorted.put(level.level(), level));

        return new Skill(id, name, description, icon, levelsUnsorted);
    }

    public static HashSet<Level> getLevels(JsonObject jsonObject, Identifier skillId) {
        HashSet<Level> levels = Sets.newHashSet();
        JsonArray levelsJson = jsonObject.getAsJsonArray("levels");
        for (JsonElement jsonElement : levelsJson) {
            JsonObject levelJson = GSON.fromJson(jsonElement, JsonObject.class);
            int level = levelJson.get("level").getAsInt();
            @Nullable
            String levelName = levelJson.has("name") ? levelJson.get("name").getAsString() : null;
            @Nullable
            String levelDescription = levelJson.has("description") ? levelJson.get("description").getAsString() : null;
            Set<Item> unlockItems = getUnlockItems(levelJson, skillId, level);
            safeUnlockBlocks(unlockItems, skillId, level);
            @Nullable
            Item levelReward = levelJson.has("reward") ? JsonHelper.getItem(levelJson, "reward") : null;
            levels.add(new Level(level, levelName, levelDescription, unlockItems, levelReward));
        }
        return levels;
    }

    private static Set<Item> getUnlockItems(JsonObject levelJson, Identifier skillId, int level) {
        Set<Item> unlockItems = Sets.newHashSet();
        JsonArray unlockJson = levelJson.has("unlock") ? levelJson.get("unlock").getAsJsonArray() : new JsonArray();
        for (JsonElement jsonElement : unlockJson) {
            Item unlockItem = JsonHelper.asItem(jsonElement, jsonElement.getAsString());
            safeSkillOnItem(unlockItem, skillId, level);
            unlockItems.add(unlockItem);
        }
        return unlockItems;
    }

    private static void safeSkillOnItem(Item unlockItem, Identifier skillId, int level) {
        if (unlockItem instanceof ISkillItem skillItem) {
            skillItem.addSkillLevel(new SkillLevel(skillId, level));
        }
    }

    private static void safeUnlockBlocks(Set<Item> unlockItems, Identifier skillId, int level) {
        for (Item unlockItem : unlockItems) {
            if (unlockItem instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                safeSkillOnBlock(block, skillId, level);
            }
        }
    }

    private static void safeSkillOnBlock(Block unlockBlock, Identifier skillId, int level) {
        if (unlockBlock instanceof ISkillBlock skillBLock) {
            skillBLock.addSkillLevel(new SkillLevel(skillId, level));
        }
    }
}
