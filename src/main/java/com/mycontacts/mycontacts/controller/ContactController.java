package com.mycontacts.mycontacts.controller;

import com.mycontacts.mycontacts.entity.Contact;
import com.mycontacts.mycontacts.service.ContactService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Contact controller. */
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
