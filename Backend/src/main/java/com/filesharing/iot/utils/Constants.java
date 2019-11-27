package com.filesharing.iot.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Constants {
    public static final String defaultSpringPort = "8080";
    public static final String currentSpringPort = System.getProperty("server.port") != null ? System.getProperty("server.port") : Constants.defaultSpringPort;
    public static String localAddress="";

    static {
        try {
            localAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
