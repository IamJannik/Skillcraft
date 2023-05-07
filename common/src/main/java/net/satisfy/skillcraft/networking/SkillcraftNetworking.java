package net.satisfy.skillcraft.networking;

import dev.architectury.networking.NetworkManager;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.SkillcraftIdentifier;
import net.satisfy.skillcraft.networking.packet.LevelC2SPacket;
import net.satisfy.skillcraft.networking.packet.LevelSyncS2CPacket;
import net.satisfy.skillcraft.networking.packet.SkillC2SPacket;

public class SkillcraftNetworking {
    public static final Identifier SKILL_ID = new SkillcraftIdentifier("skill");
    public static final Identifier SKILL_LEVEL_ID = new SkillcraftIdentifier("skill_level");
    public static final Identifier SKILL_LEVEL_SYNC_ID = new SkillcraftIdentifier("skill_level_sync");

    public static void registerC2SPackets() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SKILL_ID, new SkillC2SPacket());
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SKILL_LEVEL_ID, new LevelC2SPacket());
    }

    public static void registerS2CPackets() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, SKILL_LEVEL_SYNC_ID, new LevelSyncS2CPacket());
    }

}
