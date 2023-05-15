package net.satisfy.skillcraft.networking.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.satisfy.skillcraft.util.IEntityDataSaver;

public class LevelSyncS2CPacket implements NetworkManager.NetworkReceiver {
    @Override
    public void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();
        String skill = buf.readString();
        int level = buf.readInt();
        ((IEntityDataSaver) player).getPersistentData().putInt(skill, level);
    }
}
