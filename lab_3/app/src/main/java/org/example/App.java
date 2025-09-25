package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) {
        String dictPath = args.length > 0 ? args[0] : "dictionary.txt";
        try {
            Dictionary dict = DictionaryLoader.load(Path.of(dictPath));
            Translator translator = new Translator(dict);

            System.out.println("Введите текст для перевода (Ctrl+D — завершить):");
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String out = translator.translateLine(line);
                    System.out.println(out);
                }
            } catch (IOException ioe) {
                System.err.println("Ошибка чтения из stdin: " + ioe.getMessage());
                System.exit(2);
            }
        } catch (FileReadException | InvalidFileFormatException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
