package sample.services;

import sample.models.FileMetadata;
import sample.models.Peer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class responsible to manage the file downloading request
 * Manage the filenames, the available peers, start the file reading and consume the read bytes for other peers
 * Also can create sockets to reach available peers and download byte arrays and finally write them to the storage
 */
public class DownloadManager implements Runnable{

    private static Map<FileMetadata, List<Peer>> wannabeDownloads = new HashMap<>();

    public DownloadManager() {}

    @Override
    public void run() {
        while (wannabeDownloads.size() != 0){
            
        }
    }

    public void startNewSocketForDownload(){

    }

    public void processNewReadingRequest(){
        
    }
}
