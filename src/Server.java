import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static int PORT = 8189;
    static Scanner sc;
    static DataInputStream in;
    static DataOutputStream out;

    public static void main(String[] args)  {
        ServerSocket server = null;
        Socket socket = null;
        try {
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен");
            socket = server.accept();
            System.out.println("Клиент подключился");


            sc = new Scanner(System.in);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


        Thread threadIn = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String msg = in.readUTF();

                        if (msg.equals("/end")) {
                            System.out.println("Клиент отключился");
                            break;
                        }
                        System.out.println("Клиент: " + msg);
                    }
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
                        System.out.println("Сервер: " + msg);
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
