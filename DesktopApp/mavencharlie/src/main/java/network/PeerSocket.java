package network;

import helpers.FileProviderForPeers;
import services.DownloadManager;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class PeerSocket extends Thread {

    private Socket clientSocket;
    private PrintWriter out;
    private DataInputStream in;
    private String message;
    private DownloadManager downloadManager;

    public PeerSocket(String ip, int port, DownloadManager manager) {
        try{
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new DataInputStream(clientSocket.getInputStream());
            downloadManager = manager;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int newSegment;
        while ((newSegment = getNewRandomSegment()) != Integer.MIN_VALUE ){
            String  filename = downloadManager.getFileMetadata().getFileName();
            message = "DOWNLOAD " + filename+ " " + newSegment;
            System.out.println("Message send by peersocket: " + message);
            try{
                byte[] result = sendMessage(message);
                if(result != null){
                    removeReceivedSegmentSafely(newSegment);
                    new FileProviderForPeers().writeSpecificPositionOfFile(filename, newSegment, result);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            message = "";
        }
        stopConnection();
        System.out.println("Peer socket stopped.");
    }

    public byte[] sendMessage(String msg) throws Exception {
        out.write(msg);
        int length = in.readInt();
        byte[] result = null;
        if(length > 0){
            result = new byte[2048];
            in.readFully(result);
        }
        return result;
    }

    public void stopConnection(){
        try{
            in.close();
            out.close();
            clientSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Integer getNewRandomSegment() {
        if(downloadManager.getOfficialSegmentSize() != 0)
            return ThreadLocalRandom.current().nextInt(downloadManager.getOfficialSegmentSize());
        else
            return Integer.MIN_VALUE;
    }

    private synchronized void removeReceivedSegmentSafely(int segment){
        downloadManager.getRemainingSegments().remove(segment);
    }

}
