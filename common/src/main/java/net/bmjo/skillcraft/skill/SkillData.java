package net.bmjo.skillcraft.skill;

import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.bmjo.skillcraft.networking.SkillcraftNetworking;
import net.bmjo.skillcraft.util.IEntityDataSaver;
import net.bmjo.skillcraft.util.SkillcraftUtil;

public class SkillData {

    public static int grantSkill(IEntityDataSaver player, String skill, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int level = nbt.getInt(skill);
        level += amount;

        nbt.putInt(skill, level);
        syncSkill(skill, level, (ServerPlayerEntity) player);
        return level;
    }

    public static void resetSkill(IEntityDataSaver player, String skill) {
        NbtCompound nbt = player.getPersistentData();
        int level = 0;
        nbt.putInt(skill, level);
        syncSkill(skill, level, (ServerPlayerEntity) player);
    }

    public static void syncSkill(String skill, int level, ServerPlayerEntity player) {
        PacketByteBuf buf = SkillcraftUtil.createPacketBuf();
        buf.writeString(skill);
        buf.writeInt(level);
        NetworkManager.sendToPlayer(player, SkillcraftNetworking.SKILL_SYNC_ID, buf);
    }
}
