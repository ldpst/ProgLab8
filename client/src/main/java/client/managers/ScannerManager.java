package client.managers;

import org.jline.reader.LineReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс для единообразного использования потоков
 *
 * @author ldpst
 */
public class ScannerManager {
    private final LineReader lineReader;
    private final BufferedReader bufferedReader;

    public ScannerManager(InputStreamReader inputStreamReader) {
        this.bufferedReader = new BufferedReader(inputStreamReader);
        this.lineReader = null;
    }

    public ScannerManager(LineReader lineReader) {
        this.lineReader = lineReader;
        this.bufferedReader = null;
    }

    /**
     * Метод для считывания новой строки
     *
     * @return считанная строка
     */
    public String nextLine() {
        try {
            if (lineReader != null) {
                return lineReader.readLine();
            }
            if (bufferedReader != null) {
                return bufferedReader.readLine();
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
