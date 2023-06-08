package net.bmjo.skillcraft.mixin.forge;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.bmjo.skillcraft.util.ISkillBlock;
import net.bmjo.skillcraft.util.ISkillItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getDigSpeed", at = @At("HEAD"), cancellable = true)
    public void mineSkillSpeed(BlockState blockstate, @Nullable BlockPos pos, CallbackInfoReturnable<Float> cir) {
        Block block = blockstate.getBlock();
        if (block instanceof ISkillBlock skillBlock) {
            if (!skillBlock.hasRequiredLevel(MinecraftClient.getInstance().player, block)) {
                cir.setReturnValue(0f);
            }
        }
        Item item = this.getMainHandStack().getItem();
        if (item instanceof ISkillItem skillItem) {
            if (!skillItem.hasRequiredLevel(MinecraftClient.getInstance().player, item)) {
                cir.setReturnValue(0f);
            }
        }
    }
}
