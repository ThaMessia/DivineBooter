package studio.thamessia.DivineC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Interpreter {
    protected static void interpret(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            String fileStringManager = null;
            //String commandManager = null;

            while (scanner.hasNext()) {
                fileStringManager = scanner.next();

                if (fileStringManager.contains("printf")) {
                    fileStringManager = fileStringManager.replace("printf", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    System.out.print(fileStringManager);
                } else if (fileStringManager.contains("println")) {
                    fileStringManager = fileStringManager.replace("println", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    System.out.println(fileStringManager);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
