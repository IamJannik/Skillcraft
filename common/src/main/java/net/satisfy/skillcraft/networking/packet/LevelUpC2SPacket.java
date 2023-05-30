package net.satisfy.skillcraft.networking.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.satisfy.skillcraft.skill.SkillData;
import net.satisfy.skillcraft.util.IEntityDataSaver;

public class LevelUpC2SPacket implements NetworkManager.NetworkReceiver {
    @Override
    public void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        String skill = buf.readString();
        int amount = buf.readInt();
        int cost = buf.readInt();
        int level = SkillData.grantSkill((IEntityDataSaver)player, skill, amount);

        player.addExperienceLevels(-cost);

        //Identifier skillIdentifier = new Identifier(skill);
        //List<Item> item = SkillLoader.REGISTRY_SKILLS.get(skillIdentifier).//TODO can use Toast

        player.sendMessage(Text.literal("Level: " + level).formatted(Formatting.GOLD)); //TODO schön anzeigen
        player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
    }
}
