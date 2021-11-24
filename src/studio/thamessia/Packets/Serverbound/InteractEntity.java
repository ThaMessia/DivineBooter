package studio.thamessia.Packets.Serverbound;

import studio.thamessia.Packets.Packet;
import studio.thamessia.Utils.DataTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class InteractEntity extends Packet {
    private final int entityID;
    private final Type type;
    private final boolean sneaking;

    public InteractEntity(int entityID, Type type, boolean sneaking) {
        super(0x0D);
        this.entityID = entityID;
        this.type = type;
        this.sneaking = sneaking;
    }


    @Override
    public Packet readPacket(DataInputStream dis) {
        return null;
    }

    @Override
    public void writeData() {
        try {
            DataTypes.writeVarInt(dataOutputStream, entityID);
            DataTypes.writeVarInt(dataOutputStream, type.value);
            dataOutputStream.writeBoolean(sneaking);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
