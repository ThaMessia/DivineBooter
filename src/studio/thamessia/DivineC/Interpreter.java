/*
THE FOLLOWING CODE TAKES LIFE THANKS TO THAMESSIA IN JANUARY 2022.

NOTE: Many of you know that I usually don't document code, and sometimes I
      make it unreadable on purpose, but a friend explicitly requested me to
      document it, therefore here it is.

      The following code can be shared and distributed as long as it's clearly
      said that it's my code.
*/
package studio.thamessia.DivineC;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
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
                } else if (fileStringManager.contains("input")) { //if it's something like 'input(variable)' saves the variable 'variable' as a variable and its value is what the user inputted
                    String inputVariableName = fileStringManager.replace("input", "").replace(";", "").replace("(", "").replace(")", "");

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    String inputVariableValue = bufferedReader.readLine();

                    variablesManager.put(inputVariableName, inputVariableValue);
                } else if (fileStringManager.contains("if")) { //conditions
                    // if (true); if (false) return true false;
                    String[] extractCondition = fileStringManager.split(":");

                    fileStringManager = extractCondition[0].replace("if", "").replace("(", "").replace(")", "");

                    ScriptEngineManager factory = new ScriptEngineManager();
                    ScriptEngine engine = factory.getEngineByName("JavaScript");

                    Boolean conditionResult = (Boolean) engine.eval(fileStringManager);

                    if (conditionResult) {
                        //WHO KNOWS
                    }
                } else if (fileStringManager.contains("system")) { //executes system's language (batch, bash...)
                    String cmd = fileStringManager.replace("system", "").replace("(", "").replace(")", "").replace(";", "").replace("\"", "").replace("°", " ").replace("'", "\n");
                    Process process = Runtime.getRuntime().exec(cmd);

                    String processStringManager1 = "";
                    String processStringManager2 = "";

                    StringBuilder stringBuilder = new StringBuilder();

                    BufferedReader inputManager = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader customManager = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                    while ((processStringManager1 = inputManager.readLine()) != null) stringBuilder.append(processStringManager1);
                    while ((processStringManager2 = customManager.readLine()) != null) stringBuilder.append(processStringManager2);

                    System.out.println(stringBuilder);
                } else if (fileStringManager.contains("sendMinecraftPacketByte")) { //syntax would be the following:
                    // sendMinecraftPacketByte(host,port'bytetosend);
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketByte", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    String[] complexHost = fileStringManager.split(",");

                    String simpleHost = complexHost[0]; //gets the name of host
                    int simplePort = Integer.parseInt(complexHost[1]); //gets the port of host
                    byte simpleByte = Byte.parseByte(complexHost[2]); //gets the byte to send

                    InetSocketAddress inetSocketAddress = new InetSocketAddress(simpleHost, simplePort); //creates the address

                    Socket socket = new Socket(); //creates the socket
                    socket.connect(inetSocketAddress); //connects the socket

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeByte(simpleByte); //sends the byte to the server
                } else if (fileStringManager.contains("sendMinecraftPacketInt")) { //IT'S THE SAME FROM NOW ON
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketInt", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    String[] complexHost = fileStringManager.split(",");

                    String simpleHost = complexHost[0];
                    int simplePort = Integer.parseInt(complexHost[1]);
                    int simpleInt = Integer.parseInt(complexHost[2]);

                    InetSocketAddress inetSocketAddress = new InetSocketAddress(simpleHost, simplePort);

                    Socket socket = new Socket();
                    socket.connect(inetSocketAddress);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeInt(simpleInt);
                } else if (fileStringManager.contains("sendMinecraftPacketString")) {
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketString", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    String[] complexHost = fileStringManager.split(",");

                    String simpleHost = complexHost[0];
                    int simplePort = Integer.parseInt(complexHost[1]);
                    String simpleString = complexHost[2];

                    InetSocketAddress inetSocketAddress = new InetSocketAddress(simpleHost, simplePort);

                    Socket socket = new Socket();
                    socket.connect(inetSocketAddress);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF(simpleString);
                } else if (fileStringManager.contains("sendMinecraftPacketBoolean")) {
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketBoolean", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    String[] complexHost = fileStringManager.split(",");

                    String simpleHost = complexHost[0];
                    int simplePort = Integer.parseInt(complexHost[1]);
                    boolean simpleBoolean = Boolean.parseBoolean(complexHost[2]);

                    InetSocketAddress inetSocketAddress = new InetSocketAddress(simpleHost, simplePort);

                    Socket socket = new Socket();
                    socket.connect(inetSocketAddress);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeBoolean(simpleBoolean);
                } else if (fileStringManager.equalsIgnoreCase("sendMinecraftPacketDouble")) {
                    fileStringManager = fileStringManager.replace("sendMinecraftPacketDouble", "").replace("(", "").replace(")", "").replaceAll("\"", "").replace(";", "").replace("°", " ");
                    String[] complexHost = fileStringManager.split(",");
                     //if you came here to share hate I remind you that double stands for "double the precision as float"
                    String simpleHost = complexHost[0];
                    int simplePort = Integer.parseInt(complexHost[1]);
                    double simpleDouble = Double.parseDouble(complexHost[2]);

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
                } else if (fileStringManager.contains("writeFile")) {
                    fileStringManager = fileStringManager.replace("writeFile", "").replace("(", "").replace(")", "").replace(";", "").replace("\"", "");
                    String[] rawData = fileStringManager.split(",");

                    String stringToWrite = rawData[0];
                    String fileNameManager = rawData[1];

                    File file1 = new File(fileNameManager);
                    FileWriter fileWriter = new FileWriter(file1);

                    fileWriter.write(stringToWrite);
                    fileWriter.close();
                } else if (fileStringManager.contains("readAllFile")) {
                    String fileNameManager = fileStringManager.replace("readAllFile", "").replace("(", "").replace(")", "").replace(";", "");

                    File file1 = new File(fileNameManager);
                    Scanner fileReader = new Scanner(new FileReader(file1));

                    String stringFileManager = "";
                    while (fileReader.hasNext()) stringFileManager = fileReader.next(); System.out.println(stringFileManager);
                    fileReader.close();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}