package server.requests;

import java.io.Serializable;
import java.net.SocketAddress;

public class Request implements Serializable {
    private final String message;
    private final SocketAddress clientAddress;
    private final Object data;

    private String login, password;

    public Request(String message) {
        this(message, null, null);
    }
    public Request(String message, SocketAddress clientAddress) {
        this(message, clientAddress, null);
    }

    public Request(String message, SocketAddress clientAddress, Object data) {
        this(message, clientAddress, data, "", "");
    }

    public Request(String message, SocketAddress clientAddress, String login, String password) {
        this(message, clientAddress, null, login, password);
    }

    public Request(String message, SocketAddress clientAddress, Object data, String login, String password) {
        this.message = message;
        this.clientAddress = clientAddress;
        this.data = data;
        this.login = login;
        this.password = password;
    }


    public String getMessage() {
        return message;
    }

    public SocketAddress getClientAddress() {
        return clientAddress;
    }

    public String toString() {
        return "Request{message=\"" + message + "\", data=\"" + data + "\" clientAddress=\"" + clientAddress + "\"}";
    }

    public Object getData() {
        return data;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
