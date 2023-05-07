package net.satisfy.skillcraft.networking.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.satisfy.skillcraft.skill.SkillLevelData;
import net.satisfy.skillcraft.util.IEntityDataSaver;

public class LevelC2SPacket implements NetworkManager.NetworkReceiver {
    @Override
    public void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();
        int increase =  player.experienceLevel; //TODO
        SkillLevelData.grantLevel((IEntityDataSaver) player, increase);
        player.sendMessage(Text.literal("Level: " + ((IEntityDataSaver) player).getPersistentData().getInt("level")).formatted(Formatting.GOLD));
    }
}
