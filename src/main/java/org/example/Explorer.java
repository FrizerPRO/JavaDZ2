package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Explorer {
    String rootPath;

    ArrayList<FileEntity> allFileEntities;

    ArrayList<FileEntity> allUniqueEntities;
    private Boolean hasRepeats;

    public Explorer(String rootPath) throws WrongFileException {
        this.rootPath = rootPath;
        FileEntity.rootPath = rootPath;
        allFileEntities = getAllFilesFromRoot(rootPath);
        allUniqueEntities = new ArrayList<>(allFileEntities);
        setSubFilesForAll();
        hasRepeats = !checkForRepeats();
    }

    public ArrayList<FileEntity> getAllFilesFromRoot(String rootPath) {
        List<Path> pathes = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(Paths.get(rootPath))) {
            pathes = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Such directory does not exist");
        }
        var result = new ArrayList<FileEntity>();
        for (var path :
                pathes) {
            result.add(new FileEntity(path.toString()));
        }
        return result;
    }

    public Boolean checkForRepeats() {
        for (var file :
                allFileEntities) {
            if (!file.checkForRepeats()) {
                return false;
            }
        }
        return true;
    }

    public void setSubFilesForAll() throws WrongFileException {
        for (var file : allFileEntities) {
            setSubFile(file);
        }
    }

    private void setSubFile(FileEntity fileEntity) throws WrongFileException {
        var allSubFiles = new ArrayList<FileEntity>();
        for (var subFile :
                fileEntity.getSubFolders()) {
            try {
                var templeFile = allFileEntities.get(allFileEntities.indexOf(subFile));
                allSubFiles.add(allFileEntities.get(allFileEntities.indexOf(subFile)));
                allUniqueEntities.remove(templeFile);
            } catch (IndexOutOfBoundsException e) {
                throw new WrongFileException(subFile.path);
            }
        }
        fileEntity.setSubFiles(allSubFiles);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (hasRepeats) {
            result.append("Has Repeats");
            return result.toString();
        }

        for (var file :
                allUniqueEntities) {
            result.append(file.toString()).append("\n");
        }
        if (result.toString().contains("Has Repeats")) {
            return "Has Repeats";
        }
        return result.toString();
    }
}
