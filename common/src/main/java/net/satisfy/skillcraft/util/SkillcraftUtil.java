package net.satisfy.skillcraft.util;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;

public class SkillcraftUtil {
    public static PacketByteBuf createPacketBuf(){
        return new PacketByteBuf(Unpooled.buffer());
    }
}
