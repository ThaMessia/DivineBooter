package studio.thamessia.DivineC;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.util.Scanner;

public class Interpreter {
    public static void interpret(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            String fileStringManager = null;
            String commandManager = null;

            while (scanner.hasNext()) {
                fileStringManager = scanner.next();

                if (fileStringManager.contains("printf")) {
                    commandManager = scanner.nextLine().replace("\"", "").replace("(", "").replace(")", "");
                    System.out.print(commandManager);
                } else if (fileStringManager.contains("println")) {
                    commandManager = scanner.nextLine().replace("\"", "").replace("(", "").replace(")", "");
                    System.out.println(commandManager);
                } else if (fileStringManager.contains("sendpacket")) {

                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }
    }
}
