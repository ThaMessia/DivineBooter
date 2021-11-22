package studio.thamessia.Packets.Login;

import studio.thamessia.Packets.Packet;
import studio.thamessia.Utils.DataTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class LoginStart extends Packet<LoginStart> {
    private final String name;

    public LoginStart(String name) {
        super(0x00);
        this.name = name;
    }

    @Override
    public LoginStart readPacket(DataInputStream dis) {
        return null;
    }

    @Override
    public void writeData() {
        try {
            writeID();
            DataTypes.writeString(dataOutputStream, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
