package com.andrey.csv.controllers;

import com.andrey.csv.controllers.concrets.CustomerController;
import com.andrey.csv.model.Customer;
import com.andrey.csv.repository.ProjectRepositoryFactory;
import com.andrey.csv.utils.ExecutionResultsForUI;
import com.andrey.csv.view.Viewer;

import java.util.List;
import java.util.Scanner;

public class FrontController {
    private CustomerController customerController;
    private Viewer viewer;
    private Scanner scanner;

    private boolean isSecondaryMenuWasShown;
    private Customer currentCustomer;

    public FrontController(Viewer viewer, ProjectRepositoryFactory repositoryFactory)
    {
        this.viewer = viewer;
        customerController = new CustomerController(viewer, repositoryFactory);
        scanner = new Scanner(System.in);
    }

    public void startApplication() {
        viewer.showWelcomeMassage();
        mainMenu();
    }

    public void mainMenu() {
        showMainMenu();
        int chosenMenuItem;
        do {
            chosenMenuItem = readChosenMenuItem(ExecutionResultsForUI.WRONG_MENU_ITEM);

            switch (chosenMenuItem) {
                //Показать существующую информацию
                case 1:
                    List<Customer> allCustomersList = customerController.getAllCustomers();
                    customerController.showAllCustomers(allCustomersList);
                    showMainMenu();
                    break;
                //Показать подробные данные покупателя
                case 2:
                    System.out.println("Введите id покупателя:");
                    currentCustomer = customerController.getConcreteCustomer(
                                                askUserInputID(ExecutionResultsForUI.INCORRECT_ID));
                    customerController.showConcreteCustomer(currentCustomer);
                    showSecondaryMenu();
                    break;
                case 3:
                //Добавить покупателя
                    Customer newCustomer = customerController.startCreatingNewCustomer(scanner);
                    customerController.saveNewCustomer(newCustomer);
                    showMainMenu();
                    break;
                case 4:
                //Удалить покупателя
                    System.out.println("Введите id:");
                    customerController.deleteCustomer(askUserInputID(ExecutionResultsForUI.INCORRECT_ID));
                    showMainMenu();
                    break;
                case 5:
                    customerController.addSpeciality(scanner, currentCustomer);
                    showSecondaryMenu();
                    break;
                case 6:
                    System.out.println("Введите id:");
                    long id = askUserInputID(ExecutionResultsForUI.INCORRECT_ID);
                    customerController.deleteSpeciality(id, currentCustomer);
                    showSecondaryMenu();
                    break;
                case 7:
                    showMainMenu();
                    isSecondaryMenuWasShown = false;
                    break;
            }

        } while (chosenMenuItem != 0);
    }

    private void showSecondaryMenu() {
        isSecondaryMenuWasShown = true;
        viewer.showSecondaryMenu();
    }

    private void showMainMenu() {
        viewer.showMainMenu();
    }

    public int readChosenMenuItem(ExecutionResultsForUI message) {
        int chosenMenuItem;
        try {
            chosenMenuItem = scanner.nextInt();
            if (!isChosenMenuItemExist(chosenMenuItem)) {
                throw new Exception();
            }
            return chosenMenuItem;
        } catch (Exception e) {
            viewer.showResultOfOperation(message);
            scanner.nextLine();
            chosenMenuItem = readChosenMenuItem(message);
            return chosenMenuItem;
        }
    }

    private boolean isChosenMenuItemExist(int chosenMenuItem) {
        if (isSecondaryMenuWasShown) {
            return chosenMenuItem >= 5 && chosenMenuItem <= 7;
        }
        return chosenMenuItem >= 1 && chosenMenuItem <= 4;
    }

    private long askUserInputID(ExecutionResultsForUI message) {
        try {
            long verifiedID = verifyID(scanner.nextLong());
            if (verifiedID != -1) {
                return verifiedID;
            }
        } catch (Exception e) {
            viewer.showResultOfOperation(ExecutionResultsForUI.INCORRECT_ID);
            return -1;
        }
        viewer.showResultOfOperation(message);
        return -1;
    }

    private long verifyID(long id) {
        String currentID = Long.toString(id);
        String result = currentID.replaceAll("[^0-9]", "");
        if (result.length() == 0) {
            return -1;
        }
        return Long.parseLong(result);
    }

}
