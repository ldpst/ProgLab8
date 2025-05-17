package server.response;

import server.object.Movie;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingDeque;

public class Response implements Serializable {
    private final String message;
    private final ResponseType type;
    private final LinkedBlockingDeque<Movie> collection;
    private String[] translate = new String[4];
    private final int UID;

    public Response(String message, ResponseType type, int UID) {
        this(message, type, null, UID);
    }

    public Response(String message, ResponseType type, LinkedBlockingDeque<Movie> collection, int UID) {
        this.message = message;
        this.type = type;
        this.collection = collection;
        this.UID = UID;
    }

    public String getMessage() {
        return message;
    }

    public ResponseType getType() {
        return type;
    }

    public LinkedBlockingDeque<Movie> getCollection() {
        return collection;
    }

    public String[] getTranslate() {
        return translate;
    }

    public void setTranslate(String[] translate) {
        this.translate = translate;
    }

    public String toString() {
        return "Response{" + "message=\"" + message + '\"' + ", type=" + type + ", collection=" + collection + '}';
    }

    public int getUID() {
        return UID;
    }
}
