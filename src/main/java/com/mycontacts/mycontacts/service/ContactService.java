package com.mycontacts.mycontacts.service;

import com.mycontacts.mycontacts.entity.Contact;
import com.mycontacts.mycontacts.repository.ContactRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Contact service. */
@Service
public class ContactService {

  @Autowired
  private ContactRepository repository;

  public Contact create(Contact contact) {
    return repository.save(contact);
  }

  public List<Contact> findAll() {
    return repository.findAll();
  }
}
