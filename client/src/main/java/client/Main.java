package client;

import client.client.UDPClient;
import client.managers.RunManager;
import client.managers.ScannerManager;
import client.utils.InputFormat;
import org.jline.reader.LineReaderBuilder;


public class Main {
    public static void main(String[] args) {
        try {

            RunManager runManager = new RunManager(new UDPClient(), new ScannerManager(LineReaderBuilder.builder().build()), InputFormat.CONSOLE);
            runManager.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
