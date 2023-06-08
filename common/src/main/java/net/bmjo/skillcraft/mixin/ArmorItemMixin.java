package net.bmjo.skillcraft.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.bmjo.skillcraft.util.ISkillItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin extends Item {
    public ArmorItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getProtection", at = @At("HEAD"), cancellable = true)
    public void protectionLevel(CallbackInfoReturnable<Integer> cir) {
        if (this instanceof ISkillItem skillItem && !skillItem.hasRequiredLevel(MinecraftClient.getInstance().player, this)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "getToughness", at = @At("HEAD"), cancellable = true)
    public void toughnessLevel(CallbackInfoReturnable<Integer> cir) {
        if (this instanceof ISkillItem skillItem && !skillItem.hasRequiredLevel(MinecraftClient.getInstance().player, this)) {
            cir.setReturnValue(0);
        }
    }
}
