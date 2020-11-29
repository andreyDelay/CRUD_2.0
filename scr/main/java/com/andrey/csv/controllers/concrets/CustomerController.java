package com.andrey.csv.controllers.concrets;

import com.andrey.csv.model.Account;
import com.andrey.csv.model.Customer;
import com.andrey.csv.model.Speciality;
import com.andrey.csv.repository.CustomerRepository;
import com.andrey.csv.repository.ProjectRepositoryFactory;
import com.andrey.csv.repository.SpecialityRepository;
import com.andrey.csv.utils.ExecutionResultsForUI;
import com.andrey.csv.view.Viewer;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CustomerController {
    private ProjectRepositoryFactory repositoryFactory;
    private CustomerRepository repository;
    private Viewer viewer;

    public CustomerController(Viewer viewer, ProjectRepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        repository = repositoryFactory.getCustomerRepository();
        this.viewer = viewer;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = repository.findAll();
        return customers;
    }

    public Customer getConcreteCustomer(long id) {
        Optional<Customer> resultOfSearch = repository.find(id);
        return resultOfSearch.orElse(null);
    }

    public void saveNewCustomer(Customer newCustomer) {
        if (newCustomer != null) {
            repository.save(newCustomer);
            viewer.showResultOfOperation(ExecutionResultsForUI.SUCCESSFULLY_DONE);
        } else
            viewer.showResultOfOperation(ExecutionResultsForUI.INCORRECT_DATA);
    }

    public void update(Customer customer) {
        repository.update(customer.getId(), customer);
    }

    public void deleteCustomer(long id) {
        Customer toBeDeleted = repository.delete(id);
        if (toBeDeleted == null) {
            viewer.showResultOfOperation(ExecutionResultsForUI.OPERATION_FAILED);
        }
        viewer.showResultOfOperation(ExecutionResultsForUI.SUCCESSFULLY_DONE);
    }

    public void addSpeciality(Scanner scanner, Customer currentCustomer) {
        SpecialityController specialityController = new SpecialityController(viewer, repositoryFactory);
        Speciality speciality = specialityController.startCreatingSpeciality(scanner);
        currentCustomer.addSpeciality(speciality);
        update(currentCustomer);
    }

    public void deleteSpeciality(long id, Customer currentCustomer) {
        SpecialityController specialityController = new SpecialityController(viewer, repositoryFactory);
        Speciality speciality = specialityController.getSpeciality(id);
        currentCustomer.removeSpeciality(speciality);
        update(currentCustomer);
    }

    public void showAllCustomers(List<Customer> customers) {
        for(Customer currentCustomer: customers) {
            showConcreteCustomer(currentCustomer);
        }
    }

    public void showConcreteCustomer(Customer customer) {
        String separator = " || ";
        if (isCustomerWasSuccessfullyFound(customer)) {
            StringBuilder representationOfOneCustomer = new StringBuilder();

            representationOfOneCustomer
                    .append("ID: ").append(customer.getId()).append(separator)
                    .append("name: ").append(customer.getName()).append(separator)
                    .append("last name: ").append(customer.getLastName()).append(separator)
                    .append("age: ").append(customer.getAge()).append(separator)
                    .append("account: ").append(customer.getAccount().getName())
                                        .append("(").append("status - ")
                                        .append(customer.getAccount().getStatus())
                                        .append(")").append(separator)
                    .append("date of registration: ").append(customer.getDateOfRegistration())
                    .append("\n============================================\n");
            //TODO добавить информацию о специализациях
            viewer.showDataForOneEntity(representationOfOneCustomer.toString());
        } else {
            viewer.showResultOfOperation(ExecutionResultsForUI.ID_NOT_FOUND);
        }
    }

    private boolean isCustomerWasSuccessfullyFound(Customer customer) {
        return customer != null;
    }

    //планируется замена на цепочку ответственности, но пока не придумал как)))
    public Customer startCreatingNewCustomer(Scanner inputData) {
        String accountName;
        String name;
        String lastName;
        int age;
        System.out.println("Введите аккаунт:");
        inputData.nextLine();
        accountName = inputData.nextLine();
        System.out.println("Введите имя:");
//        inputData.nextLine();
        name = inputData.nextLine();
        System.out.println("Введите фамилию:");
        lastName = inputData.nextLine();
        System.out.println("Введите возраст:");
        age = inputData.nextInt();

        if (isDataForNewCustomerCorrect(accountName, name, lastName, age)) {
            AccountController accountController = new AccountController(viewer, repositoryFactory);
            Account account = accountController.createNewAccount(accountName);
            return new Customer(account, name, lastName, age);
        }
        return null;            // Не хотелось возвращать null!!
    }

    private boolean isDataForNewCustomerCorrect(String account, String name, String lastName, int age) {
        return account.length() == 0 || age < 0 || name.length() == 0 || lastName.length() == 0;
    }

    public ProjectRepositoryFactory getRepositoryFactory() {
        return repositoryFactory;
    }

}
