package net.satisfy.skillcraft.quilt;

import net.satisfy.skillcraft.fabriclike.SkillcraftFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class SkillcraftQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        SkillcraftFabricLike.init();
    }
}
