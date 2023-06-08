package net.bmjo.skillcraft.forge;

import dev.architectury.platform.forge.EventBuses;
import net.bmjo.skillcraft.Skillcraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Skillcraft.MOD_ID)
public class SkillcraftForge {
    public SkillcraftForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Skillcraft.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Skillcraft.init();
    }
}
