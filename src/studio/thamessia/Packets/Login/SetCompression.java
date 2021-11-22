package studio.thamessia.Packets.Login;

import studio.thamessia.Packets.Packet;
import studio.thamessia.Utils.DataTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class SetCompression extends Packet<SetCompression> {
    private int threshold;

    public SetCompression() {
        super(0x03);
    }

    @Override
    public SetCompression readPacket(DataInputStream dis) {
        try {
            DataTypes.readVarInt(dis);
            int id = DataTypes.readVarInt(dis);
            //System.out.println(id);
            threshold = DataTypes.readVarInt(dis);
            //System.out.println(threshold); //256
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void writeData() {

    }

    public int getThreshold() {
        return threshold;
    }
}