package com.example.libreria.repository;

import com.example.libreria.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {

    Book findById(int id);

    Iterable<Book> findByTitle(String title);
}
