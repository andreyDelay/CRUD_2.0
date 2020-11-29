package com.andrey.csv.model;

import com.andrey.csv.model.visitor.Visitor;

public class Account implements Model{
    private String name;
    private long id;
    private AccountStatus status;

    public Account(String name, long id, AccountStatus status) {
        this.name = name;
        this.status = status;
        this.id = id;
    }

    public Account(String name) {
        this.name = name;
        status = AccountStatus.ACTIVE;
    }


    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name + "," + id + "," + status;
    }

    @Override
    public String adaptObjectForCSVParser(Visitor visitor) {
        return visitor.prepareDataForCSVPrinter(this);
    }
}
