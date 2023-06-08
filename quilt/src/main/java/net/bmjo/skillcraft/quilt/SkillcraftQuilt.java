package net.bmjo.skillcraft.quilt;

import net.bmjo.skillcraft.fabriclike.SkillcraftFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class SkillcraftQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        SkillcraftFabricLike.init();
    }
}
