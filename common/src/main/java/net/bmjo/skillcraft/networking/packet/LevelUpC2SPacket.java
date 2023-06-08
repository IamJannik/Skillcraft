package net.bmjo.skillcraft.networking.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.bmjo.skillcraft.client.toast.LevelUpToast;
import net.bmjo.skillcraft.json.SkillLoader;
import net.bmjo.skillcraft.skill.SkillData;
import net.bmjo.skillcraft.skill.Skillset;
import net.bmjo.skillcraft.util.IEntityDataSaver;

public class LevelUpC2SPacket implements NetworkManager.NetworkReceiver {
    @Override
    public void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        String skill = buf.readString();
        int amount = buf.readInt();
        int cost = buf.readInt();
        int level = SkillData.grantSkill((IEntityDataSaver)player, skill, amount);

        player.addExperienceLevels(-cost);

        ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
        Skillset skillset = SkillLoader.REGISTRY_SKILLS.get(new Identifier(skill));
        for (int leveled = level - amount + 1; leveled <= level; leveled++) {
            LevelUpToast levelUpToast = new LevelUpToast(skillset, leveled);
            toastManager.add(levelUpToast);
        }

    }
}