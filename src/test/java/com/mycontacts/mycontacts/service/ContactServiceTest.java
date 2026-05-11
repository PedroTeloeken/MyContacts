package com.mycontacts.mycontacts.service;

import com.mycontacts.mycontacts.dto.ContactRequest;
import com.mycontacts.mycontacts.dto.ContactResponse;
import com.mycontacts.mycontacts.entity.Contact;
import com.mycontacts.mycontacts.exception.ContactNotFoundException;
import com.mycontacts.mycontacts.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    @Test
    void shouldCreateContactSuccessfully() {
        ContactRequest request = new ContactRequest("Ana", "11999999999", "ana@email.com");
        Contact savedContact = contactWithId(1L, "Ana", "11999999999", "ana@email.com");

        when(contactRepository.save(any(Contact.class))).thenReturn(savedContact);

        ContactResponse response = contactService.create(request);

        assertEquals(1L, response.getId());
        assertEquals("Ana", response.getName());
        assertEquals("11999999999", response.getPhone());
        assertEquals("ana@email.com", response.getEmail());
        verify(contactRepository).save(any(Contact.class));
    }

    @Test
    void shouldListContacts() {
        when(contactRepository.findAll()).thenReturn(List.of(
                contactWithId(1L, "Ana", "1111", "ana@email.com"),
                contactWithId(2L, "Bruno", "2222", "bruno@email.com")
        ));

        List<ContactResponse> responses = contactService.findAll();

        assertEquals(2, responses.size());
        assertEquals("Ana", responses.get(0).getName());
        assertEquals("Bruno", responses.get(1).getName());
    }

    @Test
    void shouldFindContactById() {
        Contact contact = contactWithId(1L, "Ana", "1111", "ana@email.com");
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        ContactResponse response = contactService.findById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Ana", response.getName());
    }

    @Test
    void shouldThrowWhenContactDoesNotExist() {
        when(contactRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () -> contactService.findById(99L));
    }

    @Test
    void shouldUpdateContactSuccessfully() {
        Contact existingContact = contactWithId(1L, "Ana", "1111", "ana@email.com");
        Contact updatedContact = contactWithId(1L, "Ana Silva", "2222", "ana.silva@email.com");
        ContactRequest request = new ContactRequest("Ana Silva", "2222", "ana.silva@email.com");

        when(contactRepository.findById(1L)).thenReturn(Optional.of(existingContact));
        when(contactRepository.save(existingContact)).thenReturn(updatedContact);

        ContactResponse response = contactService.update(1L, request);

        assertEquals("Ana Silva", response.getName());
        assertEquals("2222", response.getPhone());
        assertEquals("ana.silva@email.com", response.getEmail());
        verify(contactRepository).save(existingContact);
    }

    @Test
    void shouldDeleteContactSuccessfully() {
        Contact existingContact = contactWithId(1L, "Ana", "1111", "ana@email.com");
        when(contactRepository.findById(1L)).thenReturn(Optional.of(existingContact));

        contactService.delete(1L);

        verify(contactRepository, times(1)).delete(existingContact);
    }

    private Contact contactWithId(Long id, String name, String phone, String email) {
        Contact contact = new Contact(name, phone, email);
        try {
            java.lang.reflect.Field idField = Contact.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(contact, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to set contact id for tests", exception);
        }
        return contact;
    }
}
