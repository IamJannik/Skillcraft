package net.bmjo.skillcraft;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.bmjo.skillcraft.event.*;
import net.bmjo.skillcraft.json.SkillLoader;
import net.bmjo.skillcraft.networking.SkillcraftNetworking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Skillcraft {
    public static final String MOD_ID = "skillcraft";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        SkillLoader.init();
        SkillcraftNetworking.registerS2CPackets();

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(new PlayerJoinEvent());
        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.register(new ClientPlayerRespawnEvent());
        PlayerEvent.PLAYER_CLONE.register(new PlayerCloneEvent());
        CommandRegistrationEvent.EVENT.register(new ResetCommandEvent());
    }
}
