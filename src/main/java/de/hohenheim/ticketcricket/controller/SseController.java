package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.config.SseEmitters;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {

    private static SseEmitters notificationEmitters = new SseEmitters();

    private static SseEmitters ticketEmitters = new SseEmitters();

    @GetMapping(path = "/sse/notification-emitter", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter notificationEmitter(){
        return notificationEmitters.add(new SseEmitter());
    }

    @GetMapping(path = "/sse/ticket-emitter", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter ticketEmitter(){
        return ticketEmitters.add(new SseEmitter());
    }

    public static SseEmitters getNotificationEmitters() {
        return notificationEmitters;
    }

    public static SseEmitters getTicketEmitters() {
        return ticketEmitters;
    }
}
