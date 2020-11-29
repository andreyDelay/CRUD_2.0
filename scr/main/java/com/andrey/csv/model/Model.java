package com.andrey.csv.model;

import com.andrey.csv.model.visitor.Visitor;

import java.util.List;

public interface Model {
    String adaptObjectForCSVParser(Visitor visitor);
}
