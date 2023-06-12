package net.bmjo.skillcraft.mixin;

import net.bmjo.skillcraft.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class SkillcraftEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        return this.persistentData == null ? this.persistentData = new NbtCompound() : this.persistentData;
    }

    @Override
    public void setPersistentData(NbtCompound persistentData) {
        this.persistentData = persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (this.persistentData != null) {
            nbt.put("skillcraft.skill_data", this.persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At(value = "HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("skillcraft.skill_data")) {
            this.persistentData = nbt.getCompound("skillcraft.skill_data");
        }
    }
}
