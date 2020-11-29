package com.andrey.csv.model.visitor;

import com.andrey.csv.model.Account;
import com.andrey.csv.model.Customer;
import com.andrey.csv.model.Speciality;

import java.util.List;

public interface Visitor {
    String prepareDataForCSVPrinter(Customer customer);
    String prepareDataForCSVPrinter(Speciality speciality);
    String prepareDataForCSVPrinter(Account account);

}
