package com.andrey.csv.repository.csv;

import com.andrey.csv.model.Account;
import com.andrey.csv.model.Customer;
import com.andrey.csv.model.Speciality;
import com.andrey.csv.model.visitor.Visitor;
import com.andrey.csv.repository.CustomerRepository;
import com.andrey.csv.repository.csvutils.ConcreteReadWriteDB;
import com.andrey.csv.repository.csvutils.ReadWriteDataBase;
import com.andrey.csv.repository.csvutils.headers.CustomerHeaders;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CSVCustomerRepository implements CustomerRepository {
    private Path pathToDBFile = Paths.get("scr\\main\\java\\com\\andrey\\csv\\resources\\customers.csv");
    private String csvDelimiter = ";";
    Visitor visitor;
    ReadWriteDataBase readWriteDataBase;

    public CSVCustomerRepository(Visitor visitor) {
        this.visitor = visitor;
        List<String> projectHeaders = Arrays.stream(CustomerHeaders.values())
                .map(Enum::toString)
                .collect(Collectors.toList());
        readWriteDataBase = new ConcreteReadWriteDB(projectHeaders);
    }

    @Override
    public Customer save(Customer newCustomer) {
        List<String> adaptedObjects = new ArrayList<>();
        newCustomer.setId(readWriteDataBase.incrementIDForNewObject(pathToDBFile));
        adaptedObjects.add(visitor.prepareDataForCSVPrinter(newCustomer));
        readWriteDataBase.writeData(adaptedObjects, pathToDBFile);
        return newCustomer;
    }

    @Override
    public Customer update(Long id, Customer newValue) {
        List<Customer> allCustomers = findAll();
        Customer oldValue = isExistInDataBaseList(allCustomers, id);
        if (oldValue != null) {
            allCustomers.set(allCustomers.indexOf(oldValue), newValue);
            oldValue = newValue;
        }
        return oldValue;
    }

    @Override
    public Customer delete(Long id){
        List<Customer> allCustomers = findAll();
        Customer customer = isExistInDataBaseList(allCustomers, id);
        if (customer != null) {
            allCustomers.remove(customer);
            List<String> dataForDB = allCustomers.stream()
                                    .map(visitor::prepareDataForCSVPrinter)
                                    .collect(Collectors.toList());
            readWriteDataBase.writeData(dataForDB, pathToDBFile);
        }
            return customer;
    }

    @Override
    public Optional<Customer> find(Long requiredID) {
        String requiredIdAsString = String.valueOf(requiredID);
        Optional<String[]> result = readWriteDataBase.readData(pathToDBFile).stream()
                                .map(row -> row.split(csvDelimiter))
                                .filter(array -> array[0].length() != 0)
                                .filter(array -> array[0].equals(requiredIdAsString))
                                .findFirst();
        if (!result.isPresent())
            return Optional.empty();

        return Optional.of(buildCustomer(result.get()));
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> rows = readWriteDataBase.readData(pathToDBFile)
                                                .stream()
                                                .map(row -> row.split(csvDelimiter))
                                                .filter(array -> !array[0].equalsIgnoreCase("ID")) //remove header
                                                .map(this::buildCustomer)
                                                .collect(Collectors.toList());
        return rows;
    }

    private Customer isExistInDataBaseList(List<Customer> allCustomers, long id) {
        return  allCustomers.stream()
                .filter(currentCustomer -> currentCustomer.getId() == id)
                .findFirst().orElse(null);
    }

    private Customer buildCustomer(String [] customerFields) {
        CSVSpecialityRepository specialityRepository = new CSVSpecialityRepository(visitor);
        CSVAccountRepository accountRepository = new CSVAccountRepository(visitor);

        long id = Long.parseLong(customerFields[0]);
        String name = customerFields[1];
        String lastName = customerFields[2];
        int age = Integer.parseInt(customerFields[3]);
        Set<Speciality> specialities = getSpecialities(customerFields[4], specialityRepository);
        Account account = accountRepository.find(Long.parseLong(customerFields[0])).get();
        String dateOfRegistration = customerFields[6];

        return new Customer(id, name, lastName, age, specialities, account, dateOfRegistration);
    }

    private Set<Speciality> getSpecialities(String specialitiesIDs, CSVSpecialityRepository specialityRepository) {
        List<Long> idNumbers =  Arrays.stream(specialitiesIDs.split(","))
                .map(value -> value.replaceAll("[^0-9]",""))
                .filter(value -> value.length() != 0)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        Set<Speciality> result = specialityRepository.findAll()
                .stream()
                .filter(speciality -> idNumbers.contains(speciality.getId()))
                .collect(Collectors.toSet());
        return result;
    }

}
