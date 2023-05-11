package net.satisfy.skillcraft.json;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.util.JsonHelper;
import net.satisfy.skillcraft.skill.SkillLevel;
import net.satisfy.skillcraft.skill.Skillset;
import net.satisfy.skillcraft.util.ISkillItem;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SkillConvertor {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
    public static Skillset convertSkill(JsonObject jsonObject) {
        String id = jsonObject.get("id").getAsString();
        @Nullable
        String name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : null;
        @Nullable
        String description = jsonObject.has("description") ? jsonObject.get("description").getAsString() : null;
        ArrayList<SkillLevel> levels = getLevels(jsonObject, id);

        return new Skillset(id, name, description, levels);
    }

    private static ArrayList<SkillLevel> getLevels(JsonObject jsonObject, String id) {
        ArrayList<SkillLevel> levels = Lists.newArrayList();
        JsonArray levelsJson = jsonObject.getAsJsonArray("levels");
        for (JsonElement jsonElement : levelsJson) {
            JsonObject levelJson = GSON.fromJson(jsonElement, JsonObject.class);
            int level = levelJson.get("level").getAsInt();
            @Nullable
            String levelName = levelJson.has("name") ? levelJson.get("name").getAsString() : null;
            @Nullable
            String levelDescription = levelJson.has("description") ? levelJson.get("description").getAsString() : null;
            ArrayList<Item> unlockItems = getUnlockItems(levelJson, id, level);
            @Nullable
            Item levelReward = levelJson.has("reward") ? JsonHelper.getItem(levelJson, "reward") : null;
            levels.add(new SkillLevel(level, levelName, levelDescription, unlockItems, levelReward));
        }
        return levels;
    }

    private static ArrayList<Item> getUnlockItems(JsonObject levelJson, String id, int level) {
        ArrayList<Item> unlockItems = Lists.newArrayList();
        JsonArray unlockJson = levelJson.has("unlock") ? levelJson.get("unlock").getAsJsonArray() : new JsonArray();
        for (JsonElement jsonElement : unlockJson) {
            Item unlockItem = JsonHelper.asItem(jsonElement, jsonElement.getAsString());
            safeSkillOnItem(unlockItem, id, level);
            unlockItems.add(unlockItem);
        }
        return unlockItems;
    }

    private static void safeSkillOnItem(Item unlockItem, String id, int level) {
        if (unlockItem instanceof ISkillItem skillItem) {
            skillItem.setSkillKey(id);
            skillItem.setRequiredLevel(level);
        }
    }
}
