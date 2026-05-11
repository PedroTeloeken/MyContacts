package com.mycontacts.mycontacts.controller;

import com.mycontacts.mycontacts.entity.Contact;
import com.mycontacts.mycontacts.service.ContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService service;

    @PostMapping
    public Contact create(@RequestBody Contact contact) {
        return service.create(contact);
    }

    @GetMapping
    public List<Contact> list() {
        return service.findAll();
    }
}