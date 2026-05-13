package com.mycontacts.mycontacts.service;

import com.mycontacts.mycontacts.entity.Contact;
import com.mycontacts.mycontacts.repository.ContactRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

  public void delete(Long id) {
    findById(id);
    repository.deleteById(id);
  }

  public Contact findById(Long id) {
    return repository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "Contato não encontrado"
      ));
  }
}
