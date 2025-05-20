package server.response;

import java.io.Serializable;

public enum ResponseType implements Serializable {
    PRINT_MESSAGE,
    ERROR,
    COLLECTION,
    NEXT_STEP,
    IGNORE,
    EXIT

}
