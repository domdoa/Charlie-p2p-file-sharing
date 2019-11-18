package com.iot.desktop.network;

import com.iot.desktop.helpers.FileProviderForPeers;
import com.iot.desktop.helpers.PublicIPAddressResolver;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer implements Runnable /*extends Thread*/ {

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
            serverSocket = new ServerSocket(port/*, 0, InetAddress.getLocalHost()*/);
            //System.out.println("FileServer InetAddress: " + serverSocket.getInetAddress().getHostAddress());
            //System.out.println("FileServer LocalSocketAddress: " + serverSocket.getLocalSocketAddress());
            System.out.println("Local address: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("FileServer listening on the port: "+ serverSocket.getLocalPort());
            //System.out.println("Public IP address: "+ PublicIPAddressResolver.GetPublicIPAddress());
            // Notify the backend that this peer become available
            //new ServerConnection().notifyActualPeerIsOnline(InetAddress.getLocalHost().getHostAddress(),serverSocket.getLocalPort());

            while (true){
                new EchoClientHandler(serverSocket.accept()).start();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            stopThread();
            System.out.println("File server stopped.");
        }
    }

    public void stopThread() {
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
        private DataOutputStream out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new DataOutputStream(clientSocket.getOutputStream());
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                System.out.println("Connected socket: "+ clientSocket.getInetAddress() +"\t" + clientSocket.getPort());
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("FileServer input message: " + inputLine);
                    if (inputLine.contains("DOWNLOAD")) {
                        String[] parts = inputLine.split(" ");
                        // TODO: send byte array
                        FileProviderForPeers fileProviderForPeers = new FileProviderForPeers();
                        byte[] bytes = fileProviderForPeers.ReadSpecificPositionOfFile(parts[1], Integer.parseInt(parts[2]));
                        out.writeInt(bytes.length); // write length of the message
                        out.write(bytes);   // write the message
                        out.flush();
                    }
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
