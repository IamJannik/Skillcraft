package net.bmjo.skillcraft.mixin;

import net.bmjo.skillcraft.client.toast.CantUseToast;
import net.bmjo.skillcraft.skill.SkillLevel;
import net.bmjo.skillcraft.util.IEntityDataSaver;
import net.bmjo.skillcraft.util.ISkillBlock;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(Block.class)
public class BlockMixin implements ISkillBlock {
    List<SkillLevel> skillLevels = new ArrayList<>();
    private CantUseToast cantUseToast;

    @Override
    public void addSkillLevel(SkillLevel skillLevel) {
        this.skillLevels.add(skillLevel);
    }

    @Override
    public boolean hasRequiredLevel(PlayerEntity player, Block block) {
        if (skillLevels.isEmpty() || player.isCreative()) return true;
        NbtCompound nbtCompound = ((IEntityDataSaver)player).getPersistentData();
        boolean enough = true;
        for (SkillLevel skillLevel : skillLevels) {
            if (nbtCompound.getInt(skillLevel.skill().toString()) < skillLevel.level()) {
                enough = false;
            }
        }
        if (!enough && player instanceof ClientPlayerEntity) {
            generateToast(block);
        }
        return enough;
    }

    private boolean hasRequiredLevel(Identifier skill, int level) {
        IEntityDataSaver player = (IEntityDataSaver) MinecraftClient.getInstance().player;
        assert player != null;
        int playerLevel = player.getPersistentData().getInt(skill.toString());
        return playerLevel < level;
    }

    private void generateToast(Block block) {
        if (cantUseToast == null) {
            this.cantUseToast = new CantUseToast(skillLevels.get(0), block.asItem());
        }
        for (SkillLevel skillLevel : skillLevels) {
            if (!hasRequiredLevel(skillLevel.skill(), skillLevel.level())) {
                this.cantUseToast.setSkillLevel(skillLevel);
                break;
            }
        }
        ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
        if (toastManager.getToast(CantUseToast.class, cantUseToast) == null) {
            cantUseToast.reload();
            toastManager.add(cantUseToast);
        }
    }
}
