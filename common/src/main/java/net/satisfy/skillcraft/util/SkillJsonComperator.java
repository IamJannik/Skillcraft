package net.satisfy.skillcraft.util;

import com.google.gson.JsonObject;

import java.util.Comparator;

public class SkillJsonComperator implements Comparator<JsonObject> {
    @Override
    public int compare(JsonObject jo1, JsonObject jo2) {
        int jo1Weight = jo1.get("weight").getAsInt();
        int jo2Weight = jo2.get("weight").getAsInt();
        return Integer.compare(jo1Weight, jo2Weight);
    }
}
