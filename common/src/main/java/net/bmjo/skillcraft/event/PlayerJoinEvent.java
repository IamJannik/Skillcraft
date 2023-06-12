package net.bmjo.skillcraft.event;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.networking.NetworkManager;
import net.bmjo.skillcraft.networking.SkillcraftNetworking;
import net.bmjo.skillcraft.util.SkillcraftUtil;
import net.minecraft.client.network.ClientPlayerEntity;

public class PlayerJoinEvent implements ClientPlayerEvent.ClientPlayerJoin {
    @Override
    public void join(ClientPlayerEntity player) {
        NetworkManager.sendToServer(SkillcraftNetworking.SKILL_SYNC_REQUEST_ID, SkillcraftUtil.createPacketBuf());
    }
}
