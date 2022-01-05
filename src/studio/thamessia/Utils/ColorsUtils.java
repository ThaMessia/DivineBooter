package studio.thamessia.Utils;

public class ColorsUtils {
    public static void setColor(String color) {
        if (color.equalsIgnoreCase("cyan")) System.out.print("\u001B[36m");
        if (color.equalsIgnoreCase("yellow")) System.out.print("\u001B[33m");
        if (color.equalsIgnoreCase("red")) System.out.print("\u001B[31m");
    }
}
