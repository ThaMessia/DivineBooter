package studio.thamessia.Crashers;

public enum Mode {
    NORMAL_LEFT(0),
    NORMAL_RIGHT(1);

    public final int value;

    Mode(int value) {
        this.value = value;
    }
}