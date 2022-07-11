package com.example.chat;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@RestController
public class ChatController {

	private final ChatRepository chatRepository;

	//귓속말
	@CrossOrigin
	@GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
		System.out.println("msg 호출됨");
		return chatRepository.mFindBySender(sender, receiver)
				//.repeatWhen(flux -> flux.delayElements(Duration.ofSeconds(1)))
				.subscribeOn(Schedulers.boundedElastic());
	}

	//채팅룸
	@CrossOrigin
	@GetMapping(value = "chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Chat> findByRoomNum(@PathVariable Integer roomNum){
		return chatRepository.mFindByRoomNum(roomNum)
				.subscribeOn(Schedulers.boundedElastic());
	}

	@CrossOrigin
	@PostMapping("/chat")
	public Mono<Chat> setMsg(@RequestBody Chat chat){
		chat.setCreatedAt(LocalDateTime.now());
		return chatRepository.save(chat);	//object를 리턴하면 자동으로 JSON 변환	 (messageConverter)
	}
}
