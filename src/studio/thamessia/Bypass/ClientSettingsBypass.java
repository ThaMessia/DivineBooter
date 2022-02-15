/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN DECEMBER 2021.
*/
package studio.thamessia.Bypass;

import studio.thamessia.Packets.Packet;
import studio.thamessia.Utils.DataTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientSettingsBypass extends Packet {
    private final String locale;
    private final byte viewDistance;
    private final ChatMode chatMode;
    private final boolean chatColors;
    private final byte displayedSkinParts;
    private final MainHand mainHand;
    private final boolean textFilteringEnabled;
    private final boolean allowServerListenings;

    public ClientSettingsBypass(String locale, byte viewDistance, ChatMode chatMode, boolean chatColors, byte displayedSkinParts, MainHand mainHand, boolean textFilteringEnabled, boolean allowServerListenings) {
        super(0x05);
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.chatMode = chatMode;
        this.chatColors = chatColors;
        this.displayedSkinParts = displayedSkinParts;
        this.mainHand = mainHand;
        this.textFilteringEnabled = textFilteringEnabled;
        this.allowServerListenings = allowServerListenings;
    }

    @Override
    public Packet readPacket(DataInputStream dis) {
        return null;
    }

    @Override
    public void writeData() {
        writeID();
        try {
            dataOutputStream.writeUTF(locale);
            dataOutputStream.writeByte(viewDistance);
            DataTypes.writeVarInt(dataOutputStream, chatMode.value);
            dataOutputStream.writeBoolean(chatColors);

            int skinPartsManager = displayedSkinParts & 0xFF;
            dataOutputStream.writeByte(skinPartsManager);

            DataTypes.writeVarInt(dataOutputStream, mainHand.value);
            dataOutputStream.writeBoolean(textFilteringEnabled);
            dataOutputStream.writeBoolean(allowServerListenings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
