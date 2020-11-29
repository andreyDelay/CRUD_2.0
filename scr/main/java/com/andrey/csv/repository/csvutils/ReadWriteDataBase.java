package com.andrey.csv.repository.csvutils;

import java.nio.file.Path;
import java.util.List;

public interface ReadWriteDataBase {

    boolean writeData(List<String> data, Path path);

    List<String> readData(Path path);

    long incrementIDForNewObject(Path path);
}
