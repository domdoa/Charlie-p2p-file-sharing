package com.iot.desktop.services;

import com.iot.desktop.helpers.FileProviderForPeers;
import com.iot.desktop.helpers.FileSerializer;
import com.iot.desktop.models.FileMetadata;
import com.iot.desktop.models.Peer;
import com.iot.desktop.network.PeerSocket;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * This class responsible to manage the file downloading request
 * Manage the filenames, the available peers, start the file reading and consume the read bytes for other peers
 * Also can create sockets to reach available peers and download byte arrays and finally write them to the storage
 */
public class DownloadManager extends Thread{

    private FileMetadata fileMetadata;
    private List<Peer> availablePeers;
    private int officialSegmentSize;
    private List<Integer> remainingSegments;
    private List<PeerSocket> sockets;
    private static final Object LOCK = new Object();

    public DownloadManager(FileMetadata file, List<Peer> peers) {
        this.fileMetadata = file;
        this.availablePeers = peers;
        this.remainingSegments = new ArrayList<>();
        this.sockets = new ArrayList<>();

        System.out.println("Download manager constructor called.");
        long bound = fileMetadata.getSize() % FileProviderForPeers.DOWNLOAD_UNIT == 0
                ? fileMetadata.getSize() / FileProviderForPeers.DOWNLOAD_UNIT
                : (fileMetadata.getSize() / FileProviderForPeers.DOWNLOAD_UNIT) +1;
        for(int i = 0; i < bound; i++)
            remainingSegments.add(i);
        officialSegmentSize = remainingSegments.size();
        System.out.println("For "+ fileMetadata.getFileName()+ " there are this many bounds: " + remainingSegments.size());

        createFileWithSubdirectoryAndAllocateSpace();
        for (Peer availablePeer : availablePeers){
            sockets.add(new PeerSocket(availablePeer.getIpAddress(), availablePeer.getPort(), this));
            System.out.println("Peer port: " + availablePeer.getPort());
        }
        System.out.println("Peer sockets created. Number of peer sockets: " + sockets.size());
        for (PeerSocket socket: sockets){
            socket.start();
            System.out.println("Socket started on:"+ socket.getClientSocket().getInetAddress() + " and port: " + socket.getClientSocket().getPort());
        }
    }

    @Override
    public void run() {
        while (remainingSegments.size() != 0){
            try{
                Thread.sleep(1000);
                long sumOfArrivedBytes = 0;
                synchronized (LOCK){
                    for (PeerSocket peerSocket: sockets){
                        sumOfArrivedBytes += peerSocket.getArrivedBytes();
                        peerSocket.setArrivedBytes(0);
                    }
                    System.out.println("Download speed: " + (double)(sumOfArrivedBytes / 1000) + "kB/s");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("All segments arrived. DownloadManager will be stopped.");
    }

    private void createFileWithSubdirectoryAndAllocateSpace(){
        String defaultDir = FileSerializer.metaDatas.getOrDefault("defaultDir" , "");
        if(!defaultDir.equals("")){
            try{
                File file = new File(defaultDir + "/" + fileMetadata.getFileName() + "/" + fileMetadata.getFileName() + "." + fileMetadata.getExtension());
                file.getParentFile().mkdir();
                boolean succeed = file.createNewFile();
                if(succeed){
                    RandomAccessFile raf = new RandomAccessFile(file, "rw");
                    raf.setLength(fileMetadata.getSize());
                    raf.close();
                    System.out.println("File created! Absolute path: " + file.getAbsolutePath());
                    System.out.println("Actual file size and expected size: " + file.length() +"\t" +fileMetadata.getSize());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public int getOfficialSegmentSize() {
        return officialSegmentSize;
    }

    public FileMetadata getFileMetadata() {
        return fileMetadata;
    }

    public List<Integer> getRemainingSegments() {
        return remainingSegments;
    }
}