package init.upinmcSE;

import init.upinmcSE.components.TcpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        TcpServer tcpServer = new TcpServer();
        tcpServer.startServer();

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);

            while(true){
                clientSocket = serverSocket.accept();
                Socket finalClientSocket = clientSocket;
                CompletableFuture.runAsync(() -> {
                    try {
                        handleClient(finalClientSocket);
                    } catch (IOException e) {
                        System.out.println("IOException: " + e.getMessage());
                    }
                });
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }

    public static void handleClient(Socket clientSocket) throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();
        Scanner sc = new Scanner(inputStream);

        while(sc.hasNextLine()){
            String nextLine = sc.nextLine();
            System.out.println(nextLine);
            if(nextLine.contains("PING")){
                outputStream.write("THANH\r\n".getBytes());
            }
            if(nextLine.contains("ECHO")){
                String respHeader = sc.nextLine();
                String respBody = sc.nextLine();
                String response = respHeader + "\r\n" + respBody + "\r\n";
                outputStream.write(response.getBytes());
            }
        }

    }

    public static String encodingRespString(String s){
        String resp = "$";
        resp += s.length();
        resp += "\r\n";
        resp += s;
        resp += "\r\n";
        return resp;
    }
}