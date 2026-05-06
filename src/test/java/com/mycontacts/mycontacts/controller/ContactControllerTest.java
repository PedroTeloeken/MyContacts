package com.mycontacts.mycontacts.controller;

import com.mycontacts.mycontacts.entity.Contact;
import com.mycontacts.mycontacts.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ContactControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ContactRepository repository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void shouldCreateContactSuccessfully() throws Exception {
        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "João",
                        "email": "joao@email.com"
                    }
                """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("João"))
            .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void shouldReturnBadRequestWhenBodyIsMissing() throws Exception {
        mockMvc.perform(post("/contacts"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenJsonIsInvalid() throws Exception {
        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnContactsList() throws Exception {
        // prepara dados
        Contact contact = new Contact();
        contact.setName("Maria");
        contact.setEmail("maria@email.com");
        repository.save(contact);

        mockMvc.perform(get("/contacts"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldReturnEmptyList() throws Exception {
        repository.deleteAll();

        mockMvc.perform(get("/contacts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }
}