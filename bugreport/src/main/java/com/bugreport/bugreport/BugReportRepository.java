package com.bugreport.bugreport;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
// Simple custom queries can be added with: 'https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation'
// Advanced custom queries can be added with : 'https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query'

public interface BugReportRepository extends CrudRepository<BugReport, Integer> {
    public List<BugReport> findByTitleContainingIgnoreCase(String title);

    public List<BugReport> findByPageId(Integer pageId);

    public List<BugReport> findByPageIdEqualsAndTitleContainingIgnoreCase(Integer pageId, String title);
}
