package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileEntity {
    static String rootPath;
    ArrayList<FileEntity> subFileEntities;
    String path;
    String content;

    public FileEntity(String path) {
        this.path = path;
        content = getContentFromPath(path);
        subFileEntities = new ArrayList<>();
    }

    public void setSubFiles(ArrayList<FileEntity> subEntities) {
        subFileEntities = new ArrayList<>(subEntities);
    }

    private String getContentFromPath(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            System.out.println("Change filepath of check file.\n" +
                    "(File Error)");
        }
        return "";
    }

    public ArrayList<FileEntity> getSubFolders() {
        ArrayList<FileEntity> entities = new ArrayList<>();
        for (String s : content.lines().toList()) {
            if (s.contains("require")) {
                String subPath = s.substring(s.indexOf('\'') + 1, s.lastIndexOf('\''));
                entities.add(new FileEntity(rootPath + "/" + subPath));
            }
        }
        return entities;
    }

    public Boolean checkForRepeats() {
        for (var folder : subFileEntities) {
            if (!folder.checkForRepeatsRecursive(this)) {
                return false;
            }
        }
        return true;
    }

    private Boolean checkForRepeatsRecursive(FileEntity entity) {
        if (this.equals(entity)) {
            return false;
        }
        for (var folder : subFileEntities) {
            if (!folder.checkForRepeatsRecursive(entity)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileEntity fileEntity) {
            return fileEntity.path.equals(path);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (var file :
                subFileEntities) {
            result.append(file.toString()).append("\n");
        }
        result.append(content);
        return result.toString();
    }
}
