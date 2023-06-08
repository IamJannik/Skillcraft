package net.bmjo.skillcraft.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.bmjo.skillcraft.util.ISkillItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "use",  at = @At("HEAD"), cancellable = true)
    public void useLevel(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        Item item = itemStack.getItem();
        if (item instanceof ISkillItem skillItem) {
            if (!skillItem.hasRequiredLevel(user, item)) {
                cir.setReturnValue(TypedActionResult.fail(itemStack));
            }
        }
    }

    @Inject(method = "useOnBlock",  at = @At("HEAD"), cancellable = true)
    public void useOnBlockLevel(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = context.getStack();
        Item item = itemStack.getItem();
        if (item instanceof ISkillItem skillItem) {
            PlayerEntity player = context.getPlayer();
            if (player != null && !skillItem.hasRequiredLevel(player, item)) {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }

    @Inject(method = "useOnEntity",  at = @At("HEAD"), cancellable = true)
    public void useOnEntityLevel(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        Item item = itemStack.getItem();
        if (item instanceof ISkillItem skillItem) {
            if (!skillItem.hasRequiredLevel(user, item)) {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }
}
