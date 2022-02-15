/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN DECEMBER 2021.
*/
package studio.thamessia.Bypass;

import studio.thamessia.Packets.Packet;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

public class PlayerRotationBypass extends Packet {
    //private final double x;
    //private final double y;
    //private final double z;
    private final float yaw;
    private final float pitch;

    private final boolean onGround;

    public PlayerRotationBypass(float yaw, float pitch, boolean onGround) {
        super(0x13);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Override
    public Packet readPacket(DataInputStream dis) {
        return null;
    }

    @Override
    public void writeData() { //GO TRIGONOMETRY
        writeID();

        int x = 0b1010000;
        int y = 0b1011010;
        int z = 0b1100100;

        int dx = x - x;
        int dy = y - y;
        int dz = z - z;

        int r = 0b10010000;
        r += (int) Math.sqrt(dx * dx + dy * dy + dz * dz);
        float yaw = (float) (-Math.atan(dx) / Math.PI * 180);
        yaw += -Math.atan(dz) / Math.PI * 180;

        if (yaw < 0) yaw = yaw + 360;
        float pitch = (float) (-Math.asin(dy / r) / Math.PI * 180);

        if (yaw == 360) yaw = 0;
        yaw = 50;
        pitch = 85;

        yaw = this.yaw;
        pitch = this.pitch;

        try {
            dataOutputStream.writeFloat(yaw);
            dataOutputStream.writeFloat(pitch);

            yaw++;
            pitch++;
            Thread.sleep(1000);

            dataOutputStream.writeFloat(yaw);
            dataOutputStream.writeFloat(pitch);

            dataOutputStream.writeFloat(yaw);
            dataOutputStream.writeFloat(pitch);

            yaw--;
            pitch--;
            Thread.sleep(1000);

            dataOutputStream.writeFloat(yaw);
            dataOutputStream.writeFloat(pitch);

            dataOutputStream.writeBoolean(onGround);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
