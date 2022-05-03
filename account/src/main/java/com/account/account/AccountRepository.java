package com.account.account;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
// Simple custom queries can be added with: 'https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation'
// Advanced custom queries can be added with : 'https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query'

public interface AccountRepository extends CrudRepository<Account, Integer> {
    public Account findByEmail(String email);
}
