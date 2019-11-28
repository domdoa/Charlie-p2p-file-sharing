package com.iot.desktop.network;

import com.iot.desktop.helpers.FileProviderForPeers;
import com.iot.desktop.services.DownloadManager;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class PeerSocket extends Thread {

    public Socket getClientSocket() {
        return clientSocket;
    }

    private Socket clientSocket;
    private PrintWriter out;
    private DataInputStream in;
    private String message;
    private DownloadManager downloadManager;
    private long arrivedBytes;
    private static final Object LOCK = new Object();

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
        int newSegmentIndex;
        while ((newSegmentIndex = getNewRandomSegmentIndex()) != Integer.MIN_VALUE ){
            String  filenameWithExt = downloadManager.getFileMetadata().getName()+ "." +downloadManager.getFileMetadata().getExt();
            int newSegment = downloadManager.getRemainingSegments().get(newSegmentIndex);
            message = "DOWNLOAD " + filenameWithExt+ " " + newSegment;
            //System.out.println("Message send by peersocket: " + message);
            try{
                byte[] result = sendMessage(message);
                if(result != null){
                    removeReceivedSegmentSafely(newSegmentIndex);
                    new FileProviderForPeers().writeSpecificPositionOfFile(
                            downloadManager.getFileMetadata().getName(),downloadManager.getFileMetadata().getExt(), newSegment, result);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            message = "";
        }
        stopConnection();
        System.out.println("Peer socket stopped.");
        System.out.println("Number of remaining segments: " + this.downloadManager.getRemainingSegments().size());
    }

    public byte[] sendMessage(String msg) throws Exception {
        out.println(msg);
        int length = in.readInt();
        byte[] result = null;
        if(length > 0){
            result = new byte[length];
            in.readFully(result);
            arrivedBytes += result.length;
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

    private Integer getNewRandomSegmentIndex() {
            if(downloadManager.getRemainingSegments().size() > 0)
                return ThreadLocalRandom.current().nextInt(downloadManager.getRemainingSegments().size() /*-1*/);
            else
                return Integer.MIN_VALUE;
    }

    private synchronized void removeReceivedSegmentSafely(int segmentIndex){
        synchronized (LOCK){
            downloadManager.getRemainingSegments().remove(segmentIndex);
        }
    }

    public long getArrivedBytes() {
        return arrivedBytes;
    }

    public void setArrivedBytes(long arrivedBytes) {
        this.arrivedBytes = arrivedBytes;
    }
}
