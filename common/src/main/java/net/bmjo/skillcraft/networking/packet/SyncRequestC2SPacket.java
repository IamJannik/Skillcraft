package net.bmjo.skillcraft.networking.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.bmjo.skillcraft.json.SkillLoader;
import net.bmjo.skillcraft.skill.SkillData;
import net.bmjo.skillcraft.util.IEntityDataSaver;

public class SyncRequestC2SPacket implements NetworkManager.NetworkReceiver {
    @Override
    public void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
        for (Identifier identifier: SkillLoader.REGISTRY_SKILLS.keySet()) {
            SkillData.syncSkill(identifier.toString(), ((IEntityDataSaver)player).getPersistentData().getInt(identifier.toString()), player);
        }
    }
}
