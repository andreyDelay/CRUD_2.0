package com.andrey.csv.controllers.concrets;


import com.andrey.csv.model.Account;
import com.andrey.csv.repository.AccountRepository;
import com.andrey.csv.repository.ProjectRepositoryFactory;
import com.andrey.csv.utils.ExecutionResultsForUI;
import com.andrey.csv.view.Viewer;

import java.util.List;
import java.util.Optional;

public class AccountController {
    private AccountRepository repository;
    Viewer viewer;

    public AccountController(Viewer viewer, ProjectRepositoryFactory repositoryFactory) {
        repository = repositoryFactory.getAccountRepository();
        this.viewer = viewer;
    }

    public Account createNewAccount(String accountName) {
            Account account = new Account(accountName);
        if (isAccountExists(account)) {
            viewer.showResultOfOperation(ExecutionResultsForUI.ACCOUNT_EXISTS);
            return account;
        }
        viewer.showResultOfOperation(ExecutionResultsForUI.SUCCESSFULLY_DONE);
        return repository.save(account);
    }

    private boolean isAccountExists(Account account) {
        List<Account> accounts = repository.findAll();
        if (accounts == null || accounts.size() == 0) {
            return false;
        }
        Optional<Account> result = accounts.stream()
                            .filter(accFromDB -> accFromDB.getName().equalsIgnoreCase(account.getName()))
                            .findFirst();
        if (result.isPresent()) {
            account.setId(result.get().getId());
            return true;
        }
            return false;
    }

    public Account deleteAccount(Long id) {
        Account account = repository.delete(id);
        if (account == null) {
            viewer.showResultOfOperation(ExecutionResultsForUI.OPERATION_FAILED);
        }
        return account;
    }

}
