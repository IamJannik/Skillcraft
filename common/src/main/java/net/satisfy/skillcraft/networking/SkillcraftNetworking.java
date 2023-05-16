package net.satisfy.skillcraft.networking;

import dev.architectury.networking.NetworkManager;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.SkillcraftIdentifier;
import net.satisfy.skillcraft.networking.packet.LevelUpC2SPacket;
import net.satisfy.skillcraft.networking.packet.SkillSyncS2CPacket;
import net.satisfy.skillcraft.networking.packet.SyncRequestC2SPacket;

public class SkillcraftNetworking {
    public static final Identifier SKILL_LEVEL_UP_ID = new SkillcraftIdentifier("skill_level");
    public static final Identifier SKILL_SYNC_ID = new SkillcraftIdentifier("skill_sync");
    public static final Identifier SKILL_SYNC_REQUEST_ID = new SkillcraftIdentifier("skill_sync_request");

    public static void registerC2SPackets() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SKILL_LEVEL_UP_ID, new LevelUpC2SPacket());
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SKILL_SYNC_REQUEST_ID, new SyncRequestC2SPacket());
    }

    public static void registerS2CPackets() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, SKILL_SYNC_ID, new SkillSyncS2CPacket());
    }

}
