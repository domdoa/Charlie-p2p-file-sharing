package sample.helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {

    public static Map<String, String> metaDatas = new HashMap<>();

    public FileHandler() {
        metaDatas.put("path", System.getProperty("user.dir") + "//metadata.ser");
        // TODO: Remove it later
        metaDatas.put("cucc", "valami1");
        metaDatas.put("cucc1", "valami2");
        metaDatas.put("cucc2", "valami3");
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

}
