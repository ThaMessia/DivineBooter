package studio.thamessia.Crashers;

import studio.thamessia.Packets.Packet;
import studio.thamessia.Utils.DataTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class ClickWindowPacket extends Packet<ClickWindowPacket> {
    private final byte windowID;
    private final int stateID;
    private final short slot;
    private final byte button;
    private final Mode mode;
    private final int arrayLength;

    public ClickWindowPacket(byte windowID, int stateID, short slot, byte button, Mode mode, int arrayLength) {
        super(0x08);
        this.windowID = windowID;
        this.stateID = stateID;
        this.slot = slot;
        this.button = button;
        this.mode = mode;
        this.arrayLength = arrayLength;
    }

    @Override
    public ClickWindowPacket readPacket(DataInputStream dis) {
        return null;
    }

    @Override
    public void writeData() {
        writeID();
        int windowIDManager = windowID & 0xFF;
        try {
            dataOutputStream.writeByte(windowIDManager);
            DataTypes.writeVarInt(dataOutputStream, stateID);
            dataOutputStream.writeShort(slot);
            dataOutputStream.writeByte(button);
            DataTypes.writeVarInt(dataOutputStream, mode.value);
            DataTypes.writeVarInt(dataOutputStream, arrayLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
