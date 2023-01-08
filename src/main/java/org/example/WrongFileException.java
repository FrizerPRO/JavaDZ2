package org.example;

public class WrongFileException extends Exception {
    public WrongFileException(String path) {
        super("Such path does not exist" + path);
    }
}
