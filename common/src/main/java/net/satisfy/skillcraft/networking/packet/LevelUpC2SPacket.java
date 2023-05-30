package net.satisfy.skillcraft.networking.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.json.SkillLoader;
import net.satisfy.skillcraft.skill.SkillData;
import net.satisfy.skillcraft.skill.SkillLevel;
import net.satisfy.skillcraft.util.IEntityDataSaver;

import java.util.List;
import java.util.Optional;

public class LevelUpC2SPacket implements NetworkManager.NetworkReceiver {
    @Override
    public void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        String skill = buf.readString();
        int amount = buf.readInt();
        int cost = buf.readInt();
        int level = SkillData.grantSkill((IEntityDataSaver)player, skill, amount);

        player.addExperienceLevels(-cost);

        Identifier skillIdentifier = new Identifier(skill);
        Optional<SkillLevel> optionalLevel = SkillLoader.REGISTRY_SKILLS.get(skillIdentifier).getSkillLevel(level);//TODO can use Toast
        if (optionalLevel.isPresent()) {
            List<Item> items = optionalLevel.get().getUnlockItems();
            List<Block> blocks = optionalLevel.get().getUnlockBlocks();
        }

        player.sendMessage(Text.literal("Level: " + level).formatted(Formatting.GOLD)); //TODO schön anzeigen
        player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
    }
}
