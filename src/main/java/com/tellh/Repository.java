package com.tellh;

/**
 * Created by tlh on 2016/10/19.
 */
public class Repository {
    private String name;
    private String full_name;
    private String owner;

    public Repository() {
    }

    public Repository(String name, String full_name, String owner) {
        this.name = name;
        this.full_name = full_name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", full_name='" + full_name + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
