package net.satisfy.skillcraft.event;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.satisfy.skillcraft.networking.SkillcraftNetworking;

import static net.satisfy.skillcraft.util.SkillcraftUtil.createPacketBuf;

public class PlayerJoinEvent implements ClientPlayerEvent.ClientPlayerJoin {
    @Override
    public void join(ClientPlayerEntity player) {
        System.out.println("join");
        NetworkManager.sendToServer(SkillcraftNetworking.SKILL_SYNC_REQUEST_ID, createPacketBuf());
    }
}
