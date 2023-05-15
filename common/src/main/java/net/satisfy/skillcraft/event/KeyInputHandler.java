package net.satisfy.skillcraft.event;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.satisfy.skillcraft.client.screen.SkillBookScreen;
import org.lwjgl.glfw.GLFW;

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
            if (openSkillBookKey.wasPressed()) {
                client.setScreen(new SkillBookScreen());
            }
        });
    }

    public static void register() {
        KeyMappingRegistry.register(openSkillBookKey);
        registerKeyInputs();
    }
}
