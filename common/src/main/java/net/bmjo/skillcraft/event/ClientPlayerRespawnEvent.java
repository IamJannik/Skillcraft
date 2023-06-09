package net.bmjo.skillcraft.event;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.networking.NetworkManager;
import net.bmjo.skillcraft.networking.SkillcraftNetworking;
import net.minecraft.client.network.ClientPlayerEntity;

import static net.bmjo.skillcraft.util.SkillcraftUtil.createPacketBuf;

public class ClientPlayerRespawnEvent implements ClientPlayerEvent.ClientPlayerRespawn {
    @Override
    public void respawn(ClientPlayerEntity oldPlayer, ClientPlayerEntity newPlayer) {
        NetworkManager.sendToServer(SkillcraftNetworking.SKILL_SYNC_REQUEST_ID, createPacketBuf());
    }
}
