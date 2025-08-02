package init.upinmcSE.components;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

@Component
@NoArgsConstructor
public class TcpServer {
    @Autowired
    private RespSerializer respSerializer;

    public void startServer() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            int id = 0;
            while (true) {
                clientSocket = serverSocket.accept();
                id++;
                Socket finalClientSocket = clientSocket;

                InputStream inputStream = finalClientSocket.getInputStream();
                OutputStream outputStream = finalClientSocket.getOutputStream();

                Client client = new Client(finalClientSocket, inputStream, outputStream, id);
                CompletableFuture.runAsync(() -> {
                    try {
                        handleClient(client);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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

    public void handleClient(Client client) throws IOException {
        while (client.socket.isConnected()) {
            byte[] buffer = new byte[client.socket.getReceiveBufferSize()];
            int bytesRead = client.inputStream.read(buffer);
            if (bytesRead > 0) {
                // bytes parsing into strings
                List<String[]> commands = respSerializer.deseralize(buffer);
                for (String[] command : commands) {
                    handleCommand(command, client);
                }
            }
        }

//        Scanner sc = new Scanner(client.inputStream);
//        while(sc.hasNextLine()){
//            String nextLine = sc.nextLine();
//            System.out.println(nextLine);
//            if(nextLine.contains("PING")){
//                outputStream.write("THANH\r\n".getBytes());
//            }
//            if(nextLine.contains("ECHO")){
//                String respHeader = sc.nextLine();
//                String respBody = sc.nextLine();
//                String response = respHeader + "\r\n" + respBody + "\r\n";
//                outputStream.write(response.getBytes());
//            }
//    }
    }

    private void handleCommand(String[] command, Client client) {
        for (String s : command) {

        }
    }
}

