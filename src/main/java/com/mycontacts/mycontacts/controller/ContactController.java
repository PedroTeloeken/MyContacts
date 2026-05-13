package com.mycontacts.mycontacts.controller;

import com.mycontacts.mycontacts.entity.Contact;
import com.mycontacts.mycontacts.service.ContactService;

import jakarta.validation.Valid;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @DeleteMapping(value ="/{id}")
  public ResponseEntity<Void> delete(@Valid @PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
