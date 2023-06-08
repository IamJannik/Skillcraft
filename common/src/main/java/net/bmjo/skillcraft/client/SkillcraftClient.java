package net.bmjo.skillcraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.bmjo.skillcraft.SkillcraftIdentifier;
import net.bmjo.skillcraft.event.KeyInputHandler;
import net.bmjo.skillcraft.networking.SkillcraftNetworking;

@Environment(EnvType.CLIENT)
public class SkillcraftClient {
    public static Identifier lastBookSkill = new SkillcraftIdentifier("build");
    public static void onInitializeClient() {
        KeyInputHandler.register();
        SkillcraftNetworking.registerC2SPackets();
    }
}
