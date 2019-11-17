package com.filesharing.iot.repository;

import com.filesharing.iot.Chord.Constants;
import com.filesharing.iot.models.ForeignPC;
import com.filesharing.iot.utils.Utils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ForeignPcRepository {
    private List<ForeignPC> foreignPCS = new ArrayList<>();
    private String fileName = "foreignPC" + Constants.currentSpringPort +".txt";

    public void save(ForeignPC foreignPC) throws Exception {
        ForeignPC james = foreignPCS.stream()
                .filter(p -> p.getInetSocketAddress().getAddress().toString().equals(foreignPC.getInetSocketAddress().getAddress().toString())
                        && p.getInetSocketAddress().getPort() == foreignPC.getInetSocketAddress().getPort())
                .findAny()
                .orElse(null);
        if (foreignPC != null && james == null && foreignPC.getInetSocketAddress() != null){
            foreignPCS.add(foreignPC);
            Utils.writeToFile(fileName, foreignPCS);
        }
    }

    public List<ForeignPC> getForeignPCS() throws Exception {
        foreignPCS = Utils.readFromFile(fileName);
        return foreignPCS;
    }

    public void remove(ForeignPC foreignPC) throws Exception {
        foreignPCS = foreignPCS.stream()
                .filter(p -> !p.getInetSocketAddress().getAddress().toString().equals(foreignPC.getInetSocketAddress().getAddress().toString())
                        && !(p.getInetSocketAddress().getPort() == foreignPC.getInetSocketAddress().getPort()))
                .collect(Collectors.toList());
        Utils.writeToFile(fileName, foreignPCS);
    }

    public String getFileName() {
        return fileName;
    }
}