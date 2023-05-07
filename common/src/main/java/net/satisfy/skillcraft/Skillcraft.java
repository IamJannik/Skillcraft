package net.satisfy.skillcraft;

import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.Registries;
import net.satisfy.skillcraft.skill.Skills;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class Skillcraft {
    public static final String MOD_ID = "skillcraft";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    
    public static void init() {
        Skills.init();
        System.out.println(SkillcraftExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
