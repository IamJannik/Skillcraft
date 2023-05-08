package net.satisfy.skillcraft.skill;

import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.satisfy.skillcraft.networking.SkillcraftNetworking;
import net.satisfy.skillcraft.util.IEntityDataSaver;
import net.satisfy.skillcraft.util.SkillcraftUtil;

public class SkillData {
    public static int grantSkill(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int level = nbt.getInt("level");
        level += amount;

        nbt.putInt("level", level);
        syncSkill(level, (ServerPlayerEntity) player);
        return level;
    }

    public static int resetSkill(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        int level = 0;
        nbt.putInt("level", level);
        syncSkill(level, (ServerPlayerEntity) player);
        return level;
    }

    public static void syncSkill(int level, ServerPlayerEntity player) {
        PacketByteBuf buffer = SkillcraftUtil.createPacketBuf();
        buffer.writeInt(level);
        NetworkManager.sendToPlayer(player, SkillcraftNetworking.SKILL_LEVEL_SYNC_ID, buffer);
    }
}
