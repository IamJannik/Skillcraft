package net.satisfy.skillcraft.json;

import com.google.gson.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.skill.Skillset;

import java.io.*;

public class SkillReader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
    private static final String JSON_EXTENSION = ".json";
    private final Identifier path;

    public SkillReader(Identifier path) {
        this.path = path;
    }

    public Skillset readJson(ResourceManager manager) {
        String path = "C:/JAVA/minecraft/fabric/Skillcraft/common/src/main/resources/data/" + this.path.getNamespace() + "/skills/" + this.path.getPath() + JSON_EXTENSION; //TODO
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            JsonObject jo = GSON.fromJson(reader, JsonObject.class);
            jo.addProperty("id", this.path.getPath());
            return GSON.fromJson(jo, Skillset.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Skillset load(ResourceManager manager) {
        return readJson(manager);
    }
}
