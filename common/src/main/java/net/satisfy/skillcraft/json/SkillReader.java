package net.satisfy.skillcraft.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.skill.Skillset;
import org.apache.commons.compress.utils.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class SkillReader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
    private static final String JSON_EXTENSION = ".json";
    private final Identifier path;

    public SkillReader(Identifier path) {
        this.path = path;
    }

    public Skillset load(ResourceManager manager) {
        List<Resource> jsons = getJsons(manager);
        return readJson(jsons);
    }

    public Skillset readJson(List<Resource> resources) {
        if (resources.size() > 0) { //TODO
            try {
                BufferedReader reader = new BufferedReader(resources.get(0).getReader());
                JsonObject jo = GSON.fromJson(reader, JsonObject.class);
                jo.addProperty("id", this.path.getPath());
                return GSON.fromJson(jo, Skillset.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Skillset(path.getNamespace(), path.getNamespace(), "ERROR - COULD NOT FIND FILE", Lists.newArrayList());
    }

    private List<Resource> getJsons(ResourceManager manager) {
        List<Resource> resources = Lists.newArrayList();
        for (String namespace : manager.getAllNamespaces()) {
            manager.getResource(new Identifier(namespace, "/skills/" + this.path.getPath() + JSON_EXTENSION)).ifPresent(resources::add);
        }
        return resources;
    }
}
