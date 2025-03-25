package com.dynamika.library.service;

import com.dynamika.library.model.exception.NotFoundException;
import com.dynamika.library.entity.Book;
import com.dynamika.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Книга не найдена: " + id));
    }

    public Book addBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new RuntimeException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) throws NotFoundException {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    book.setIsbn(updatedBook.getIsbn());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new NotFoundException("Книга не найдена: " + id));
    }

    public void deleteBook(Long id) throws NotFoundException {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Книга не найдена: " + id);
        }
        bookRepository.deleteById(id);
    }

}
