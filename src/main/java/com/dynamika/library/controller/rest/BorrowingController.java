package com.dynamika.library.controller.rest;

import com.dynamika.library.model.exception.NotFoundException;
import com.dynamika.library.entity.Borrowing;
import com.dynamika.library.service.BorrowingService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;

    @PostMapping
    public ResponseEntity<?> createBorrowing(@RequestParam Long clientId,
                                             @RequestParam Long bookId,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate borrowDate) {
        try {
            Borrowing borrowing = borrowingService.createBorrowing(clientId, bookId, borrowDate);
            return new ResponseEntity<>(borrowing, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Borrowing>> getAllBorrowings() {
        try {
            List<Borrowing> borrowings = borrowingService.getAllBorrowings();
            if (borrowings.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(borrowings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrowing> getBorrowingById(@PathVariable Long id) {
        try {
            Borrowing borrowing = borrowingService.getBorrowingById(id);
            return new ResponseEntity<>(borrowing, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Borrowing> returnBook(@PathVariable Long id) {
        try {
            Borrowing borrowing = borrowingService.returnBook(id);
            return new ResponseEntity<>(borrowing, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Borrowing>> getActiveBorrowings() {
        try {
            List<Borrowing> activeBorrowings = borrowingService.getActiveBorrowings();
            return new ResponseEntity<>(activeBorrowings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}