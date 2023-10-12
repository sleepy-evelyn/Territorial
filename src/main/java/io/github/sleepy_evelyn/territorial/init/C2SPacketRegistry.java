package io.github.sleepy_evelyn.territorial.init;

import io.github.sleepy_evelyn.territorial.networking.c2s.AddEclipseEffectPacket;
import io.github.sleepy_evelyn.territorial.networking.c2s.C2SPacket;
import io.github.sleepy_evelyn.territorial.networking.c2s.CancelPeekingPacket;

public final class C2SPacketRegistry {

    public static void initialize() {
        C2SPacket.register(AddEclipseEffectPacket.ID, new AddEclipseEffectPacket());
        C2SPacket.register(CancelPeekingPacket.ID, new CancelPeekingPacket());
    }
}
