package studio.thamessia.Packets;

import studio.thamessia.Main;
import studio.thamessia.Packets.Serverbound.InteractEntity;

import java.io.IOException;

public class Semaphore {
    private static InteractEntity interactEntity;

    public synchronized void attack() throws IOException {
        //for (;;) interactEntity.sendPacket();
    }
}
