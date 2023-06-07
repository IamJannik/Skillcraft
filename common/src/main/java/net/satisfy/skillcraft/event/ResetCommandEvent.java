package net.satisfy.skillcraft.event;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.json.SkillLoader;
import net.satisfy.skillcraft.skill.SkillData;
import net.satisfy.skillcraft.util.IEntityDataSaver;

public class ResetCommandEvent implements CommandRegistrationEvent {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registry, CommandManager.RegistrationEnvironment selection) {
        dispatcher.register(CommandManager.literal("skillcraft")
                .then(CommandManager.literal("reset")
                .executes(context -> {
                    if (context.getSource().getPlayer() instanceof IEntityDataSaver) {
                        for (Identifier identifier : SkillLoader.REGISTRY_SKILLS.keySet()) {
                            SkillData.resetSkill((IEntityDataSaver) context.getSource().getPlayer(), identifier.toString());
                        }
                    } else {
                        context.getSource().sendError(Text.literal("Player not found."));
                    }
                    return 1;
                }))
        );

    }
}
