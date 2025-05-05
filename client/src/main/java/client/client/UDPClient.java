package client.client;

import client.exceptions.ServerIsUnavailableException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.ConfigManager;
import server.requests.Request;
import server.response.Response;

import java.io.IOException;
import java.net.*;

public class UDPClient {
    private final int PACKET_SIZE = ConfigManager.getPacketSize();

    public static final Logger logger = LogManager.getLogger(UDPClient.class);

    public final DatagramSocket client;

    private String login, password;

    public UDPClient() throws IOException {
        logger.info("Запуск клиента...");
        client = new DatagramSocket();
        client.setSoTimeout(1000);
        logger.info("Клиент запущен");
    }

    public Response makeRequest(String request, Object object) throws ServerIsUnavailableException, IOException {
        int attempt = 0;
        while (attempt < ConfigManager.getAttemptMax()) {
            attempt++;
            try {
                sendRequest(new Request(request, new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), client.getLocalPort()), object));

                byte[] data = receiveData();
                return SerializationUtils.deserialize(data);
            } catch (SocketTimeoutException e) {
                logger.warn("Попытка {}: Таймаут ожидания ответа от сервера.", attempt);
            } catch (IOException e) {
                logger.error("Попытка {}: Ошибка при отправке запроса: {}", attempt, e.getMessage());
                throw e;
            }
        }
        logger.error("Сервер недоступен. Завершение программы");
        throw new ServerIsUnavailableException();
    }

    public Response makeRequest(String request, String login, String password) throws ServerIsUnavailableException, IOException {
        int attempt = 0;
        while (attempt < ConfigManager.getAttemptMax()) {
            attempt++;
            try {
                sendRequest(new Request(request, new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), client.getLocalPort()), login, password));

                byte[] data = receiveData();
                return SerializationUtils.deserialize(data);
            } catch (SocketTimeoutException e) {
                logger.warn("Попытка {}: Таймаут ожидания ответа от сервера.", attempt);
            } catch (IOException e) {
                logger.error("Попытка {}: Ошибка при отправке запроса: {}", attempt, e.getMessage());
                throw e;
            }
        }
        logger.error("Сервер недоступен. Завершение программы");
        throw new ServerIsUnavailableException();
    }

    public Response makeRequest(String request, Object object, String login, String password) throws ServerIsUnavailableException, IOException {
        int attempt = 0;
        while (attempt < ConfigManager.getAttemptMax()) {
            attempt++;
            try {
                sendRequest(new Request(request, new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), client.getLocalPort()), object, login, password));

                byte[] data = receiveData();
                return SerializationUtils.deserialize(data);
            } catch (SocketTimeoutException e) {
                logger.warn("Попытка {}: Таймаут ожидания ответа от сервера.", attempt);
            } catch (IOException e) {
                logger.error("Попытка {}: Ошибка при отправке запроса: {}", attempt, e.getMessage());
                throw e;
            }
        }
        logger.error("Сервер недоступен. Завершение программы");
        throw new ServerIsUnavailableException();
    }

    private void sendRequest(Request request) throws IOException {
        logger.debug("Отправка запроса на сервер...");
        byte[] data = SerializationUtils.serialize(request);
        DatagramPacket dp = new DatagramPacket(data, data.length, new InetSocketAddress(ConfigManager.getAddress(), ConfigManager.getPort()));
        client.send(dp);
        logger.debug("Запрос отправлен: {}", SerializationUtils.deserialize(data).toString());
    }

    private byte[] receiveData() throws IOException {
        byte[] data = new byte[PACKET_SIZE];
        DatagramPacket dp1 = new DatagramPacket(data, data.length);
        logger.debug("Ожидание ответа от сервера...");
        client.receive(dp1);
        logger.debug("Ответ получен: {}", SerializationUtils.deserialize(data).toString());
        return data;
    }

    public void setLoginAndPassword(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
