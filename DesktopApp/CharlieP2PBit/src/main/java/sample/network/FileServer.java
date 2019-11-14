package sample.network;

import com.sun.xml.internal.bind.v2.util.ByteArrayOutputStreamEx;
import sample.helpers.FileProviderForPeers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    private ServerSocket serverSocket;

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        while (true)
            new EchoClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws Exception {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                ByteArrayOutputStream out = (ByteArrayOutputStream) clientSocket.getOutputStream();
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.contains("DOWNLOAD")) {
                        String[] parts = inputLine.split(" ");
                        // TODO: send byte array
                        FileProviderForPeers fileProviderForPeers = new FileProviderForPeers();
                        byte[] bytes = fileProviderForPeers.ReadSpecificPositionOfFile("test", Integer.parseInt(parts[1]));
                        out.write(bytes);
                        out.flush();
                    }
                    else {
                        FileProviderForPeers fileProviderForPeers = new FileProviderForPeers();
                        fileProviderForPeers.writeSpecificPositionOfFile("test", 30,inputLine.getBytes() ,true, FileProviderForPeers.DOWNLOAD_UNIT);
                    }
                    //out.println(inputLine);
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                //TODO: Log exception to file
                e.printStackTrace();
            }
        }
    }
}
