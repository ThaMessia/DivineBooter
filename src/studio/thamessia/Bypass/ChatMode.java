package studio.thamessia.Bypass;

import studio.thamessia.Utils.DataTypes;

public enum ChatMode {
    ENABLED(0),
    C_ONLY(1),
    HIDDEN(2);

    public final int value;

    ChatMode(int value) {
        this.value = value;
    }
}
