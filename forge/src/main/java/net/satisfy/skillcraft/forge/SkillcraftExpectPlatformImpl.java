package net.satisfy.skillcraft.forge;

import net.satisfy.skillcraft.SkillcraftExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class SkillcraftExpectPlatformImpl {
    /**
     * This is our actual method to {@link SkillcraftExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
