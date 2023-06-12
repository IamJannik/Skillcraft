package net.bmjo.skillcraft.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Comparator;

public class SkillJsonComparator implements Comparator<JsonObject> {
    @Override
    public int compare(JsonObject jo1, JsonObject jo2) {
        JsonElement jo1Weight = jo1.get("weight");
        int jo1Int = jo1Weight != null ? jo1Weight.getAsInt() : 0;
        JsonElement jo2Weight = jo2.get("weight");
        int jo2Int = jo2Weight != null ? jo2Weight.getAsInt() : 0;
        return Integer.compare(jo2Int, jo1Int);
    }
}
