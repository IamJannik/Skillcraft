package net.bmjo.skillcraft.json;

import com.google.gson.*;
import net.minecraft.resource.Resource;
import net.bmjo.skillcraft.skill.Skill;
import net.bmjo.skillcraft.util.SkillJsonComparator;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class SkillReader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
    private static final String REPLACE_KEY = "replace";

    public SkillReader() {
    }

    public static JsonObject read(Resource resource) {
        return readJson(resource);
    }

    @Nullable
    private static JsonObject readJson(Resource resource) {
            try {
                BufferedReader reader = new BufferedReader(resource.getReader());
                return GSON.fromJson(reader, JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    public static Skill combineSkills(List<JsonObject> skillJsons) {
        skillJsons.sort(new SkillJsonComparator());
        if (skillJsons.stream().anyMatch(jsonObject -> jsonObject.get(REPLACE_KEY).getAsBoolean())) {
            List<JsonObject> replaceSkills = skillJsons.stream().filter(jsonObject -> jsonObject.get(REPLACE_KEY).getAsBoolean()).toList();
            return SkillConvertor.convertSkill(replaceSkills.get(0)); //returns Skill with REPLACE=true & highest WEIGHT
        }
        //TODO combine SKILLS
        return SkillConvertor.convertSkill(skillJsons.get(0));
    }


}
