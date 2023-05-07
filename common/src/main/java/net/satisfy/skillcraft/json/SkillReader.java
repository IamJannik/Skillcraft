package net.satisfy.skillcraft.json;

import com.google.gson.*;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.skill.Skillset;

import java.io.*;

public class SkillReader {
    public static Skillset readJson(Identifier name) {
        String path = "C:/JAVA/minecraft/fabric/Skillcraft/common/src/main/resources/data/" + name.getNamespace() + "/skillset/" + name.getPath() + ".json"; //TODO
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            Gson gson = new Gson();
            return gson.fromJson(reader, Skillset.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
