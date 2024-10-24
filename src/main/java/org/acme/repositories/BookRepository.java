package org.acme.repositories;

import org.acme.models.Book;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

// https://quarkus.io/guides/hibernate-orm-panache#defining-your-repository
@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {
    public Book findByTitle(String title) {
        return find("title", title).firstResult();
    }
}
