package net.satisfy.skillcraft.json;

import com.google.gson.*;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.skill.Skillset;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class SkillReader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
    private static final String REPLACE_KEY = "replace";
    private static final String JSON_EXTENSION = ".json";

    public SkillReader() {
    }

    public static Skillset convertSkill(JsonObject jsonObject) {
        return GSON.fromJson(jsonObject, Skillset.class);
    }

    public static JsonObject read(Identifier identifier, Resource resource) {
        return readJson(identifier, resource);
    }

    @Nullable
    private static JsonObject readJson(Identifier identifier, Resource resource) {
            try {
                BufferedReader reader = new BufferedReader(resource.getReader());
                JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);
                jsonObject.addProperty("id", identifier.getPath().replace("skills/", "").replace(JSON_EXTENSION, ""));
                return jsonObject;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    public static Skillset combineSkillsets(List<JsonObject> skillJsons) {
        skillJsons.sort(new SkillJsonComperator());
        if (skillJsons.stream().anyMatch(jsonObject -> jsonObject.get(REPLACE_KEY).getAsBoolean())) {
            List<JsonObject> replaceSkills = skillJsons.stream().filter(jsonObject -> jsonObject.get(REPLACE_KEY).getAsBoolean()).toList();
            return convertSkill(replaceSkills.get(0)); //return Skill with REPLACE=true & highest WEIGHT
        }
        //combine Skills
        return convertSkill(skillJsons.get(0)); //TODO combine SKILLS
    }
}
