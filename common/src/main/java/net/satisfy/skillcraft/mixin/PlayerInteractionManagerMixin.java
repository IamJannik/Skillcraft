package net.satisfy.skillcraft.mixin;

import net.minecraft.server.network.ServerPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(ServerPlayerInteractionManager.class)
public class PlayerInteractionManagerMixin {

    @Inject(method = "interactItem", at = @At("HEAD"), cancellable = true)
    public void interactSkillItem() {

    }

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    public void interactSkillBlock() {

    }
}
