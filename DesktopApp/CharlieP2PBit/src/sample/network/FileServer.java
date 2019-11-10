package sample.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer implements Runnable {

    private ServerSocket serverSocket;

    @Override
    public void run() {
        try{
            start(0);   // this start a new serversocket on a randomly allocated port
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start(int port) throws Exception {
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Server listening on the port: "+ serverSocket.getLocalPort());
            // TODO: Notify the backend that this peer become available
            new ServerConnection().notifyActualPeerIsOnline(serverSocket.getLocalPort());

            while (true)
                new EchoClientHandler(serverSocket.accept()).start();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    public void stop() throws Exception {
        try{
            serverSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
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
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    out.println(inputLine);
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
