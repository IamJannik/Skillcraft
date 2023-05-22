package net.satisfy.skillcraft.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.util.IEntityDataSaver;
import net.satisfy.skillcraft.util.ISkillBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class BlockMixin implements ISkillBlock {
    private Identifier skillKey;
    private int requiredLevel = 0;

    @Override
    public boolean hasRequiredLevel(PlayerEntity player, Block block) {
        if (skillKey == null || requiredLevel == 0) return  true;
        NbtCompound nbtCompound = ((IEntityDataSaver)player).getPersistentData();
        boolean enough = nbtCompound.getInt(skillKey.toString()) >= requiredLevel;
        player.sendMessage(enough ?
                Text.literal("You have reached the required level to use Block: " + block.getName() + ". (" + requiredLevel + ")").formatted(Formatting.GREEN) :
                Text.literal("You haven't reached the required level to use Block:" + block.getName() + ". (" + requiredLevel + ")").formatted(Formatting.RED));
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
