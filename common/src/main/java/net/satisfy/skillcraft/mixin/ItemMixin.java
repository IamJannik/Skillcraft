package net.satisfy.skillcraft.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.client.toast.CantUseToast;
import net.satisfy.skillcraft.json.SkillLoader;
import net.satisfy.skillcraft.util.IEntityDataSaver;
import net.satisfy.skillcraft.util.ISkillItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin implements ISkillItem {
    private Identifier skillKey;
    private int requiredLevel = 0;

    public boolean hasRequiredLevel(PlayerEntity player, Item item) {
        if (skillKey == null || requiredLevel == 0 || player.isCreative()) return true;
        NbtCompound nbtCompound = ((IEntityDataSaver)player).getPersistentData();
        boolean enough = nbtCompound.getInt(skillKey.toString()) >= requiredLevel;
        if (!enough && player instanceof ClientPlayerEntity) {
            MinecraftClient.getInstance().getToastManager().add(new CantUseToast(SkillLoader.REGISTRY_SKILLS.get(skillKey), item, requiredLevel));
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
}
