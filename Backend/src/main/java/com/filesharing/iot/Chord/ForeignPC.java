package com.filesharing.iot.Chord;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Objects;

public class ForeignPC implements Serializable {
    InetSocketAddress inetSocketAddress;
    String springPort;

    public ForeignPC(InetSocketAddress inetSocketAddress, String springPort) {
        this.inetSocketAddress = inetSocketAddress;
        this.springPort = springPort;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public void setInetSocketAddress(InetSocketAddress inetSocketAddress) {
        this.inetSocketAddress = inetSocketAddress;
    }

    public String getSpringPort() {
        return springPort;
    }

    public void setSpringPort(String springPort) {
        this.springPort = springPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForeignPC foreignPC = (ForeignPC) o;
        return Objects.equals(inetSocketAddress, foreignPC.inetSocketAddress);
    }

    @Override
    public int hashCode() {

        return Objects.hash(inetSocketAddress, springPort);
    }

    @Override
    public String toString() {
        return "socketAddress=" + inetSocketAddress +
                ", springPort=" + springPort;
    }
}
