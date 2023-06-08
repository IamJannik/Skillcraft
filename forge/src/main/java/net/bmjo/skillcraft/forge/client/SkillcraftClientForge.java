package net.bmjo.skillcraft.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.bmjo.skillcraft.Skillcraft;
import net.bmjo.skillcraft.client.SkillcraftClient;

@Mod.EventBusSubscriber(modid = Skillcraft.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SkillcraftClientForge {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        SkillcraftClient.onInitializeClient();
    }

}
