package studio.thamessia.Utils;

public class ColorsUtils {
    public static String cyan = "\u001B[36m";
    public static String yellow = "\u001B[33m";
    public static String red = "\u001B[31m";
    public static String green = "\u001B[32m";
    public static String blue = "\u001B[34m";
    public static String purple = "\u001B[35m";

    public static void setColor(String color) {
        if (color.equalsIgnoreCase("cyan")) System.out.print("\u001B[36m");
        if (color.equalsIgnoreCase("yellow")) System.out.print("\u001B[33m");
        if (color.equalsIgnoreCase("red")) System.out.print("\u001B[31m");
        if (color.equalsIgnoreCase("green")) System.out.print("\u001B[32m");
        if (color.equalsIgnoreCase("blue")) System.out.print("\u001B[34m");
        if (color.equalsIgnoreCase("purple")) System.out.print("\u001B[35m");
    }
}
