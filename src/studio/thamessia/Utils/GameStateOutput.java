package studio.thamessia.Utils;

import studio.thamessia.Packets.Play.ChatMessagePacket;

import java.io.DataOutputStream;

public class GameStateOutput {
    private final DataOutputStream dataOutputStream;

    public GameStateOutput(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public void sendMessage(int p_v, int th, String message) {
        ChatMessagePacket packetPlayOutChatMessage = new ChatMessagePacket(
                p_v >= 47 && p_v < 80 ? 0x01 : p_v >= 80 && p_v < 318 ? 0x02 : p_v >= 318 && p_v < 336 ? 0x03 : p_v >= 336 && p_v < 343 ? 0x02 : p_v >= 343 && p_v < 464 ? 0x01 : 0x03,
                th,
                message
        );
        packetPlayOutChatMessage.sendPacket(dataOutputStream);
    }

    //chat message packet that supports compressed and uncompressed packet format
}