/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN NOVEMBER 2021.
*/
package studio.thamessia.Utils;

import studio.thamessia.Packets.Packet;
import studio.thamessia.Utils.DataTypes;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class UncompressedPacket extends Packet {
    private int ID;
    private final ByteArrayOutputStream byteArrayOutputStream =
            new ByteArrayOutputStream();
    public final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);


    public UncompressedPacket(int id) {
        super(id);
    }

    public int writeID() {
        try {
            DataTypes.writeVarInt(dataOutputStream, ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void sendPacket(DataOutputStream dos) {
        writeData();
        try {
            DataTypes.writeVarInt(dos, byteArrayOutputStream.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.write(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}