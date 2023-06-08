package net.bmjo.skillcraft.fabriclike.client;

import net.fabricmc.api.ClientModInitializer;
import net.bmjo.skillcraft.client.SkillcraftClient;

public class SkillcraftClientFabricLike implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SkillcraftClient.onInitializeClient();
    }
}
