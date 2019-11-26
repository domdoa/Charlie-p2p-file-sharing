package com.iot.desktop.helpers;

import com.iot.desktop.controllers.RootController;
import com.iot.desktop.models.FileMetadata;
import com.iot.desktop.models.UploadFileModel;
import com.iot.desktop.network.ServerServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class FileSystemWatcher implements Runnable {

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final boolean recursive;
    private boolean trace = false;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    public FileSystemWatcher(Path dir, boolean recursive) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();
        this.recursive = recursive;

        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }

        // enable trace after initial registration
        this.trace = true;
    }

    /**
     * Process all events for keys queued to the watcher
     */
    public void processEvents() {
        for (; ; ) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);
                // TODO: Switch case for the kind and send the appropiate request
                if (ENTRY_CREATE.equals(kind)) {
                    boolean unique = true;
                    File uploaded = child.toFile();
                    for (int i=0; i< FileSerializer.downloadedFiles.size(); i++){
                        FileMetadata file =FileSerializer.downloadedFiles.get(i);
                        if ((file.getFileName()+"."+file.getExtension()).equals(name.toString())){
                            unique = false;
                            break;
                        }
                    }
                    String names = name.toString();
                    String[] nameExt = names.split("\\.");
                    if (unique && nameExt.length == 2){
                        FileMetadata fm = new FileMetadata(null, null,nameExt[0], nameExt[1], uploaded.length(),null);
                        FileSerializer.uploadedFiles.add(fm);
                        UploadFileModel ufm = new UploadFileModel(uploaded.getName(), Long.toString(uploaded.length()), new Date(System.currentTimeMillis()));
                        RootController.uploadedFiles.add(ufm);
                        // Necessary peer Id somehow get it
                        // TODO: compute MD5 signature
                        com.iot.desktop.dtos.File file = new com.iot.desktop.dtos.File(0,0, nameExt[0], nameExt[1], null, Long.toString(uploaded.length()));
                        new ServerServiceImpl().addFilesToPeer(Collections.singletonList(file), 1);
                    }
                } else if (ENTRY_MODIFY.equals(kind)) {

                } else if (ENTRY_DELETE.equals(kind)) {

                }


                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        processEvents();
    }
}
