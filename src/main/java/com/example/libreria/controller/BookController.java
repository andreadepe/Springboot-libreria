package com.example.libreria.controller;

import com.example.libreria.model.Book;
import com.example.libreria.model.User;
import com.example.libreria.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/addbook")
    public String addBook(Book book, HttpSession session){

        if(session.getAttribute("loggedUser")==null){
            return "redirect:/login";
        }

        return "addBook";
    }

    @PostMapping("/addbook")
    public String checkAddBook(@Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "addBook";
        bookRepository.save(book);
        return "redirect:/book/"+book.getId();
    }

    @GetMapping("/book/{id}")
    public ModelAndView book(@PathVariable("id") int id, HttpSession session){
        if(session.getAttribute("loggedUser")==null){
            ModelAndView modelAndView =  new ModelAndView("redirect:/login");
            return modelAndView;
        }

        Book book = bookRepository.findById(id);

        ModelAndView modelAndView = new ModelAndView();

        if (book != null){
            modelAndView.setViewName("book");
            modelAndView.addObject("book", book);
            return modelAndView;
        }

        return null;
    }

    @GetMapping("/books")
    public ModelAndView books(HttpSession session){
        if(session.getAttribute("loggedUser")==null){
            ModelAndView modelAndView =  new ModelAndView("redirect:/login");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("books");
        modelAndView.addObject("books", bookRepository.findAll());
        return modelAndView;
    }
}
