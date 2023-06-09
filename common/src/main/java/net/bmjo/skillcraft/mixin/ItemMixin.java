package net.bmjo.skillcraft.mixin;

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
import net.bmjo.skillcraft.client.toast.CantUseToast;
import net.bmjo.skillcraft.json.SkillLoader;
import net.bmjo.skillcraft.skill.Skill;
import net.bmjo.skillcraft.util.IEntityDataSaver;
import net.bmjo.skillcraft.util.ISkillItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin implements ISkillItem {
    private Identifier skillKey;
    private int requiredLevel = 0;
    private CantUseToast cantUseToast;

    public boolean hasRequiredLevel(PlayerEntity player, Item item) {
        if (skillKey == null || requiredLevel == 0 || player.isCreative()) return true;
        NbtCompound nbtCompound = ((IEntityDataSaver)player).getPersistentData();
        boolean enough = nbtCompound.getInt(skillKey.toString()) >= requiredLevel;
        if (!enough && player instanceof ClientPlayerEntity) {
            generateToast(item);
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

    private void generateToast(Item item) {
        if (cantUseToast == null) {
            this.cantUseToast = new CantUseToast(SkillLoader.REGISTRY_SKILLS.get(skillKey), item, requiredLevel);
        }
        ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
        if (toastManager.getToast(CantUseToast.class, cantUseToast) == null) {
            cantUseToast.reload();
            toastManager.add(cantUseToast);
        }
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "appendTooltip",  at = @At("HEAD"))
    public void appendSkillTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        if (skillKey != null && requiredLevel != 0) {
            IEntityDataSaver player = (IEntityDataSaver) MinecraftClient.getInstance().player;
            assert player != null;
            int playerLevel = player.getPersistentData().getInt(skillKey.toString());
            if (playerLevel < requiredLevel) {
                Skill skill = SkillLoader.REGISTRY_SKILLS.get(skillKey);
                tooltip.add(Text.literal("Needs " + skill.getName() + ": Level " + requiredLevel).formatted(Formatting.RED));
            }

        }

    }
}
