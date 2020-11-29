package com.andrey.csv.model.visitor;

import com.andrey.csv.model.Account;
import com.andrey.csv.model.Customer;
import com.andrey.csv.model.Speciality;

import java.util.Set;


public class CSVVisitor implements Visitor {
    private char csvDelimiter = ';';

    public String prepareDataForCSVPrinter(Customer customer) {
        StringBuilder dataForCSVParser = new StringBuilder();
        dataForCSVParser
                .append(customer.getId()).append(csvDelimiter)
                .append(customer.getName()).append(csvDelimiter)
                .append(customer.getLastName()).append(csvDelimiter)
                .append(customer.getAge()).append(csvDelimiter)
                .append(getSpecialitiesNumbers(customer.getSpecialities())).append(csvDelimiter)
                .append(customer.getAccount().getName()).append(csvDelimiter)
                .append(customer.getDateOfRegistration());
        return dataForCSVParser.toString();
    }

    private String getSpecialitiesNumbers(Set<Speciality> specialities) {
        if (specialities == null || specialities.size() == 0) return "";

        StringBuilder numbers = new StringBuilder();
        for (Speciality s : specialities) {
            numbers.append(s.getId()).append(',');
        }
        numbers.deleteCharAt(numbers.length() - 1);
        return numbers.toString();
    }

    public String prepareDataForCSVPrinter(Speciality speciality) {
        StringBuilder dataForCSVParser = new StringBuilder();
        dataForCSVParser
                .append(speciality.getId()).append(csvDelimiter)
                .append(speciality.getName());
        return dataForCSVParser.toString();
    }


    public String prepareDataForCSVPrinter(Account account) {
        StringBuilder dataForCSVParser = new StringBuilder();
        dataForCSVParser
                .append(account.getId()).append(csvDelimiter)
                .append(account.getName()).append(csvDelimiter)
                .append(account.getStatus());
        return dataForCSVParser.toString();
    }

}
