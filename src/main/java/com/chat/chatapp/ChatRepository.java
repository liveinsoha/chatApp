package com.chat.chatapp;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

    /*
    Tailable이 걸리면 버퍼사이즈가 커야한다.  -> 버퍼 사이즈 수정해주자
     */
    @Tailable
    @Query("{sender : ?0, receiver: ?1}")
    Flux<Chat> mFindBySender(String sender, String receiver);
    /*
    Flux는 흐름이다. 계속해서 데이터를 받겠다는 의미이다.response를 유지하면서 데이터를 계속 흘려보내기 기능
     */

    @Tailable
    @Query("{roomNum: ?0}")
    Flux<Chat> mFindByRoomNum(Integer roomNum);
}
