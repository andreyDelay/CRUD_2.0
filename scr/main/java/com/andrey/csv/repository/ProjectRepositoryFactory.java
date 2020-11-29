package com.andrey.csv.repository;

public interface ProjectRepositoryFactory {
    CustomerRepository getCustomerRepository();

    AccountRepository getAccountRepository();

    SpecialityRepository getSpecialityRepository();
}
