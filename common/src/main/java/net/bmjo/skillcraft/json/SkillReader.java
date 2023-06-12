package net.bmjo.skillcraft.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.bmjo.skillcraft.skill.Skill;
import net.bmjo.skillcraft.util.SkillJsonComparator;
import net.minecraft.resource.Resource;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class SkillReader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();

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
        if (skillJsons.stream().anyMatch(jsonObject -> jsonObject.get("replace").getAsBoolean())) {
            List<JsonObject> replaceSkills = skillJsons.stream().filter(jsonObject -> jsonObject.get("replace").getAsBoolean()).toList();
            return SkillConvertor.convertSkill(replaceSkills.get(0)); //returns Skill with REPLACE=true & highest WEIGHT
        }
        Skill skill = SkillConvertor.convertSkill(skillJsons.get(0));
        for (JsonObject jsonObject : skillJsons) {
            Skill combineSkill = SkillConvertor.convertSkill(jsonObject);
            skill.combineSkill(combineSkill);
        }
        return skill;
    }


}
