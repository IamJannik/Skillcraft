package net.bmjo.skillcraft.util;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    NbtCompound getPersistentData();
    void setPersistentData(NbtCompound nbt);
}
