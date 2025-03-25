package com.dynamika.library.controller;

import com.dynamika.library.service.BookService;
import com.dynamika.library.service.BorrowingService;
import com.dynamika.library.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/web")
public class WebController {
    private final BookService bookService;
    private final ClientService clientService;
    private final BorrowingService borrowingService;

    @GetMapping("/books")
    public String getBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    @GetMapping("/clients")
    public String getClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "clients";
    }

    @GetMapping("/borrowings")
    public String showBorrowingsPage(Model model) {
        model.addAttribute("borrowings", borrowingService.getActiveBorrowings());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("books", bookService.getAllBooks());
        return "borrowings";
    }
}
