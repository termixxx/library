package com.dynamika.library.repository;

import com.dynamika.library.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    @Query("SELECT b FROM Borrowing b WHERE b.returnDate IS NULL")
    List<Borrowing> findAllActiveBorrowings();
}
