package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.config.SseEmitters;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {

    private static SseEmitters sseEmitters = new SseEmitters();

    @GetMapping(path = "/sse/emitter", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public SseEmitter eventEmitter(){
        return sseEmitters.add(new SseEmitter());
    }

    public static SseEmitters getSseEmitters() {
        return sseEmitters;
    }
}
