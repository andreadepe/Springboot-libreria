package com.example.libreria.controller;

import com.example.libreria.model.Credentials;
import com.example.libreria.model.User;
import com.example.libreria.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/signup")
    public String signup(User user){
        return "signup";
    }

    @PostMapping("/signup")
    public String checkSignup(@Valid User user, BindingResult bindingResult, Model model, HttpSession session){
        if(bindingResult.hasErrors())
            return "signup";
        User checkUser = userRepository.findByUsername(user.getCredentials().getUsername());
        if(checkUser != null){
            return "redirect:/login";
        }
        userRepository.save(user);
        session.setAttribute("loggedUser", user);
        return "redirect:/user/"+user.getId();
    }

    @GetMapping("/login")
    public String login(Credentials credentials){
        return "login";
    }

    @PostMapping("/login")
    public String checkLogin(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session){
        User user = userRepository.login(username, password);
        if(user == null)
            return "redirect:/login";
        session.setAttribute("loggedUser", user);
        return "redirect:/user/"+user.getId();
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.setAttribute("loggedUser", null);

        return "redirect:/login";
    }

    @GetMapping("/user/{id}")
    public ModelAndView user(@PathVariable("id") int id){
        User user = userRepository.findById(id);

        ModelAndView modelAndView = new ModelAndView();

        if (user != null){
            modelAndView.setViewName("user");
            modelAndView.addObject("user", user);
            return modelAndView;
        }

        return null;
    }

}
