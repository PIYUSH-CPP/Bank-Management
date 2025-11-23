package com.example.bank.model;

public class Customer {
    private final String customerId;
    private final String name;
    private String email;
    private String phone;
    private String address;

    public Customer(String customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    public void updateContact(String email, String phone) {
        if (email != null) this.email = email;
        if (phone != null) this.phone = phone;
    }

    public void updateAddress(String address) {
        this.address = address;
    }
}
