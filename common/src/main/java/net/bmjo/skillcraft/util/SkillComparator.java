package net.bmjo.skillcraft.util;

import net.minecraft.util.Identifier;
import net.bmjo.skillcraft.Skillcraft;

import java.util.Comparator;

public class SkillComparator implements Comparator<Identifier> {
    @Override
    public int compare(Identifier i1, Identifier i2) {
        int compare = i1.toString().compareTo(i2.toString());
        return i1.getNamespace().equals(Skillcraft.MOD_ID) ? i2.getNamespace().equals(Skillcraft.MOD_ID) ? compare : -1 : i2.getNamespace().equals(Skillcraft.MOD_ID) ? 1 : compare;
    }
}
