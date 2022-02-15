/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN OCTOBER 2021.
*/
package studio.thamessia.Utils;

import studio.thamessia.Packets.Packet;

import java.io.DataInputStream;
import java.io.IOException;

public class PacketSetCompression extends UncompressedPacket {
    private int threshold;

    public PacketSetCompression() {
        super(0x03);
    }

    @Override
    public Packet readPacket(DataInputStream dataInputStream) {
        try {
            threshold = DataTypes.readVarInt(dataInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void writeData() {

    }

    public int getThreshold() {
        return threshold;
    }
}