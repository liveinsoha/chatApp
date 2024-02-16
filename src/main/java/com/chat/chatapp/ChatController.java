package com.chat.chatapp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.awt.*;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatRepository chatRepository;

    /*
    SSE프로토콜 한번 응답하고 끝내는 게 아니라 reponse를 지속적으로 유지하고 흘려보낼 수 있다
    크롬에서 계속해서 뱅글뱅글 돌고있다. 계속해서 메시지를 받기 위해서이다.
    크롬에서 get을고 요청하지 않고 자바스크립트로 요청해보자
     */

    /*
    자바스크립트 요청을 허용하기 위해서 CrossOrigin 애노테이션을 붙인다.
     */
    @CrossOrigin //귓속말일 때 사용
    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
        log.info("getMsg 요청 들어온다");
        Flux<Chat> chatFlux = chatRepository.mFindBySender(sender, receiver).subscribeOn(Schedulers.boundedElastic());
        return chatFlux;
    }

    /*
    모노는 한 건 만 리턴, Flux는 계속해서 리턴
    save한 데이터를 보고싶어서 리턴해준다.
     */
    @CrossOrigin
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat) {
        log.info("chat 저장 = {}", chat);
        chat.setCreatedTime(LocalDateTime.now());
        Mono<Chat> save = chatRepository.save(chat);
        System.out.println("save = " + save);
        return save;
    }

    @CrossOrigin
    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> findByRoomNum(@PathVariable Integer roomNum) {
        log.info("/chat/roomNum/{roomNum} : {}", roomNum);
        Flux<Chat> chatFlux = chatRepository.mFindByRoomNum(roomNum)
                .subscribeOn(Schedulers.boundedElastic());

        log.info("chatFlux = {} ", chatFlux);

        return chatFlux;
    }


}
