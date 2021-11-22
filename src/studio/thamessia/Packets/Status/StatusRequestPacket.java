package studio.thamessia.Packets.Status;

import studio.thamessia.Packets.Packet;

import java.io.DataInputStream;

public class StatusRequestPacket extends Packet<StatusRequestPacket> {

    public StatusRequestPacket() {
        super(0x00);
    }

    @Override
    public StatusRequestPacket readPacket(DataInputStream dis) {
        return null;
    }

    @Override
    public void writeData() {
        writeID();
    }
}