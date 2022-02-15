/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN SEPTEMBER 2021.
*/
package studio.thamessia.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataTypes {
    public static int readVarInt(final DataInputStream dataInputStream) throws IOException {
        int var1 = 0;
        int var2 = 0;
        byte var3;

        do {
            var3 = dataInputStream.readByte();
            var1 |= (var3 & 127) << var2++ * 7;

            if (var2 > 5) {
                throw new RuntimeException("VarInt too big");
            }
        }
        while ((var3 & 128) == 128);

        return var1;
    }

    public static void writeVarInt(final DataOutputStream dataOutputStream, int p_150787_1_) throws IOException {
        while ((p_150787_1_ & -128) != 0) {
            dataOutputStream.writeByte(p_150787_1_ & 127 | 128);
            p_150787_1_ >>>= 7;
        }

        dataOutputStream.writeByte(p_150787_1_);
    }

    public static String readString(final DataInputStream dataInputStream) throws IOException {
        int var2 = readVarInt(dataInputStream);
        return new String(dataInputStream.readNBytes(var2),
                StandardCharsets.UTF_8);
    }

    public static void writeString(final DataOutputStream dataOutputStream, String p_150785_1_) throws IOException {
        byte[] var2 = p_150785_1_.getBytes(StandardCharsets.UTF_8);
        if (var2.length > 32767) {
            System.out.println("The value is too long!");
            return;
        }
        writeVarInt(dataOutputStream, var2.length);
        dataOutputStream.write(var2, 0, var2.length);
    }

}