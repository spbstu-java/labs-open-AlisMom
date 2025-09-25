package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class DictionaryLoader {

    public static Dictionary load(Path path) throws FileReadException, InvalidFileFormatException {
        Map<String, String> map = new LinkedHashMap<>();
        int maxWords = 1;

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                if (lineNo == 1) {
                    line = stripBOM(line);
                }
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("#")) {
                    continue;
                }

                int bar = raw.indexOf('|');
                if (bar < 0) {
                    throw new InvalidFileFormatException(
                        "Неверный формат словаря в строке " + lineNo + " (ожидался символ '|'): " + line);
                }

                String left = raw.substring(0, bar).trim();
                String right = raw.substring(bar + 1).trim();

                if (left.isEmpty() || right.isEmpty()) {
                    throw new InvalidFileFormatException(
                        "Неверный формат словаря в строке " + lineNo + " (пустая левая/правая часть).");
                }

                String normKey = normalizeKey(left);
                map.put(normKey, right);

                int words = left.trim().split("\\s+").length;
                if (words > maxWords) maxWords = words;
            }
        } catch (IOException ioe) {
            throw new FileReadException("Не удалось прочитать файл словаря: " + path + " (" + ioe.getMessage() + ")", ioe);
        } catch (SecurityException se) {
            throw new FileReadException("Нет доступа к файлу словаря: " + path, se);
        }

        if (map.isEmpty()) {
            throw new InvalidFileFormatException("Словарь пуст или не содержит валидных записей.");
        }
        return new Dictionary(map, maxWords);
    }

    static String normalizeKey(String s) {

        return s.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    private static String stripBOM(String s) {
        if (s != null && !s.isEmpty() && s.charAt(0) == '\uFEFF') {
            return s.substring(1);
        }
        return s;
    }
}
