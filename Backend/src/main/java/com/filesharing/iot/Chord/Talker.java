package com.filesharing.iot.Chord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Talker thread that processes request accepted by listener and writes
 * response to socket.
 *
 * @author Chuan Xia
 */

public class Talker implements Runnable {

    Socket talkSocket;
    private Node local;
    private static Logger logger = LoggerFactory.getLogger(Talker.class);

    public Talker(Socket _talkSocket, Node _local) {
        talkSocket = _talkSocket;
        local = _local;
    }

    public void run() {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = talkSocket.getInputStream();
            String request = Helper.inputStreamToString(input);
            String response = processRequest(request);
            if (response != null) {
                if (response.startsWith("SPRING")) {
                    //local.getLocalAddress().setSpringPort(response.split(" ")[1]);
                } else {
                    output = talkSocket.getOutputStream();
                    output.write(response.getBytes());
                }

            }
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot talk.\nServer port: " + local.getLocalAddress().getInetSocketAddress().getPort() + "; Talker port: " + talkSocket.getPort(), e);
        }
    }

    private String processRequest(String request) {
        ForeignPC result = null;
        String ret = null;
        if (request == null) {
            return null;
        }
        //logger.info("Process request : " + request);
        if (request.startsWith("CLOSEST")) {
            long id = Long.parseLong(request.split("_")[1]);
            result = local.closest_preceding_finger(id);
            String ip = result.getInetSocketAddress().getAddress().toString();
            int port = result.getInetSocketAddress().getPort();
            ret = "MYCLOSEST_" + ip + ":" + port;
            //logger.info("Process request : " + request + " Return: " + ret);
        } else if (request.startsWith("YOURSUCC")) {
            result = local.getSuccessor();
            if (result != null) {
                String ip = result.getInetSocketAddress().getAddress().toString();
                int port = result.getInetSocketAddress().getPort();
                ret = "MYSUCC_" + ip + ":" + port;
                //logger.info("Process request : " + request + " Return: " + ret);
            } else {
                ret = "NOTHING";
            }
        } else if (request.startsWith("YOURPRE")) {
            result = local.getPredecessor();
            if (result != null) {
                String ip = result.getInetSocketAddress().getAddress().toString();
                int port = result.getInetSocketAddress().getPort();
                ret = "MYPRE_" + ip + ":" + port;
                //logger.info("Process request : " + request + " Return: " + ret);
            } else {
                ret = "NOTHING";
            }
        } else if (request.startsWith("FINDSUCC")) {
            long id = Long.parseLong(request.split("_")[1]);
            result = local.find_successor(id);
            String ip = result.getInetSocketAddress().getAddress().toString();
            int port = result.getInetSocketAddress().getPort();
            ret = "FOUNDSUCC_" + ip + ":" + port + ":" + result.getSpringPort();
            //logger.info("Process request : " + request + " Return: " + ret);
        } else if (request.startsWith("IAMPRE")) {
            String springPort = request.split(":")[2].split(" ")[1];
            ForeignPC new_pre = new ForeignPC(Helper.createSocketAddress(request.split("_")[1]), springPort);
            local.notified(new_pre);
            ret = "NOTIFIED";
        } else if (request.startsWith("KEEP")) {
            ret = "ALIVE";
        }
//        else if (request.startsWith("SPRING")) {
//            ret = request;
//        }
        return ret;
    }
}