package com.kaziamyr.onlinebookstore.repository.impl;

import com.kaziamyr.onlinebookstore.exception.EntityNotFoundException;
import com.kaziamyr.onlinebookstore.model.Book;
import com.kaziamyr.onlinebookstore.repository.BookRepository;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Can't add book: " + book);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        String query = "FROM Book";
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(query, Book.class).getResultList();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't get all books from db", e);
        }
    }

    @Override
    public Book getBookById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Book.class, id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't get book by id " + id, e);

        }
    }
}
