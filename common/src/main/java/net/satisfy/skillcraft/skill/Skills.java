package net.satisfy.skillcraft.skill;

import net.minecraft.util.Identifier;
import net.satisfy.skillcraft.Skillcraft;
import net.satisfy.skillcraft.SkillcraftIdentifier;
import net.satisfy.skillcraft.json.SkillReader;

import java.util.List;

public class Skills {
    public static final List<Skillset> SKILLSETS = List.of();
    public static final Skillset COMBAT_SKILL = createSkillset(new SkillcraftIdentifier("combat"));
    public static final Skillset BUILDING_SKILL = createSkillset(new SkillcraftIdentifier("build"));

    private static Skillset createSkillset(Identifier name) {
        return SkillReader.readJson(name);
    }

    public static void init() {
        Skillcraft.LOGGER.debug("Initiating " + Skillcraft.MOD_ID + " Skills in " + Skills.class);
    }
}
