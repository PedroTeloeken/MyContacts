package com.mycontacts.mycontacts.service;

import com.mycontacts.mycontacts.entity.Contact;
import com.mycontacts.mycontacts.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository repository;

    @InjectMocks
    private ContactService service;

    @Test
    void create_shouldSaveAndReturnContact() {
        Contact contact = new Contact();
        contact.setName("João");
        contact.setEmail("joao@email.com");

        when(repository.save(contact)).thenReturn(contact);

        Contact result = service.create(contact);

        assertThat(result.getName()).isEqualTo("João");
        assertThat(result.getEmail()).isEqualTo("joao@email.com");
        verify(repository, times(1)).save(contact);
    }

    @Test
    void findAll_shouldReturnAllContacts() {
        Contact c1 = new Contact();
        c1.setName("Maria");

        Contact c2 = new Contact();
        c2.setName("Pedro");

        when(repository.findAll()).thenReturn(List.of(c1, c2));

        List<Contact> result = service.findAll();

        assertThat(result).hasSize(4);
        assertThat(result).extracting(Contact::getName).containsExactly("Maria", "Pedro");
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoContacts() {
        when(repository.findAll()).thenReturn(List.of());

        List<Contact> result = service.findAll();

        assertThat(result).isEmpty();
        verify(repository, times(1)).findAll();
    }
}
