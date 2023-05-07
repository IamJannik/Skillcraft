package net.satisfy.skillcraft.skill;

import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.Skillcraft;
import net.satisfy.skillcraft.SkillcraftIdentifier;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;

public class Skills {
    public static ArrayList<Identifier> SKILLSETS = Lists.newArrayList();
    public static final Identifier COMBAT_SKILL = createSkillset(new SkillcraftIdentifier("combat"));
    public static final Identifier BUILDING_SKILL = createSkillset(new SkillcraftIdentifier("build"));

    private static Identifier createSkillset(Identifier name) {
        SKILLSETS.add(name);
        return name;
    }

    public static void init() {
        Skillcraft.LOGGER.debug("Initiating " + Skillcraft.MOD_ID + " Skills in " + Skills.class);
    }
}
