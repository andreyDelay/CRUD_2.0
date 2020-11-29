package com.andrey.csv.controllers.concrets;


import com.andrey.csv.model.Speciality;
import com.andrey.csv.repository.ProjectRepositoryFactory;
import com.andrey.csv.repository.SpecialityRepository;
import com.andrey.csv.utils.ExecutionResultsForUI;
import com.andrey.csv.view.Viewer;

import java.util.Optional;
import java.util.Scanner;

public class SpecialityController {

    private SpecialityRepository repository;
    private Viewer viewer;

    public SpecialityController(Viewer viewer, ProjectRepositoryFactory repositoryFactory) {
        this.repository = repositoryFactory.getSpecialityRepository();
        this.viewer = viewer;
    }

    public Speciality getSpeciality(long id) {
        Optional<Speciality> speciality = repository.find(id);
        return speciality.orElse(null);
    }

    public void save(Speciality speciality) {
        if (speciality != null) {
            repository.save(speciality);
            viewer.showResultOfOperation(ExecutionResultsForUI.SUCCESSFULLY_DONE);
        } else {
            viewer.showResultOfOperation(ExecutionResultsForUI.INCORRECT_DATA);
        }
    }

    public void delete(long id) {
        Speciality toBeDeleted = repository.delete(id);
        if (toBeDeleted == null) {
            viewer.showResultOfOperation(ExecutionResultsForUI.OPERATION_FAILED);
            return;
        }
        viewer.showResultOfOperation(ExecutionResultsForUI.SUCCESSFULLY_DONE);
    }

    public Speciality startCreatingSpeciality(Scanner scanner) {
        Speciality speciality = null;
        String name;
        System.out.println("Введите название:");
        scanner.nextLine();
        name = scanner.nextLine();
        if (name.length() != 0) {
            speciality = new Speciality(name);
            save(speciality);
        }
        return speciality;
    }

}
