package com.iot.desktop.helpers;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * This class is responsible for consume reading and writing for specific positions in a file
 * In order to achieve this feature we use FileChannel class which is built in Java NIO package
 */
public class FileProviderForPeers {

    public static int DOWNLOAD_UNIT = 2048;

    public FileProviderForPeers(){}

    public byte[] ReadSpecificPositionOfFile(String fileName, int segment) {
        try (RandomAccessFile reader = new RandomAccessFile((System.getProperty("user.dir") + "/"+ fileName), "r");
             FileChannel channel = reader.getChannel()) {

            // get real file path
            long fileLength = channel.size();
            long position = 0;
            long size = 0;
            if (fileLength > DOWNLOAD_UNIT && segment * DOWNLOAD_UNIT < fileLength) {
                position = segment * DOWNLOAD_UNIT;
            }
            if (position + DOWNLOAD_UNIT < fileLength)
                size = DOWNLOAD_UNIT;
            else
                size = fileLength - position;

            MappedByteBuffer buff = channel.map(FileChannel.MapMode.READ_ONLY, position, size);
            if (buff.hasRemaining()) {
                byte[] data = new byte[buff.remaining()];
                buff.get(data);
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO: Pass the metadata also as parameter
    public  void writeSpecificPositionOfFile(String fileName, int segment, byte[] bytes) throws Exception{
        String defaultDir = FileSerializer.metaDatas.getOrDefault("defaultDir" , "");
        if(!defaultDir.equals("")){
            String fullPath = defaultDir + "/" + fileName + "/" + fileName + ".jpg"; //+ "." + fileMetadata.getExtension()
            try (RandomAccessFile writer = new RandomAccessFile(fullPath, "rw");
                 FileChannel channel = writer.getChannel()){
                ByteBuffer buff = ByteBuffer.wrap(bytes);

                channel.force(true);
                channel.write(buff, segment*DOWNLOAD_UNIT);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
