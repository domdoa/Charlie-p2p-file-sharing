package com.iot.desktop.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Constants {
    public static final String serverURL = "http://localhost:8080/";
    public static final String loginEndpoint = "login";
    public static final String notifyPeerIsOnline = "peers";
    public static String localAddress="";

    static {
        try {
            localAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
