package net.bmjo.skillcraft.fabric;

import net.bmjo.skillcraft.fabriclike.SkillcraftFabricLike;
import net.fabricmc.api.ModInitializer;

public class SkillcraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SkillcraftFabricLike.init();
    }
}
