package net.satisfy.skillcraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.SkillcraftIdentifier;
import net.satisfy.skillcraft.event.KeyInputHandler;
import net.satisfy.skillcraft.networking.SkillcraftNetworking;

@Environment(EnvType.CLIENT)
public class SkillcraftClient {
    public static Identifier lastBookSkill = new SkillcraftIdentifier("build");
    public static void onInitializeClient() {
        KeyInputHandler.register();
        SkillcraftNetworking.registerC2SPackets();
    }
}
