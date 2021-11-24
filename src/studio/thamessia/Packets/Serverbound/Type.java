package studio.thamessia.Packets.Serverbound;

public enum Type {
    INTERACT(0x1),
    ATTACK(0x2),
    INTERACT_AT(0x3);

    public final int value;

    Type(int value) {
        this.value = value;
    }
}
