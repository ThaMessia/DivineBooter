package studio.thamessia.DivineC;

import java.io.DataOutputStream;
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
                } else if (fileStringManager.contains("sendMinecraftPacketByte")) {
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketByte", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    String[] complexHost = fileStringManager.split(",");
                    String[] complexByte = fileStringManager.split("'");

                    String simpleHost = complexHost[0];
                    int simplePort = Integer.parseInt(complexHost[1]);
                    byte simpleByte = Byte.parseByte(complexByte[1]);

                    InetSocketAddress inetSocketAddress = new InetSocketAddress(simpleHost, simplePort);

                    Socket socket = new Socket();
                    socket.connect(inetSocketAddress);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeByte(simpleByte);
                } else if (fileStringManager.contains("sendMinecraftPacketInt")) {
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketByte", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    String[] complexHost = fileStringManager.split(",");
                    String[] complexInt = fileStringManager.split("'");

                    String simpleHost = complexHost[0];
                    int simplePort = Integer.parseInt(complexHost[1]);
                    int simpleInt = Integer.parseInt(complexInt[1]);

                    InetSocketAddress inetSocketAddress = new InetSocketAddress(simpleHost, simplePort);

                    Socket socket = new Socket();
                    socket.connect(inetSocketAddress);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeInt(simpleInt);
                } else if (fileStringManager.contains("sendMinecraftPacketString")) {
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketString", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    String[] complexHost = fileStringManager.split(",");
                    String[] complexString = fileStringManager.split("'");

                    String simpleHost = complexHost[0];
                    int simplePort = Integer.parseInt(complexHost[1]);
                    String simpleString = complexString[1];

                    InetSocketAddress inetSocketAddress = new InetSocketAddress(simpleHost, simplePort);

                    Socket socket = new Socket();
                    socket.connect(inetSocketAddress);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF(simpleString);
                } else if (fileStringManager.contains("getOsInfo")) {
                    System.out.println(System.getProperty("os.name"));
                    System.out.println(System.getProperty("os.version"));
                    System.out.println(System.getProperty("os.arch"));
                    System.out.println(System.getProperty("user.name"));
                    System.out.println(System.getProperty("user.home"));
                } else if (fileStringManager.contains("getOsName")) {
                    System.out.println(System.getProperty("os.name"));
                } else if (fileStringManager.contains("getOsVersion")) {
                    System.out.println(System.getProperty("os.version"));
                } else if (fileStringManager.contains("getOsArch")) {
                    System.out.println(System.getProperty("os.arch"));
                } else if (fileStringManager.contains("getUserName")) {
                    System.out.println(System.getProperty("user.name"));
                } else if (fileStringManager.contains("getUserHome")) {
                    System.out.println(System.getProperty("user.home"));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}