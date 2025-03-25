package com.dynamika.library.model.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BorrowingDto {
    private String clientFullName;
    private LocalDate clientBirthDate;
    private String bookTitle;
    private String bookAuthor;
    private String bookIsbn;
    private LocalDate borrowDate;
}