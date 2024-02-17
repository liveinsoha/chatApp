package com.chat.chatapp;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class homeController {

    @GetMapping
    public String home() {
        return "chatFront/chat";
    }
}
