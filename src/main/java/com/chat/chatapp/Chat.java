package com.chat.chatapp;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Data
@Document
public class Chat {

    @Id
    private String id;
    private String msg;
    private String sender;
    private String receiver;
    private Integer roomNum;

    private LocalDateTime createdTime;
}
