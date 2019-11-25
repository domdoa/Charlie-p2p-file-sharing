package com.filesharing.iot.repository;

import com.filesharing.iot.models.File;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FileRepository {
    private List<File> files = new ArrayList<>();

    public void save(File file){
        Boolean duplicate = false;
        for (int i = 0; i < files.size(); i++) {
            if(files.get(i).getMd5Sign().equals(file.getMd5Sign())){
                duplicate = true;
            }
        }
        if(!duplicate){
            files.add(file);
        }
    }

    public List<File> getFiles(){return files;}

    public void remove(File file){
        files = files.stream()
                .filter(f -> f.getId() != file.getId())
                .collect(Collectors.toList());
    }

}