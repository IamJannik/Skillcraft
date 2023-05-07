package net.satisfy.skillcraft.fabriclike.client;

import net.fabricmc.api.ClientModInitializer;
import net.satisfy.skillcraft.client.SkillcraftClient;

public class SkillcraftClientFabricLike implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SkillcraftClient.onInitializeClient();
    }
}
