package com.andrey.csv.repository.csv;

import com.andrey.csv.model.Account;
import com.andrey.csv.model.AccountStatus;
import com.andrey.csv.model.visitor.Visitor;
import com.andrey.csv.repository.AccountRepository;
import com.andrey.csv.repository.csvutils.ConcreteReadWriteDB;
import com.andrey.csv.repository.csvutils.ReadWriteDataBase;
import com.andrey.csv.repository.csvutils.headers.AccountHeaders;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CSVAccountRepository implements AccountRepository {
    private Path pathToDBFile = Paths.get("scr\\main\\java\\com\\andrey\\csv\\resources\\accounts.csv");
    private String csvDelimiter = ";";
    Visitor visitor;
    ReadWriteDataBase readWriteDataBase;

    public CSVAccountRepository(Visitor visitor) {
        this.visitor = visitor;
        List<String> projectHeaders = Arrays.stream(AccountHeaders.values())
                .map(Enum::toString)
                .collect(Collectors.toList());
        readWriteDataBase = new ConcreteReadWriteDB(projectHeaders);
    }

    @Override
    public Account save(Account newAccount) {
        List<String> adaptedObjects = new ArrayList<>();
        newAccount.setId(readWriteDataBase.incrementIDForNewObject(pathToDBFile));
        adaptedObjects.add(visitor.prepareDataForCSVPrinter(newAccount));
        readWriteDataBase.writeData(adaptedObjects, pathToDBFile);
        return newAccount;
    }

    @Override
    public Account update(Long id, Account newValue) {
        List<Account> allAccounts = findAll();
        Account oldValue = isExistInDataBaseList(allAccounts, id);
        if (oldValue != null) {
            allAccounts.set(allAccounts.indexOf(oldValue), newValue);
            oldValue = newValue;
        }
        return oldValue;
    }

    @Override
    public Account delete(Long id) {
        List<Account> allAccounts= findAll();
        Account account = isExistInDataBaseList(allAccounts, id);
        if (account != null) {
            allAccounts.remove(account);
            List<String> dataForDB = allAccounts.stream()
                    .map(visitor::prepareDataForCSVPrinter)
                    .collect(Collectors.toList());
            readWriteDataBase.writeData(dataForDB, pathToDBFile);
        }
        return account;
    }

    @Override
    public Optional<Account> find(Long id) {
        String requiredIdAsString = String.valueOf(id);
        Optional<String[]> result = readWriteDataBase.readData(pathToDBFile)
                .stream()
                .map(row -> row.split(csvDelimiter))
                .filter(array -> array[0].length() != 0)
                .filter(array -> array[0].equalsIgnoreCase(requiredIdAsString))
                .findFirst();

        if (!result.isPresent())
            return Optional.empty();


        return Optional.of(buildAccount(result.get()));
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = readWriteDataBase.readData(pathToDBFile)
                .stream()
                .map(row -> row.split(csvDelimiter))
                .filter(array -> !array[0].equalsIgnoreCase("ID"))
                .map(this::buildAccount)
                .collect(Collectors.toList());

        return accounts;
    }

    private Account isExistInDataBaseList(List<Account> allAccounts, long id) {
        return  allAccounts.stream()
                .filter(currentAccount -> currentAccount.getId() == id)
                .findFirst().orElse(null);
    }

    private Account buildAccount(String[] accountFields) {
        long id = Long.parseLong(accountFields[0]);
        String name = accountFields[1];
        String accountStatus = accountFields[2];

        return new Account(name, id, AccountStatus.valueOf(accountStatus));
    }
}
