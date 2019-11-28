package com.iot.desktop.services;

import com.iot.desktop.controllers.RootController;
import com.iot.desktop.helpers.FileProviderForPeers;
import com.iot.desktop.helpers.FileSerializer;
import com.iot.desktop.main.Root;
import com.iot.desktop.models.DownloadFileModel;
import com.iot.desktop.models.FileMetadata;
import com.iot.desktop.models.Peer;
import com.iot.desktop.network.PeerSocket;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class responsible to manage the file downloading request
 * Manage the filenames, the available peers, start the file reading and consume the read bytes for other peers
 * Also can create sockets to reach available peers and download byte arrays and finally write them to the storage
 */
public class DownloadManager extends Thread{

    private com.iot.desktop.dtos.File fileMetadata;
    private List<Peer> availablePeers;
    private int officialSegmentSize;
    private List<Integer> remainingSegments;
    private List<PeerSocket> sockets;
    private static final Object LOCK = new Object();

    public DownloadManager(com.iot.desktop.dtos.File file, List<Peer> peers) {
        this.fileMetadata = file;
        this.availablePeers = peers;
        this.remainingSegments = new ArrayList<>();
        this.sockets = new ArrayList<>();

        DownloadFileModel dm = new DownloadFileModel(file.getName(), file.getSize(),"0%" ," - " );
        if(FileSerializer.downloadedFiles.size() == 0){
            FileSerializer.downloadedFiles.add(file);
            RootController.downloadedFiles.add(dm);
        }
        for (int i = 0; i< FileSerializer.downloadedFiles.size(); i++){
            if(FileSerializer.downloadedFiles.get(i).getName().equals(dm.getFileName())){
                FileSerializer.downloadedFiles.remove(file);
                RootController.downloadedFiles.removeIf( (fi)  -> fi.getFileName().equals(dm.getFileName()));
                FileSerializer.downloadedFiles.add(file);
                RootController.downloadedFiles.add(dm);
            }
            else if(i == FileSerializer.downloadedFiles.size()){
                FileSerializer.downloadedFiles.add(file);
                RootController.downloadedFiles.add(dm);
            }
        }

        System.out.println("Download manager constructor called.");
        long bound = fileMetadata.getSize() % FileProviderForPeers.DOWNLOAD_UNIT == 0
                ? fileMetadata.getSize() / FileProviderForPeers.DOWNLOAD_UNIT
                : (fileMetadata.getSize() / FileProviderForPeers.DOWNLOAD_UNIT) +1;
        for(int i = 0; i < bound; i++)
            remainingSegments.add(i);
        officialSegmentSize = remainingSegments.size();
        System.out.println("For "+ fileMetadata.getName()+ " there are this many bounds: " + remainingSegments.size());

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
        long start = System.currentTimeMillis();
        long arrivedBytesForProgress = 0;
        while (remainingSegments.size() != 0){
            try{
                Thread.sleep(1000);
                long arrivedBytesForSpeed = 0;
                synchronized (LOCK){
                    for (PeerSocket peerSocket: sockets){
                        arrivedBytesForSpeed += peerSocket.getArrivedBytes();
                        arrivedBytesForProgress += peerSocket.getArrivedBytes();
                        peerSocket.setArrivedBytes(0);
                    }
                    double speed = (double) arrivedBytesForSpeed / 1000;
                    double progress = (double) arrivedBytesForProgress / fileMetadata.getSize() * 100;
                    System.out.println("Download speed: " + speed + "kB/s");
                    for (int i = 0; i < RootController.downloadedFiles.size(); i++){
                        if (RootController.downloadedFiles.get(i).getFileName().equals(fileMetadata.getName())){
                            DownloadFileModel dm = RootController.downloadedFiles.get(i);
                            RootController.downloadedFiles.remove(i);
                            dm.setProgress(String.format("%.2f",progress) + "%");
                            if(dm.getProgress().equals("100.00%")){
                                dm.setSpeed(" - ");
                            }else{
                                dm.setSpeed(String.format("%.2f", speed) + " kB/s");
                            }
                            RootController.downloadedFiles.add(i, dm);
                            break;
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("All segments arrived. DownloadManager will be stopped.");
        System.out.println("Download time: " + (System.currentTimeMillis()-start) + "ms");

    }

    private void createFileWithSubdirectoryAndAllocateSpace(){
        String defaultDir = FileSerializer.metaDatas.getOrDefault("defaultDir" , "");
        if(!defaultDir.equals("")){
            try{
                File file = new File(defaultDir + "/" + fileMetadata.getName() + "/" + fileMetadata.getName() + "." + fileMetadata.getExt());
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

    public com.iot.desktop.dtos.File getFileMetadata() {
        return fileMetadata;
    }

    public List<Integer> getRemainingSegments() {
        return remainingSegments;
    }
}
