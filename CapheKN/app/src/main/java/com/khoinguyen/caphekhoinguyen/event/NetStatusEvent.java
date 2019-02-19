package com.khoinguyen.caphekhoinguyen.event;

public class NetStatusEvent {
    private boolean isConnect;

    public NetStatusEvent() {
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }
}
