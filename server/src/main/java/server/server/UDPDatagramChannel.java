package server.server;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.ConfigManager;
import server.utils.Pair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPDatagramChannel {
    private final Logger logger = LogManager.getLogger(UDPDatagramChannel.class);

    private final DatagramChannel channel;

    public UDPDatagramChannel() throws IOException {
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(ConfigManager.getAddress(), ConfigManager.getPort()));
    }

    public DatagramChannel getChannel() {
        return channel;
    }

    public void sendData(byte[] data, SocketAddress clientAddress) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        logger.info("Сервер отправляет ответ...");
        channel.send(buffer, clientAddress);
        logger.debug("Сервер отправил пакет: {}", SerializationUtils.deserialize(data).toString());
    }

    public Pair<byte[], SocketAddress> getData() throws IOException {
        logger.info("Сервер ожидает пакет...");

        ByteBuffer buffer = ByteBuffer.allocate(ConfigManager.getPacketSize());
        SocketAddress clientAddress = channel.receive(buffer);
        logger.debug("Клиент с адресом {} подключился", clientAddress);

        if (clientAddress != null) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            logger.debug("Сервер получил пакет: {} ", SerializationUtils.deserialize(data).toString());
            return new Pair<>(data, clientAddress);
        }
        else return null;
    }
}
