package net.bmjo.skillcraft.mixin;

import net.bmjo.skillcraft.util.ISkillItem;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Wearable;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class PlayerInteractionMixin {
    @Inject(method = "clickSlot", at = @At("HEAD"), cancellable = true)
    public void setArmor(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        Item item = player.currentScreenHandler.getCursorStack().getItem();
        if (item instanceof Wearable && slotId >= 5 && slotId <= 8) {
            if (item instanceof ISkillItem skillItem) {
                if (!skillItem.hasRequiredLevel(player, item)) {
                    ci.cancel();
                }
            }
        }
    }
}
