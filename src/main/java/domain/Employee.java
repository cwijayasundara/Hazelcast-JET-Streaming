package domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Employee implements Serializable {

    Employee(){}

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", active=" + active +
                ", salary=" + salary +
                ", address='" + address + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @JsonProperty
    private String name;

    @JsonProperty
    private int age;

    @JsonProperty
    private boolean active;

    @JsonProperty
    private double salary;

    @JsonProperty
    private String address;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty
    private String lastName;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Employee(String name, int age, boolean active, double salary, String address, String lastName) {
        this.name = name;
        this.age = age;
        this.active = active;
        this.salary = salary;
        this.address = address;
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

}