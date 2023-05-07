package net.satisfy.skillcraft.event;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.satisfy.skillcraft.networking.SkillcraftNetworking;
import org.lwjgl.glfw.GLFW;

import static net.satisfy.skillcraft.util.SkillcraftUtil.createPacketBuf;

@Environment(EnvType.CLIENT)
public class KeyInputHandler {
    public static final String KEY_CATEGORY_SKILLCRAFT = "key.category.skillcraft";
    public static final String KEY_OPEN_SKILL_BOOK = "key.skillcraft.openskillbook";

    public static KeyBinding openSkillBookKey = new KeyBinding(
            KEY_OPEN_SKILL_BOOK,
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            KEY_CATEGORY_SKILLCRAFT
    );

    public static void registerKeyInputs() {
        ClientTickEvent.CLIENT_POST.register(client -> {
            if(openSkillBookKey.wasPressed() && client.player != null) {
                client.player.sendMessage(Text.of("Imagine now the book is opened."), true);
                NetworkManager.sendToServer(SkillcraftNetworking.SKILL_LEVEL_ID, createPacketBuf());
            }
        });
    }

    public static void register() {
        KeyMappingRegistry.register(openSkillBookKey);

        registerKeyInputs();
    }
}
