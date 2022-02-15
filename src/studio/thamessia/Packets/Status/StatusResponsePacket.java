/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN OCTOBER 2021.
*/
package studio.thamessia.Packets.Status;

import studio.thamessia.Packets.Packet;
import studio.thamessia.Utils.DataTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class StatusResponsePacket extends Packet<StatusResponsePacket> {
    private String response;

    public StatusResponsePacket() {
        super(0x01);
    }

    @Override
    public StatusResponsePacket readPacket(DataInputStream dis) {
        try {
            response = DataTypes.readString(dis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void writeData() {

    }

    public String getResponse() {
        return response;
    }
}