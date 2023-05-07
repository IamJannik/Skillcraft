package net.satisfy.skillcraft.fabric;

import net.satisfy.skillcraft.fabriclike.SkillcraftFabricLike;
import net.fabricmc.api.ModInitializer;

public class SkillcraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("HI");
        SkillcraftFabricLike.init();
    }
}
