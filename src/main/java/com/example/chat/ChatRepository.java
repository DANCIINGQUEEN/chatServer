package com.example.chat;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import reactor.core.publisher.Flux;


public interface ChatRepository extends ReactiveMongoRepository<Chat, String>{
	
	@Tailable
	@Query("{ sender : ?0, receiver: ?1 }")
	Flux<Chat> mFindBySender(String sender, String receiver);		//flux (흐름) response를 계속 유지하면서 데이터 흘려보내기

	@Tailable
	@Query("{roomNum: ?0}")
	Flux<Chat>mFindByRoomNum(Integer roomNum);
}
