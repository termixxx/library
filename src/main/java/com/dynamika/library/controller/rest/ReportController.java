package com.dynamika.library.controller.rest;

import com.dynamika.library.model.dto.BorrowingDto;
import com.dynamika.library.service.BorrowingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@AllArgsConstructor
public class ReportController {
    private final BorrowingService borrowingService;

    @GetMapping("/active-borrowings")
    public ResponseEntity<List<BorrowingDto>> getActiveBorrowingsReport() {
        List<BorrowingDto> borrowings = borrowingService.getActiveBorrowingsWithDetails();
        return ResponseEntity.ok(borrowings);
    }
}
