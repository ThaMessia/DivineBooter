package studio.thamessia.Packets.Handshake;

import studio.thamessia.Packets.Packet;
import studio.thamessia.Utils.DataTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class HandshakePacket extends Packet<HandshakePacket> {
    private final int protocolVersion;
    private final String serverAddress;
    private final int serverPort;
    private final NextState nextState;

    public HandshakePacket(int protocolVersion, String serverAddress, int serverPort, NextState nextState) {
        super(0x00);
        this.protocolVersion = protocolVersion;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.nextState = nextState;
    }

    @Override
    public HandshakePacket readPacket(DataInputStream dis) {
        return null;
    }

    @Override
    public void writeData() {
        try {
            writeID();
            DataTypes.writeVarInt(dataOutputStream, protocolVersion);
            DataTypes.writeString(dataOutputStream, serverAddress);
            dataOutputStream.writeShort(serverPort);
            DataTypes.writeVarInt(dataOutputStream, nextState.value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}