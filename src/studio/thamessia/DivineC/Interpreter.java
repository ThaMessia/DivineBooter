package studio.thamessia.DivineC;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Interpreter {
    private static Map<String, Object> variablesManager = new HashMap<>(); //hashmap that will be used to store variables

    protected static void interpret(String fileName) {
        // WARNING: use "°" for spaces instead of " "

        try {
            File file = new File(fileName); //file declaration
            Scanner scanner = new Scanner(file); //scanner for reading from file
            String fileStringManager = null; //string manager to assign file values
            //String commandManager = null;

            while (scanner.hasNext()) { //reading the file until the end
                fileStringManager = scanner.next(); //reads every line

                if (fileStringManager.contains("printf")) { //if the line contains printf... so basically printf("");
                    if (!fileStringManager.contains("\"")) { //if it's a variable
                        //remove 'printf(variable);' so it becomes 'variable'
                        fileStringManager = fileStringManager.replace("printf", "").replace("(", "").replace(")", "").replace(";", "");
                        System.out.print(variablesManager.get(fileStringManager)); //prints the value of the variable
                    } else { //if it's just printing a message
                        //remove 'printf("Hello, World!");' so it just is 'Hello, World!' that will be printed
                        fileStringManager = fileStringManager.replace("printf", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                        System.out.print(fileStringManager); //prints the value of the printf. 'Example: printf("Hello, World")' will be 'Hello, World!'
                    }
                } else if (fileStringManager.contains("println")) { //same thing here
                    if (!fileStringManager.contains("\"")) { //if it's a variable
                        fileStringManager = fileStringManager.replace("println", "").replace("(", "").replace(")", "").replace(";", "");
                        System.out.println(variablesManager.get(fileStringManager));
                    } else { //if it's not
                        fileStringManager = fileStringManager.replace("println", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                        System.out.println(fileStringManager);
                    }
                } else if (fileStringManager.contains("var")) { //if it's something like 'var pippo "ciao";'
                    String[] variableManager = fileStringManager.split("°"); //splits through the spaces in order to get both the name of variable and the value

                    String variableName = variableManager[1]; //gets the second element that is the name of the variable
                    String variableValue = variableManager[2].replace("\"", "").replace(";", ""); //gets the third element that is the value of variable

                    variablesManager.put(variableName, variableValue); //puts both the name and the value of the variable in the variables hashmap
                } else if (fileStringManager.contains("sendMinecraftPacketByte")) { //syntax would be the following:
                    // sendMinecraftPacketByte(host,port'bytetosend);
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketByte", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    String[] complexHost = fileStringManager.split(",");
                    String[] complexByte = fileStringManager.split("'");

                    String simpleHost = complexHost[0]; //gets the name of host
                    int simplePort = Integer.parseInt(complexHost[1]); //gets the port of host
                    byte simpleByte = Byte.parseByte(complexByte[1]); //gets the byte to send

                    InetSocketAddress inetSocketAddress = new InetSocketAddress(simpleHost, simplePort); //creates the address

                    Socket socket = new Socket(); //creates the socket
                    socket.connect(inetSocketAddress); //connects the socket

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeByte(simpleByte); //sends the byte to the server
                } else if (fileStringManager.contains("sendMinecraftPacketInt")) { //IT'S THE SAME FROM NOW ON
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketInt", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
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
                } else if (fileStringManager.contains("sendMinecraftPacketBoolean")) {
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketBoolean", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    String[] complexHost = fileStringManager.split(",");
                    String[] complexBoolean = fileStringManager.split("'");

                    String simpleHost = complexHost[0];
                    int simplePort = Integer.parseInt(complexHost[1]);
                    boolean simpleBoolean = Boolean.parseBoolean(complexBoolean[1]);

                    InetSocketAddress inetSocketAddress = new InetSocketAddress(simpleHost, simplePort);

                    Socket socket = new Socket();
                    socket.connect(inetSocketAddress);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeBoolean(simpleBoolean);
                } else if (fileStringManager.equalsIgnoreCase("sendMinecraftPacketDouble")) {
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketDouble", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    String[] complexHost = fileStringManager.split(",");
                    String[] complexDouble = fileStringManager.split("'"); //if you came here to share hate I remind you that
                                                                                 //double stands for "double the precision as float"
                    String simpleHost = complexHost[0];
                    int simplePort = Integer.parseInt(complexHost[1]);
                    double simpleDouble = Double.parseDouble(complexDouble[1]);

                    InetSocketAddress inetSocketAddress = new InetSocketAddress(simpleHost, simplePort);

                    Socket socket = new Socket();
                    socket.connect(inetSocketAddress);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeDouble(simpleDouble);
                } else if (fileStringManager.contains("getOsInfo")) { //pretty obvious
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