/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN OCTOBER 2021.
*/
package studio.thamessia.Packets.Play;


import studio.thamessia.Packets.Packet;
import studio.thamessia.Utils.CompressedPacket;
import studio.thamessia.Utils.DataTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class ChatMessagePacket extends CompressedPacket {
    private final String message;

    public ChatMessagePacket(int id, int threshold, String message) {
        super(id, threshold);
        this.message = message;
    }

    @Override
    public Packet readPacket(DataInputStream dis) {
        return null;
    }

    @Override
    public void writeData() {
        writeID();
        try {
            DataTypes.writeString(dataOutputStream, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}