package net.satisfy.skillcraft.event;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ResetCommandEvent implements CommandRegistrationEvent {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registry, CommandManager.RegistrationEnvironment selection) {
        dispatcher.register(CommandManager.literal("resetskill")
                //.then(CommandManager.literal("reset").then(CommandManager.argument("skill", reader -> SkillLoader.REGISTRY_SKILLS.get(new Identifier(reader.getRead()))).executes(context -> SkillData.resetSkill(context.getArgument("skill", Skillset.class), ,))))
        );

    }
}
