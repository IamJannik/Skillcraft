package net.satisfy.skillcraft;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.ChunkEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.satisfy.skillcraft.event.PlayerJoinEvent;
import net.satisfy.skillcraft.event.ResetCommandEvent;
import net.satisfy.skillcraft.json.SkillLoader;
import net.satisfy.skillcraft.networking.SkillcraftNetworking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Skillcraft {
    public static final String MOD_ID = "skillcraft";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        SkillLoader.init();
        SkillcraftNetworking.registerS2CPackets();

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(new PlayerJoinEvent());
        CommandRegistrationEvent.EVENT.register(new ResetCommandEvent());
    }
}
