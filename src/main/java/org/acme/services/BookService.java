package org.acme.services;

import java.util.List;

import org.acme.models.Book;
import org.acme.repositories.BookRepository;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class BookService {
    private final BookRepository bookRepository;

    @Inject
    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Startup
    public void seed() {
        var book = new Book();
        book.setTitle("The Hobbit");
        book.setAuthor("J.R.R. Tolkien");
        persist(book);

        var book2 = new Book();
        book2.setTitle("1984");
        book2.setAuthor("George Orwell");
        persist(book2);

        var book3 = new Book();
        book3.setTitle("To Kill a Mockingbird");
        book3.setAuthor("Harper Lee");
        persist(book3);

        var book4 = new Book();
        book4.setTitle("Pride and Prejudice");
        book4.setAuthor("Jane Austen");
        persist(book4);

        var book5 = new Book();
        book5.setTitle("The Great Gatsby");
        book5.setAuthor("F. Scott Fitzgerald");
        persist(book5);

        var book6 = new Book();
        book6.setTitle("Moby Dick");
        book6.setAuthor("Herman Melville");
        persist(book6);
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
