package com.example.bookstoredemo.repository;

import com.example.bookstoredemo.models.Books;
import com.example.bookstoredemo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Books,Long> {
    List<Books> findByPublisher(User user);

    Object countByPublisher(User user);
}
