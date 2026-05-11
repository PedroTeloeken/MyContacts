package com.mycontacts.mycontacts.service;

import com.mycontacts.mycontacts.dto.ContactRequest;
import com.mycontacts.mycontacts.dto.ContactResponse;
import com.mycontacts.mycontacts.entity.Contact;
import com.mycontacts.mycontacts.exception.ContactNotFoundException;
import com.mycontacts.mycontacts.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public ContactResponse create(ContactRequest request) {
        Contact contact = new Contact(request.getName(), request.getPhone(), request.getEmail());
        Contact savedContact = contactRepository.save(contact);
        return toResponse(savedContact);
    }

    public List<ContactResponse> findAll() {
        return contactRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ContactResponse findById(Long id) {
        return toResponse(findContactById(id));
    }

    public ContactResponse update(Long id, ContactRequest request) {
        Contact existingContact = findContactById(id);
        existingContact.setName(request.getName());
        existingContact.setPhone(request.getPhone());
        existingContact.setEmail(request.getEmail());

        Contact updatedContact = contactRepository.save(existingContact);
        return toResponse(updatedContact);
    }

    public void delete(Long id) {
        Contact existingContact = findContactById(id);
        contactRepository.delete(existingContact);
    }

    private Contact findContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    private ContactResponse toResponse(Contact contact) {
        return new ContactResponse(
                contact.getId(),
                contact.getName(),
                contact.getPhone(),
                contact.getEmail()
        );
    }
}
