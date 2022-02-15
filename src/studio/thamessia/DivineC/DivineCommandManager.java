/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN JANUARY 2022.
*/
package studio.thamessia.DivineC;

import studio.thamessia.Utils.ColorsUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class DivineCommandManager {
    public static void interpreterManager() {
        Scanner scanner = new Scanner(System.in);

        ColorsUtils.setColor("cyan");
        System.out.print("Insert file: ");
        ColorsUtils.setColor("red");
        String fileName = scanner.nextLine();

        Interpreter.interpret(fileName);
    }
}
