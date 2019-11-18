package helpers;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileSerializer {

    public static Map<String, String> metaDatas = new HashMap<>();

    public FileSerializer() {
        metaDatas.put("path", System.getProperty("user.dir") + "//metadata.ser");
        metaDatas.put("defaultDir", System.getProperty("user.dir")+ "/CharlieP2PDownloads");
        createDefaultDirectoryIfNotExists();
    }

    public void writeToFile() {
        try{
            FileOutputStream fileOut =  new FileOutputStream(metaDatas.get("path"));
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(metaDatas);
            out.close();
            fileOut.close();
        }catch (Exception e){
            //TODO: Log in file
            e.printStackTrace();
        }
    }

    public HashMap<String, String> readFromFile() {
        HashMap<String, String> map = null;
        try {
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "//metadata.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            map = (HashMap<String, String>) in.readObject();
            in.close();
            fileIn.close();
            return map;
        } catch (Exception i) {
            i.printStackTrace();
        }
        return map;
    }

    private void createDefaultDirectoryIfNotExists(){
        String directoryName = metaDatas.get("defaultDir");
        File directory = new File(directoryName);
        if (!directory.exists()){
            directory.mkdir();
        }
    }
}
