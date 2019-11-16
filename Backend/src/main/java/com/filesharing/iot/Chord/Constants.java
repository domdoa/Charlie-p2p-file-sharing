package com.filesharing.iot.Chord;

public final class Constants {
    public static final String defaultSpringPort = "8080";
    public static final String currentSpringPort = System.getProperty("server.port") != null ? System.getProperty("server.port") : Constants.defaultSpringPort;
}
