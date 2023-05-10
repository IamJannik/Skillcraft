package net.satisfy.skillcraft.skill;

import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.satisfy.skillcraft.networking.SkillcraftNetworking;
import net.satisfy.skillcraft.util.IEntityDataSaver;
import net.satisfy.skillcraft.util.SkillcraftUtil;

public class SkillData {
    public static int grantSkill(IEntityDataSaver player, String key, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int level = nbt.getInt(key);
        level += amount;

        nbt.putInt(key, level);
        syncSkill(level, (ServerPlayerEntity) player);
        return level;
    }

    public static int resetSkill(IEntityDataSaver player, String key) {
        NbtCompound nbt = player.getPersistentData();
        int level = 0;
        nbt.putInt(key, level);
        syncSkill(level, (ServerPlayerEntity) player);
        return level;
    }

    public static void syncSkill(int level, ServerPlayerEntity player) {
        PacketByteBuf buffer = SkillcraftUtil.createPacketBuf();
        buffer.writeInt(level);
        NetworkManager.sendToPlayer(player, SkillcraftNetworking.SKILL_LEVEL_SYNC_ID, buffer);
    }
}
