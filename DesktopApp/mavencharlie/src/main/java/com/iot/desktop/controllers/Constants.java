package com.iot.desktop.controllers;

import com.iot.desktop.models.ForeignPC;
import com.iot.desktop.models.Group;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public final class Constants {
    public static final String serverURL = "http://localhost:8080/";
    public static final String loginEndpoint = "login";
    public static final String notifyPeerIsOnlineEndpoint = "peers";
    public static final String notifyPeerIsOfflineEndpoint = "peers";
    public static final String addFileToUserEndpoint = "peers/files";
    public static final String removeFileFromUserEndpoint = "peers/files";
    public static String getGroupsForUserEndpoint = "getGroupsForUser";
    public static String localAddress = "";
    public static String emailAddress = "";
    public static String groupNameOfTheUser = "";
    public static List<Group> userGroups = new ArrayList<>();
    public static String currentDirectory = System.getProperty("user.dir");
    public static String charlieP2PFolder = "/CharlieP2PDownloads";
    public static String JWTToken = "";
    public static final int defaultSpringPort = 8080;
    public static final int peerSpringPort = System.getProperty("server.port") != null ? Integer.parseInt(System.getProperty("server.port")) : Constants.defaultSpringPort;
    public static List<ForeignPC> allSuperPeers = new ArrayList<>();


    static {
        try {
            localAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
