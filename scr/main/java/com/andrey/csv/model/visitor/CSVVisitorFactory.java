package com.andrey.csv.model.visitor;

public class CSVVisitorFactory implements VisitorFactory {
    @Override
    public Visitor createVisitor() {
        return new CSVVisitor();
    }
}
