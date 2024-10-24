package org.acme.services;

import java.io.IOException;
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
    private final LuceneService luceneService;

    @Inject
    BookService(BookRepository bookRepository, LuceneService luceneService) {
        this.bookRepository = bookRepository;
        this.luceneService = luceneService;
    }

    @Transactional
    @Startup
    public void seed() throws IOException {
        log.info("Seeding books");
        long startTime = System.currentTimeMillis();

        luceneService.openWriter();

        for (var i = 0; i < 1000000; i++) {
            var book = new Book();
            book.setTitle("Book " + i);
            book.setAuthor("Author " + i);
            persist(book);
            // luceneService.indexBook(book);
        }

        luceneService.closeWriter();

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
