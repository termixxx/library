package com.dynamika.library.controller;

import com.dynamika.library.service.BookService;
import com.dynamika.library.service.ClientService;
import com.dynamika.library.service.BorrowingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class MainController {
    private final BookService bookService;
    private final ClientService clientService;
    private final BorrowingService borrowingService;

    @GetMapping("/")
    public String showDashboard(Model model) {
        model.addAttribute("booksCount", bookService.getAllBooks().size());
        model.addAttribute("clientsCount", clientService.getAllClients().size());
        model.addAttribute("borrowingsCount", borrowingService.getActiveBorrowings().size());
        return "index";
    }
}