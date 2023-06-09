package net.bmjo.skillcraft.event;

import dev.architectury.event.events.common.PlayerEvent;
import net.bmjo.skillcraft.util.IEntityDataSaver;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerCloneEvent implements PlayerEvent.PlayerClone {
    @Override
    public void clone(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean wonGame) {
        if (newPlayer instanceof IEntityDataSaver newPlayerSaver && oldPlayer instanceof IEntityDataSaver oldPlayerSaver) {
            newPlayerSaver.setPersistentData(oldPlayerSaver.getPersistentData());
        }
    }
}
