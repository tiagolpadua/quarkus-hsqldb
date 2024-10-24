package org.acme.services;

import java.util.List;

import org.acme.models.Book;
import org.acme.repositories.BookRepository;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class BookService {
    private final BookRepository bookRepository;

    @Inject
    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Startup
    public void seed() {
        log.info("Seeding books");  
        long startTime = System.currentTimeMillis();

        for (var i = 0; i < 1000000; i++) {
            var book = new Book();
            book.setTitle("Book " + i);
            book.setAuthor("Author " + i);
            persist(book);
        }
        
        long endTime = System.currentTimeMillis();
        log.info("Time taken to seed books: {} ms", (endTime - startTime));
        log.info("Seeding books completed");
    }

    public List<Book> findAll() {
        return bookRepository.findAll().list();
    }

    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id);
    }

    public void persist(Book book) {
        bookRepository.persist(book);
    }
}
