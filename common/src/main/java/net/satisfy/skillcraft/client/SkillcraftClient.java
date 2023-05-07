package net.satisfy.skillcraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.satisfy.skillcraft.event.KeyInputHandler;
import net.satisfy.skillcraft.networking.SkillcraftNetworking;

@Environment(EnvType.CLIENT)
public class SkillcraftClient {

    public static void onInitializeClient() {
        KeyInputHandler.register();
        SkillcraftNetworking.registerC2SPackets();
    }
}
