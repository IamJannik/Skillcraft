package net.satisfy.skillcraft.fabric;

import net.satisfy.skillcraft.SkillcraftExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class SkillcraftExpectPlatformImpl {
    /**
     * This is our actual method to {@link SkillcraftExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
