package com.filesharing.iot.utils;

import com.filesharing.iot.models.ForeignPC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<ForeignPC> readFromFile(String pathToFile) throws Exception {
        List<ForeignPC> foreignPCS = new ArrayList<>();
        File file = new File(pathToFile);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = br.readLine();
        while (st != null) {
            String[] split = st.split(":");
            ForeignPC foreignPC = new ForeignPC(new InetSocketAddress(split[0], Integer.parseInt(split[1])), split[2]);
            foreignPCS.add(foreignPC);
            st = br.readLine();
        }
        br.close();
        return foreignPCS;
    }


    public static void writeToFile(String pathToFile, List<ForeignPC> list) throws Exception {

        // attach a file to FileWriter
        FileWriter fw = new FileWriter(pathToFile);

        // read character wise from string and write
        // into FileWriter

        for (ForeignPC aList : list) {
            fw.write(aList.writeToFile());
            fw.write("\n");
        }

        //close the file
        fw.close();
    }
}
