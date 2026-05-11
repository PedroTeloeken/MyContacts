package com.mycontacts.mycontacts.controller;

import com.mycontacts.mycontacts.entity.Contact;
import com.mycontacts.mycontacts.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    @Mock
    private ContactService service;

    @InjectMocks
    private ContactController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateContactSuccessfully() throws Exception {
        Contact contact = new Contact();
        contact.setName("João");
        contact.setEmail("joao@email.com");

        when(service.create(any(Contact.class))).thenReturn(contact);

        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contact)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("João"))
            .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void shouldReturnContactsList() throws Exception {
        Contact c1 = new Contact();
        c1.setName("Maria");

        Contact c2 = new Contact();
        c2.setName("Pedro");

        when(service.findAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/contacts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Maria"))
            .andExpect(jsonPath("$[1].name").value("Pedro"));
    }

    @Test
    void shouldReturnBadRequestWhenBodyIsMissing() throws Exception {
        mockMvc.perform(post("/contacts"))
            .andExpect(status().isBadRequest());
    }
}
