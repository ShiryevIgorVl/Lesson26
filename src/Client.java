import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String IP_ADDR = "localhost";
    private static final int SERVER_PORT = 8189;

    static private Socket socket;
    static Scanner sc;
    static DataInputStream in;
    static DataOutputStream out;

    public static void main(String[] args)  {
        try {
            System.out.println("Клиент запущен");
            socket = new Socket(IP_ADDR, SERVER_PORT);
            System.out.println("Сервер подключился");

            sc = new Scanner(System.in);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread threadIn = new Thread(new Runnable() {
            String msg;
            @Override
            public void run() {
                while (true) {
                    try {
                        msg = in.readUTF();
                        if (msg.equals("/end")){
                            System.out.println("Сервер отключился");
                            break;
                        }
                        System.out.println("Сервер: " + msg);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                 }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    });

        Thread threadOut = new Thread(new Runnable() {
            @Override
            public void run() {
                String msg = sc.nextLine();
                while (true) {
                    try {
                        out.writeUTF(msg);
                        System.out.println("Клиент: " + msg);
                    } catch(IOException ioException){
                        ioException.printStackTrace();
                    }
                }
            }

        });

        threadIn.start();
        threadIn.run();
        threadOut.start();
        threadOut.run();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
}
}
