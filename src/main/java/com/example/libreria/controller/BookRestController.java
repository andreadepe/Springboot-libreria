package com.example.libreria.controller;

import com.example.libreria.model.Book;
import com.example.libreria.repository.BookRepository;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
public class BookRestController {

    @Autowired
    BookRepository bookRepository;

    @PostMapping("/books")
    public Iterable<Book> books(){
        return bookRepository.findAll();
    }

    @PostMapping("/book")
    public Iterable<Book> book(@RequestParam("title") String title){
        return bookRepository.findByTitle(title);
    }

    @PostMapping("/bookcontains")
    public Iterable<Book> bookContains(@RequestParam("title") String title){
        return bookRepository.findByTitleLike("%" + title + "%");
    }

    @PostMapping("/sync")
    public String sync(@RequestParam("title")String title){
        RestTemplate restTemplate = new RestTemplate();
        String urlAPI = "https://www.googleapis.com/books/v1/volumes?q=" + URLEncoder.encode(title, StandardCharsets.UTF_8);
        ResponseEntity<String> response
                = restTemplate.getForEntity(urlAPI, String.class);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(response.getBody()).getAsJsonObject();

        JsonArray items = jsonObject.getAsJsonArray("items");
        if(items!= null){
            for(JsonElement item : items){
                JsonObject itemObj = item.getAsJsonObject();
                JsonObject volume = itemObj.getAsJsonObject("volumeInfo");
                JsonPrimitive titleObj = volume.getAsJsonPrimitive("title");
                JsonArray authorsObj = volume.getAsJsonArray("authors");
                String author = "";
                if(authorsObj != null) {
                    for(JsonElement authorObj : authorsObj){
                        if(!author.equals(""))
                            author += ", ";
                        author += authorObj.getAsJsonPrimitive().getAsString();
                    }
                }
                JsonPrimitive dateObj = volume.getAsJsonPrimitive("publishedDate");
                int year = 0;
                if(dateObj != null){
                    try{
                        LocalDate date = LocalDate.parse(dateObj.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
                        year = date.getYear();
                    } catch (DateTimeParseException e){
                        try{
                            YearMonth date = YearMonth.parse(dateObj.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM"));
                            year = date.getYear();
                        } catch(DateTimeParseException ex){
                            try{
                                ZonedDateTime date = ZonedDateTime.parse(dateObj.getAsString());
                                year = date.getYear();
                            } catch(DateTimeException exc){
                                year = dateObj.getAsInt();
                            }
                        }

                    }
                }
                JsonObject saleInfo = itemObj.getAsJsonObject("saleInfo");
                JsonPrimitive isOnSale = saleInfo.getAsJsonPrimitive("saleability");
                Double priceObj = 0.00;
                if(isOnSale.getAsString().equals("FOR_SALE"))
                    priceObj = saleInfo.getAsJsonObject("listPrice").getAsJsonPrimitive("amount").getAsDouble();
                Book book = new Book(titleObj.getAsString(), author ,year ,priceObj);
                bookRepository.save(book);
            }
        }


        return "OK";
    }
}
