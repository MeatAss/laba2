package com.example.laba2.controller;

import com.example.laba2.domain.Role;
import com.example.laba2.domain.User;
import com.example.laba2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration(Map<String, Object> model){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){
        User userDB = userRepository.findByUsername(user.getUsername());

        if (userDB != null)
        {
            model.put("message", "User exist");
            return "registration";
        }

        user.unlock();
        user.setDateRegistration(new SimpleDateFormat().format(new Date()));
        user.setDateLogin(user.getDateRegistration());

        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);

        return "redirect:/login";
    }
}
