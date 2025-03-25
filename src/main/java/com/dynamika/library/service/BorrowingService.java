package com.dynamika.library.service;

import com.dynamika.library.model.exception.NotFoundException;
import com.dynamika.library.model.dto.BorrowingDto;
import com.dynamika.library.entity.Book;
import com.dynamika.library.entity.Borrowing;
import com.dynamika.library.entity.Client;
import com.dynamika.library.repository.BookRepository;
import com.dynamika.library.repository.BorrowingRepository;
import com.dynamika.library.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BorrowingService {
    private final BorrowingRepository borrowingRepository;
    private final ClientRepository clientRepository;
    private final BookRepository bookRepository;

    public Borrowing createBorrowing(Long clientId, Long bookId, LocalDate borrowDate) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Клиент не найден: " + clientId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Аренда книги не найдена: " + bookId));

        Borrowing borrowing = new Borrowing();
        borrowing.setClient(client);
        borrowing.setBook(book);
        borrowing.setBorrowDate(borrowDate);

        return borrowingRepository.save(borrowing);
    }

    public List<Borrowing> getAllBorrowings() {
        return borrowingRepository.findAll();
    }

    public Borrowing getBorrowingById(Long id) {
        return borrowingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Аренда книги не найдена: " + id));
    }

    public Borrowing returnBook(Long id) {
        return borrowingRepository.findById(id)
                .map(borrowing -> {
                    borrowing.setReturnDate(LocalDate.now());
                    return borrowingRepository.save(borrowing);
                })
                .orElseThrow(() -> new NotFoundException("Аренда книги не найдена: " + id));

    }

    public List<Borrowing> getActiveBorrowings() {
        return borrowingRepository.findAll();
    }

    public List<BorrowingDto> getActiveBorrowingsWithDetails() {
        return borrowingRepository.findAllActiveBorrowings()
                .stream()
                .map(borrowing -> {
                    BorrowingDto dto = new BorrowingDto();
                    dto.setClientFullName(borrowing.getClient().getFullName());
                    dto.setClientBirthDate(borrowing.getClient().getBirthDate());
                    dto.setBookTitle(borrowing.getBook().getTitle());
                    dto.setBookAuthor(borrowing.getBook().getAuthor());
                    dto.setBookIsbn(borrowing.getBook().getIsbn());
                    dto.setBorrowDate(borrowing.getBorrowDate());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}