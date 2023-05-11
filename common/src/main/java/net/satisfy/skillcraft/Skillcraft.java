package net.satisfy.skillcraft;

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

        System.out.println(SkillcraftExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
