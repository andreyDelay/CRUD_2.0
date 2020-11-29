package com.andrey.csv.utils;

public enum ExecutionResultsForUI {
    WRONG_MENU_ITEM("Incorrect menu item. Please input existing item of the menu."),
    INCORRECT_ID("Incorrect ID. ID must only be numbers."),
    INCORRECT_DATA("Error. Please try again with another data."),
    SUCCESSFULLY_DONE("Successfully done!"),
    OPERATION_FAILED("Operation failed!"),
    ID_NOT_FOUND("Such id doesn't exist!"),
    ACCOUNT_EXISTS("Such account already exists!");

    private String message;

    ExecutionResultsForUI(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
