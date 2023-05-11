package net.satisfy.skillcraft.networking.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.satisfy.skillcraft.skill.SkillData;
import net.satisfy.skillcraft.util.IEntityDataSaver;

public class LevelC2SPacket implements NetworkManager.NetworkReceiver {
    @Override
    public void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();
        int increase = 1; //TODO how much increase
        String key = buf.readString();
        SkillData.grantSkill((IEntityDataSaver) player, key, increase);
        player.sendMessage(Text.literal("Level: " + ((IEntityDataSaver) player).getPersistentData().getInt("combat")).formatted(Formatting.GOLD)); //TODO schön anzeigen
    }
}
