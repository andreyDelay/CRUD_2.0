package com.andrey.csv.model;


import com.andrey.csv.model.visitor.Visitor;

import java.util.HashSet;
import java.util.Set;

public class Customer implements Model {
    Account account;
    private Set<Speciality> specialities;
    private String dateOfRegistration;
    private String name;
    private String lastName;

    private int age;
    private long id;

    public Customer(long id, String name, String lastName, int age, Set<Speciality> specialities, Account account, String dateOfRegistration) {
        this.account = account;
        this.specialities = specialities;
        this.dateOfRegistration = dateOfRegistration;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.id = id;
    }

    public Customer(Account account, String name, String lastName, int age) {
        this.account = account;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.dateOfRegistration = java.time.LocalDate.now().toString();
        specialities = new HashSet<>();
    }

    public String getDateOfRegistration() {
        return dateOfRegistration;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addSpeciality(Speciality speciality) {
        specialities.add(speciality);
    }

    public void removeSpeciality(Speciality speciality) {
        specialities.remove(speciality);
    }

    public Set<Speciality> getSpecialities() {
        return specialities;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String adaptObjectForCSVParser(Visitor visitor) {
        return visitor.prepareDataForCSVPrinter(this);
    }
}
