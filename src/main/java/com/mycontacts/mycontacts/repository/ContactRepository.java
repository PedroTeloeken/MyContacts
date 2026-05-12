package com.mycontacts.mycontacts.repository;

import com.mycontacts.mycontacts.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

/** Contact repository. */
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
