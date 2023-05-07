package net.satisfy.skillcraft.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.satisfy.skillcraft.Skillcraft;
import net.satisfy.skillcraft.client.SkillcraftClient;

@Mod.EventBusSubscriber(modid = Skillcraft.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SkillcraftClientForge {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        SkillcraftClient.onInitializeClient();
    }

}
