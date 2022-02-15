/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN SEPTEMBER 2021.
*/
package studio.thamessia.Packets;

import studio.thamessia.Utils.DataTypes;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet<T extends Packet<T>> {
    private final int ID; //packet id
    //data
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    public final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

    protected Packet(int id) {
        this.ID = id;
    }

    public void sendPacket(DataOutputStream dos) throws IOException {
        writeData();
        DataTypes.writeVarInt(dos, byteArrayOutputStream.size()); //sends the sum of packetid and data in bytes
        dos.write(byteArrayOutputStream.toByteArray()); //writes data in byte array
    }

    public int writeID() {
        try {
            DataTypes.writeVarInt(dataOutputStream, ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public abstract T readPacket(DataInputStream dis);

    public abstract void writeData();
}
