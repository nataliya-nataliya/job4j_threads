package ru.job4j.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Predicate;

public class ReaderParse {
    private final File file;

    public ReaderParse(File file) {
        this.file = file;
    }

    private static Predicate<Character> content() {
        return fileCharacter -> fileCharacter > 0;
    }

    private static Predicate<Character> contentWithoutUnicode() {
        return fileCharacter -> fileCharacter < 0x80;
    }

    public String getContent(Predicate<Character> filter) {
        try (BufferedInputStream i = new BufferedInputStream(new FileInputStream(file))) {
            StringBuilder output = new StringBuilder();
            int data;
            while ((data = i.read()) > 0) {
                if (filter.test((char) data)) {
                    output.append((char) data);
                }
            }
            return output.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
