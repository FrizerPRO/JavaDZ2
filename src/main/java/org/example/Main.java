package org.example;

import java.io.*;

public class Main {
    static void startGame() {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            do {
                System.out.println("Enter root folder path:");

                String path;
                try {
                    path = reader.readLine();
                } catch (IOException exception) {
                    System.out.println("Неверный ввод строки");
                    continue;
                }
                try {
                    var explorer = new Explorer(path);
                    System.out.println(explorer);
                    File f = new File(path + "/output.txt");
                    if (!f.exists()) {
                        f.createNewFile();
                    }
                    FileWriter writer = new FileWriter(path + "/output.txt");
                    writer.write(explorer.toString());
                    writer.close();
                } catch (WrongFileException exception) {
                    System.out.println(exception.toString());
                }
                System.out.println("Введите 1 чтобы воспроизвести программу еще 1 раз!");
            } while (reader.readLine().equals("1"));
        } catch (IOException exception) {
            System.out.println("Неверный ввод строки");
        }
    }

    public static void main(String[] args) {
        startGame();
    }
}