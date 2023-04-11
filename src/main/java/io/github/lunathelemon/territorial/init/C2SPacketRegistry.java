package io.github.lunathelemon.territorial.init;

import io.github.lunathelemon.territorial.networking.c2s.AddEclipseEffectPacket;
import io.github.lunathelemon.territorial.networking.c2s.C2SPacket;
import io.github.lunathelemon.territorial.networking.c2s.CancelPeekingPacket;

public final class C2SPacketRegistry {

    public static void register() {
        C2SPacket.register(AddEclipseEffectPacket.ID, new AddEclipseEffectPacket());
        C2SPacket.register(CancelPeekingPacket.ID, new CancelPeekingPacket());
    }
}
