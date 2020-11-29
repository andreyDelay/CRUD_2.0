package com.andrey.csv.view;

import com.andrey.csv.utils.ExecutionResultsForUI;

public class ConcreteViewer implements Viewer{


    @Override
    public void showWelcomeMassage() {
        System.out.println("Добро пожаловать в приложение!");
        System.out.println("*********************************\n");
    }

    @Override
    public void showMainMenu() {
        System.out.println("===============================");
        System.out.println("Введите номер операции, которую хотите совершить");
        System.out.println("1. Показать существующую информацию.");
        System.out.println("2. Изменить данные покупателя.");
        System.out.println("3. Добавить покупателя");
        System.out.println("4. Удалить покупателя");
        System.out.println();
        System.out.println("0. Выход");
        System.out.println("===============================");
    }

    @Override
    public void showSecondaryMenu() {
        System.out.println("===============================");
        System.out.println("5. Добавить специальность.");
        System.out.println("6. Удалить специальность.");
        System.out.println();
        System.out.println("7. Назад");
        System.out.println("===============================");
    }

    @Override
    public void showDataForOneEntity(String dataToBeShown) {
        System.out.println(dataToBeShown);
    }

    @Override
    public void showResultOfOperation(ExecutionResultsForUI message) {
        System.out.println(message.getMessage());
    }
}
