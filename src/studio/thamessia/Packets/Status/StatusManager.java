package studio.thamessia.Packets.Status;

import studio.thamessia.Packets.Handshake.HandshakePacket;
import studio.thamessia.Packets.Handshake.NextState;
import studio.thamessia.Packets.Status.StatusRequestPacket;
import studio.thamessia.Packets.Status.StatusResponsePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class StatusManager {

    public static String serverStatus(String host, int port) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream =
                    new DataOutputStream(socket.getOutputStream());

            HandshakePacket handshakePacket = new HandshakePacket(53550 /*sesso*/, host, port, NextState.STATUS);
            handshakePacket.sendPacket(dataOutputStream);

            new StatusRequestPacket().sendPacket(dataOutputStream);
            String a = new StatusResponsePacket().readPacket(dataInputStream).getResponse().trim();
            StringBuilder response = new StringBuilder();

            boolean open = false;

            for(char c : a.toCharArray()) { //golias' algorithm
                if (!open && c == '{') {
                    open = true;
                    response.append(c);
                    continue;
                }

                if (open) response.append(c);
            }
            socket.close();

            return response.toString();
        } catch (ConnectException e) {
            System.err.println("[DivineBooter] Server is offline.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.print("");
        }
        return null;
    }
}