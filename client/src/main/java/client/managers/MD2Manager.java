package client.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD2Manager {
    private final static MessageDigest md;
    private final static Logger logger = LogManager.getLogger(MD2Manager.class);

    static {
        try {
            md = MessageDigest.getInstance("MD2");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Ошибка при создании экземпляра MessageDigest", e);
            throw new RuntimeException(e);
        }
    }

    public static String getHash(String input) {
        byte[] data = md.digest(input.getBytes());
        StringBuilder res = new StringBuilder();
        for (byte b : data) {
            res.append(String.format("%02x", b));
        }
        return res.toString();
    }
}
