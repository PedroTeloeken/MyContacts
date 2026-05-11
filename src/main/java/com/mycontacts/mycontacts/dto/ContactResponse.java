package com.mycontacts.mycontacts.dto;

public class ContactResponse {

    private Long id;
    private String name;
    private String phone;
    private String email;

    public ContactResponse() {
    }

    public ContactResponse(Long id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
