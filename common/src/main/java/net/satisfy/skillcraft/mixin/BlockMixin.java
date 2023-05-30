package net.satisfy.skillcraft.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.client.toast.CantUseToast;
import net.satisfy.skillcraft.json.SkillLoader;
import net.satisfy.skillcraft.util.IEntityDataSaver;
import net.satisfy.skillcraft.util.ISkillBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class BlockMixin implements ISkillBlock {
    private Identifier skillKey;
    private int requiredLevel = 0;
    private CantUseToast cantUseToast;

    @Override
    public boolean hasRequiredLevel(PlayerEntity player, Block block) {
        if (skillKey == null || requiredLevel == 0 || player.isCreative()) return true;
        NbtCompound nbtCompound = ((IEntityDataSaver)player).getPersistentData();
        boolean enough = nbtCompound.getInt(skillKey.toString()) >= requiredLevel;
        if (!enough && player instanceof ClientPlayerEntity) {
            generateToast(block);
        }
        return enough;
    }

    @Override
    public void setRequiredLevel(int level) {
        this.requiredLevel = level;
    }

    @Override
    public void setSkillKey(Identifier skillKey) {
        this.skillKey = skillKey;
    }

    private void generateToast(Block block) {
        if (cantUseToast == null) {
            this.cantUseToast = new CantUseToast(SkillLoader.REGISTRY_SKILLS.get(skillKey), block.asItem(), requiredLevel);
        }
        ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
        if (toastManager.getToast(CantUseToast.class, cantUseToast) == null) {
            cantUseToast.reload();
            toastManager.add(cantUseToast);
        }
    }
}
