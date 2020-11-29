package com.andrey.csv.app;

import com.andrey.csv.controllers.FrontController;
import com.andrey.csv.model.visitor.CSVVisitorFactory;
import com.andrey.csv.model.visitor.VisitorFactory;
import com.andrey.csv.repository.ProjectRepositoryFactory;
import com.andrey.csv.repository.csv.CSVRepositoryFactory;
import com.andrey.csv.view.ConcreteViewer;
import com.andrey.csv.view.Viewer;

public class MainFlow {
    public static void main(String[] args) {
        Viewer userInterface = new ConcreteViewer();
        //abstract fabric
        VisitorFactory visitorFactory = new CSVVisitorFactory();
        ProjectRepositoryFactory repositoryFactory = new CSVRepositoryFactory(visitorFactory.createVisitor());
        FrontController controller = new FrontController(userInterface, repositoryFactory);
        controller.startApplication();

    }
}
