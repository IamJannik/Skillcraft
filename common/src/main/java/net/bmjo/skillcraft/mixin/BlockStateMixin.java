package net.bmjo.skillcraft.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import net.bmjo.skillcraft.util.ISkillBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class BlockStateMixin {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void useLevel(World world, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        BlockState blockState = world.getBlockState(hit.getBlockPos());
        Block block = blockState.getBlock();
        if (block instanceof ISkillBlock skillBlock) {
            if (!skillBlock.hasRequiredLevel(player, block)) {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }
}
