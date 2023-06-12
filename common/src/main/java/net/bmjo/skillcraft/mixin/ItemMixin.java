package net.bmjo.skillcraft.mixin;

import net.bmjo.skillcraft.client.SkillcraftClient;
import net.bmjo.skillcraft.client.toast.CantUseToast;
import net.bmjo.skillcraft.json.SkillLoader;
import net.bmjo.skillcraft.skill.Skill;
import net.bmjo.skillcraft.skill.SkillLevel;
import net.bmjo.skillcraft.util.IEntityDataSaver;
import net.bmjo.skillcraft.util.ISkillItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin implements ISkillItem {
    private List<SkillLevel> skillLevels = Lists.newArrayList();
    private CantUseToast cantUseToast;

    public void addSkillLevel(SkillLevel skillLevel) {
        if (!this.skillLevels.contains(skillLevel)) {
            this.skillLevels.add(skillLevel);
        }
    }

    @Override
    public boolean hasRequiredLevel(PlayerEntity player, Item item) {
        if (this.skillLevels.isEmpty() || player.isCreative()) return true;
        NbtCompound nbtCompound = ((IEntityDataSaver)player).getPersistentData();
        boolean enough = true;
        for (SkillLevel skillLevel : this.skillLevels) {
            if (nbtCompound.getInt(skillLevel.skill().toString()) < skillLevel.level()) {
                enough = false;
                break;
            }
        }

        if (!enough && player instanceof ClientPlayerEntity) {
            this.generateToast(item);
        }
        return enough;
    }

    private boolean hasRequiredLevel(Identifier skill, int level) {
        IEntityDataSaver player = (IEntityDataSaver) SkillcraftClient.getPlayer();
        assert player != null;
        int playerLevel = player.getPersistentData().getInt(skill.toString());
        return playerLevel >= level;
    }

    private void generateToast(Item item) {
        SkillLevel toastSkillLevel = this.skillLevels.get(0);
        for (SkillLevel skillLevel : this.skillLevels) {
            if (!this.hasRequiredLevel(skillLevel.skill(), skillLevel.level())) {
                toastSkillLevel = skillLevel;
                break;
            }
        }

        if (this.cantUseToast == null) {
            this.cantUseToast = new CantUseToast(toastSkillLevel, item);
        } else {
            this.cantUseToast.setSkillLevel(toastSkillLevel);
        }


        ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
        if (toastManager.getToast(CantUseToast.class, this.cantUseToast) == null) {
            this.cantUseToast.reload();
            toastManager.add(this.cantUseToast);
        }
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void appendSkillTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        if (!this.skillLevels.isEmpty()) {
            for (SkillLevel skillLevel : this.skillLevels) {
                if (!this.hasRequiredLevel(skillLevel.skill(), skillLevel.level())) {
                    Skill skill = SkillLoader.REGISTRY_SKILLS.get(skillLevel.skill());
                    tooltip.add(Text.literal("Needs " + skill.getName() + ": " + skill.getLevelName(skillLevel.level())).formatted(Formatting.RED));
                }
            }
        }
    }
}
