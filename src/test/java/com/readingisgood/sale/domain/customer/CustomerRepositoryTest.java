package com.readingisgood.sale.domain.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
class CustomerRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void findByEmail() {

        String email = "johdoe@readingisgood.com";
        Customer customer = Customer.builder().email(email)
                .name("john")
                .lastName("doe")
                .build();

        testEntityManager.persistAndFlush(customer);
        testEntityManager.clear();

        Optional<Customer> result = customerRepository.findByEmail(email);
        assertThat(result).hasValueSatisfying(found ->
                assertThat(found).isEqualTo(customer));
    }
}