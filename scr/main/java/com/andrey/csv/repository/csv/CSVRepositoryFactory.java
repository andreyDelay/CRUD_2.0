package com.andrey.csv.repository.csv;

import com.andrey.csv.model.visitor.Visitor;
import com.andrey.csv.repository.AccountRepository;
import com.andrey.csv.repository.CustomerRepository;
import com.andrey.csv.repository.ProjectRepositoryFactory;
import com.andrey.csv.repository.SpecialityRepository;

public class CSVRepositoryFactory implements ProjectRepositoryFactory {
    private Visitor visitor;

    public CSVRepositoryFactory(Visitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public CustomerRepository getCustomerRepository() {
        return new CSVCustomerRepository(visitor);
    }

    @Override
    public AccountRepository getAccountRepository() {
        return new CSVAccountRepository(visitor);
    }

    @Override
    public SpecialityRepository getSpecialityRepository() {
        return new CSVSpecialityRepository(visitor);
    }
}
