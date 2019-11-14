package com.filesharing.iot.models;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Peer extends User {
    private String ipAddress;
    private int port;
    private List<File> fileList;

    public void addFiles(List<File> files) {
        fileList.addAll(files);
    }


    public void removeFile(File file) {
        fileList = fileList.stream().filter(el ->
                !el.getName().equals(file.getName()) &&
                        !el.getSize().equals(file.getSize())).collect(Collectors.toList());
    }

    public void updateFile(String fileName , File file) {
        File f = fileList.stream()
                .filter(el -> el.getName().equals(fileName))
                .findFirst()
                .orElse(null);
        if(f != null){
            f.setName(file.getName());
            f.setExtension(file.getExtension());
            f.setMd5Sign(file.getMd5Sign());
            f.setSize(file.getSize());
        }
    }
}
