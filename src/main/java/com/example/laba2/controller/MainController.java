package com.example.laba2.controller;

import com.example.laba2.domain.User;
import com.example.laba2.repository.MessageRepository;
import com.example.laba2.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user, Map<String, Object> model) {
        if (user != null) {
            user.setDateLogin(new SimpleDateFormat().format(new Date()));

            messageRepository.save(user);

            model.put("userInfo", user.getUsername());
        }

        Iterable<User> messages = messageRepository.findAll();

        model.put("messages", messages);

        return "index";
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateTable(@RequestBody List<Long> usersId, @RequestParam("state") Boolean state) {

        if (state) {
            if (userService.lockUsers(usersId)) {
                SecurityContextHolder.clearContext();
                return "{\"success\":false}";
            }
        } else {
            userService.unlockUsers(usersId);
        }

        return "{\"success\":true}";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String deleteTable(@RequestBody List<Long> usersId) {

        if (userService.deleteUsers(usersId)) {
            SecurityContextHolder.clearContext();
            return "{\"success\":false}";
        }

        return "{\"success\":true}";
    }
}