package com.andrey.csv.view;

import com.andrey.csv.utils.ExecutionResultsForUI;

public interface Viewer {
    void showWelcomeMassage();

    void showMainMenu();

    void showSecondaryMenu();

    void showDataForOneEntity(String dataToBeShown);

    void showResultOfOperation(ExecutionResultsForUI message);
}
