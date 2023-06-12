package net.bmjo.skillcraft;

import com.google.common.collect.Maps;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.bmjo.skillcraft.event.ClientPlayerRespawnEvent;
import net.bmjo.skillcraft.event.PlayerCloneEvent;
import net.bmjo.skillcraft.event.PlayerJoinEvent;
import net.bmjo.skillcraft.event.ResetCommandEvent;
import net.bmjo.skillcraft.json.SkillLoader;
import net.bmjo.skillcraft.networking.SkillcraftNetworking;
import net.bmjo.skillcraft.skill.Skill;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class Skillcraft {
    public static final String MOD_ID = "skillcraft";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final Map<Identifier, Skill> SKILLS = Maps.newTreeMap();

    public static void init() {
        SkillLoader.init();
        SkillcraftNetworking.registerS2CPackets();

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(new PlayerJoinEvent());
        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.register(new ClientPlayerRespawnEvent());
        PlayerEvent.PLAYER_CLONE.register(new PlayerCloneEvent());
        CommandRegistrationEvent.EVENT.register(new ResetCommandEvent());
    }
}
