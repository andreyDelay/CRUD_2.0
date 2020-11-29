package com.andrey.csv.repository.csv;

import com.andrey.csv.model.Speciality;
import com.andrey.csv.model.visitor.Visitor;
import com.andrey.csv.repository.SpecialityRepository;
import com.andrey.csv.repository.csvutils.ConcreteReadWriteDB;
import com.andrey.csv.repository.csvutils.ReadWriteDataBase;
import com.andrey.csv.repository.csvutils.headers.SpecialityHeaders;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CSVSpecialityRepository implements SpecialityRepository {
    private Path pathToDBFile = Paths.get("scr\\main\\java\\com\\andrey\\csv\\resources\\specialities.csv");
    private String csvDelimiter = ";";
    private Visitor visitor;
    private ReadWriteDataBase readWriteDataBase;

    public CSVSpecialityRepository(Visitor visitor) {
        this.visitor = visitor;
        List<String> projectHeaders = Arrays.stream(SpecialityHeaders.values())
                .map(Enum::toString)
                .collect(Collectors.toList());
        readWriteDataBase = new ConcreteReadWriteDB(projectHeaders);
    }

    @Override
    public Speciality save(Speciality newSpeciality) {
        List<String> adaptedObjects = new ArrayList<>();
        newSpeciality.setId(readWriteDataBase.incrementIDForNewObject(pathToDBFile));
        adaptedObjects.add(visitor.prepareDataForCSVPrinter(newSpeciality));
        readWriteDataBase.writeData(adaptedObjects, pathToDBFile);
        return newSpeciality;
    }

    @Override
    public Speciality update(Long id, Speciality newValue) {
        List<Speciality> allSpecialities = findAll();
        Speciality oldValue = isExistInDataBaseList(allSpecialities, id);
        if (oldValue != null) {
            allSpecialities.set(allSpecialities.indexOf(oldValue), newValue);
            oldValue = newValue;
        }
        return oldValue;
    }

    @Override
    public Speciality delete(Long id) {
        List<Speciality> allSpecialities = findAll();
        Speciality speciality = isExistInDataBaseList(allSpecialities, id);
        if (speciality != null) {
            allSpecialities.remove(speciality);
            List<String> dataForDB = allSpecialities.stream()
                    .map(visitor::prepareDataForCSVPrinter)
                    .collect(Collectors.toList());
            readWriteDataBase.writeData(dataForDB, pathToDBFile);
        }
        return speciality;
    }

    @Override
    public Optional<Speciality> find(Long requiredID) {
        String requiredIdAsString = String.valueOf(requiredID);
        Optional<String[]> result = readWriteDataBase.readData(pathToDBFile).stream()
                .map(row -> row.split(csvDelimiter))
                .filter(array -> array[0].length() != 0)
                .filter(array -> array[0].equals(requiredIdAsString))
                .findFirst();
        if (!result.isPresent())
            return Optional.empty();

        return Optional.of(buildSpeciality(result.get()));
    }

    @Override
    public List<Speciality> findAll() {
        List<Speciality> specialities = readWriteDataBase.readData(pathToDBFile)
                                                .stream()
                                                .map(row -> row.split(csvDelimiter))
                                                .filter(array -> !array[0].equalsIgnoreCase("ID")) //remove header
                                                .map(this::buildSpeciality)
                                                .collect(Collectors.toList());
        return specialities;
    }

    private Speciality isExistInDataBaseList(List<Speciality> allSpecialities, long id) {
        return  allSpecialities.stream()
                .filter(currentSpeciality -> currentSpeciality.getId() == id)
                .findFirst().orElse(null);
    }

    private Speciality buildSpeciality(String[] specialityFields) {
        long id = Long.parseLong(specialityFields[0]);
        String name = specialityFields[1];

        return new Speciality(name, id);
    }
}
