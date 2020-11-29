package com.andrey.csv.model;

import com.andrey.csv.model.visitor.Visitor;

import java.util.Objects;

public class Speciality implements Model {
    private String name;
    private long id;

    public Speciality(String name) {
        this.name = name;
    }

    public Speciality(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + "," + id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Speciality that = (Speciality) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }


    @Override
    public String adaptObjectForCSVParser(Visitor visitor) {
        return visitor.prepareDataForCSVPrinter(this);
    }
}
