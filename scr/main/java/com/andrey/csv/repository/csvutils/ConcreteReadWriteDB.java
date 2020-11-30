package com.andrey.csv.repository.csvutils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ConcreteReadWriteDB implements ReadWriteDataBase {
    private char csvDelimiter = ';';
    List<String> projectHeaders;
    //получает значение в единственном методе getProperCSVFormatAndCheckHeaders()
    private boolean needToRewriteProjectHeaders;

    public ConcreteReadWriteDB(List<String> projectHeaders) {
        this.projectHeaders = projectHeaders;
    }


    /*
    Тут была попытка реализовать перезапись заголовков, если они вдруг были удалены или несоответствуют новым
    не несёт никакой особой функциональности, но данные в файле удаляются в методе createCSVPrinterForNewFile()
    из-за опции StandardOpenOption.TRUNCATE_EXISTING.
     */
    public boolean writeData(List<String> records, Path path) {
        try(CSVPrinter printer = getCSVPrinter(path, records)) {
            if (needToRewriteProjectHeaders) {
                List<String> rowsFromCsvFile = readData(path);
                records.addAll(0, rowsFromCsvFile);
            }
            for(String oneRecord: records) {
                printer.printRecord(oneRecord);
            }
            printer.flush();
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private CSVPrinter getCSVPrinter(Path path, List<String> records) throws IOException {
        File file = new File(path.toString());
        if (!file.exists() || records.size() != 1) {
            return createCSVPrinterForNewFile(path);
        }
        CSVFormat csvFormat = getProperCSVFormatAndCheckHeaders(path);
        if (needToRewriteProjectHeaders) return createCSVPrinterForNewFile(path);
        return createCSVPrinterForExistingFile(path, csvFormat);
    }

    private CSVFormat getProperCSVFormatAndCheckHeaders(Path path) {
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withDelimiter(csvDelimiter)
                .withQuote(null);
        if (!isHeadersCorrect(path)) {
            needToRewriteProjectHeaders = true;
        }
        return csvFormat;
    }

    private boolean isHeadersCorrect(Path path) {
        boolean result = true;
        try (CSVParser parser = getCSVParserWithHeaders(path)) {
            String headerRow = parser.getHeaderNames().stream()
                                .collect(Collectors.joining(String.valueOf(csvDelimiter)));
            List<String> headersFromCSVFile = Arrays.stream(
                    headerRow.split(String.valueOf(csvDelimiter)))
                    .collect(Collectors.toList());
            if (headersFromCSVFile.size() == 0) {
                return false;
            }

            for (String headerFromFile: headersFromCSVFile) {
                if (!projectHeaders.contains(headerFromFile))
                        result = false;
            }
        } catch (Exception e) {
            System.out.println("Будет создан новый файл для записи данных");
            result = false;
        }
        return result;
    }

    private CSVPrinter createCSVPrinterForExistingFile(Path path, CSVFormat csvFormat) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        return new CSVPrinter(writer, csvFormat);
    }

    private CSVPrinter createCSVPrinterForNewFile(Path path) throws IOException {
        String headers = String.join(";", projectHeaders);
        BufferedWriter writer = Files.newBufferedWriter(path,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
        return new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader(headers)
                .withDelimiter(csvDelimiter).withQuote(null));
    }

    public List<String> readData(Path path) {
        List<String> rowsFromCsvFile = new ArrayList<>();
        try (CSVParser parser = getCSVParser(path)){
            StringBuilder oneRow = new StringBuilder();
                for (CSVRecord record : parser.getRecords()) {
                    for (int i = 0; i < projectHeaders.size(); i++) {
                        oneRow.append(record.get(i)).append(csvDelimiter);
                    }
                    rowsFromCsvFile.add(oneRow.toString());
                    oneRow.setLength(0);
                }
        } catch (IOException e) {
            System.out.println("Будет создана новая БД для " + path.getName(0));
        }
        return rowsFromCsvFile;
    }

    private CSVParser getCSVParserWithHeaders(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path);
        return new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(csvDelimiter).withFirstRecordAsHeader());
    }

    private CSVParser getCSVParser(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path);
        return new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(csvDelimiter));
    }

    public long incrementIDForNewObject(Path sourceFile) {
        List<String> rows = readData(sourceFile);
        Optional<Long> incrementedID = rows.stream()
                .map(row -> row.split(String.valueOf(csvDelimiter)))
                .skip(1)//skip header
                .map(array -> array[0])
                .map(Long::parseLong)
                .max(Long::compareTo);
        return incrementedID.map(id -> id + 1L).orElse(1L);
    }
}
