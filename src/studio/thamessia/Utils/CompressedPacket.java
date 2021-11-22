package studio.thamessia.Utils;

import studio.thamessia.Packets.Packet;

import java.io.*;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public abstract class CompressedPacket extends Packet {
    private final int ID;
    private final int threshold;
    private final ByteArrayOutputStream byteArrayOutputStream =
            new ByteArrayOutputStream();
    public final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

    public CompressedPacket(int ID, int threshold) {
        super(ID);
        this.ID = ID;
        this.threshold = threshold;
    }

    public void sendPacket(DataOutputStream dataOutputStream) {
        writeData(); // write the data

        // That part is used to get the length of data length + the length of the ByteArrayOutputStream
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream bytesDos = new DataOutputStream(bytes);
        try {
            DataTypes.writeVarInt(bytesDos, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bytesDos.write(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // send the length of the data length + the length of the data
        try {
            DataTypes.writeVarInt(dataOutputStream, bytes.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            DataTypes.writeVarInt(dataOutputStream, 0); // send the size of the data (0 for non compressed)
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataOutputStream.write(byteArrayOutputStream.toByteArray()); // write compressed data
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int writeID() {
        try {
            DataTypes.writeVarInt(dataOutputStream, ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}