package net.bmjo.skillcraft.json;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.bmjo.skillcraft.skill.SkillLevel;
import net.bmjo.skillcraft.skill.Skill;
import net.bmjo.skillcraft.util.ISkillBlock;
import net.bmjo.skillcraft.util.ISkillItem;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        Map<Integer, SkillLevel> levelsUnsorted = new HashMap<>();
        getLevels(jsonObject, id).forEach(skillLevel -> levelsUnsorted.put(skillLevel.level(), skillLevel));

        return new Skill(id, name, description, icon, levelsUnsorted);
    }

    public static ArrayList<SkillLevel> getLevels(JsonObject jsonObject, Identifier skillId) {
        ArrayList<SkillLevel> levels = Lists.newArrayList();
        JsonArray levelsJson = jsonObject.getAsJsonArray("levels");
        for (JsonElement jsonElement : levelsJson) {
            JsonObject levelJson = GSON.fromJson(jsonElement, JsonObject.class);
            int level = levelJson.get("level").getAsInt();
            @Nullable
            String levelName = levelJson.has("name") ? levelJson.get("name").getAsString() : null;
            @Nullable
            String levelDescription = levelJson.has("description") ? levelJson.get("description").getAsString() : null;
            ArrayList<Item> unlockItems = getUnlockItems(levelJson, skillId, level);
            ArrayList<Block> unlockBlocks = getUnlockBlocks(unlockItems, skillId, level);
            @Nullable
            Item levelReward = levelJson.has("reward") ? JsonHelper.getItem(levelJson, "reward") : null;
            levels.add(new SkillLevel(level, levelName, levelDescription, unlockItems, unlockBlocks, levelReward));
        }
        return levels;
    }

    private static ArrayList<Item> getUnlockItems(JsonObject levelJson, Identifier skillId, int level) {
        ArrayList<Item> unlockItems = Lists.newArrayList();
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
            skillItem.setSkillKey(skillId);
            skillItem.setRequiredLevel(level);
        }
    }

    private static ArrayList<Block> getUnlockBlocks(ArrayList<Item> unlockItems, Identifier skillId, int level) {
        ArrayList<Block> unlockBlocks = Lists.newArrayList();
        for (Item unlockItem : unlockItems) {
            if (unlockItem instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                unlockBlocks.add(block);
                safeSkillOnBlock(block, skillId, level);
            }
        }
        return unlockBlocks;
    }

    private static void safeSkillOnBlock(Block unlockBlock, Identifier skillId, int level) {
        if (unlockBlock instanceof ISkillBlock skillBLock) {
            skillBLock.setSkillKey(skillId);
            skillBLock.setRequiredLevel(level);
        }
    }
}
