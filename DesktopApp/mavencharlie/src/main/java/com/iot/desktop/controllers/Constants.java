package com.iot.desktop.controllers;

import com.iot.desktop.models.Group;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public final class Constants {
    public static final String serverURL = "http://localhost:8080/";
    public static final String loginEndpoint = "login";
    public static final String notifyPeerIsOnlineEndpoint = "peers";
    public static String getGroupsForUserEndpoint = "getGroupsForUser";
    public static String localAddress = "";
    public static String emailAddress = "";
    public static List<Group>  userGroups = new ArrayList<>();


    static {
        try {
            localAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
