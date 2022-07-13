package de.hohenheim.ticketcricket.config;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SseEmitters {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);

        emitter.onCompletion(() -> {
            this.emitters.remove(emitter);
        });
        emitter.onTimeout(() -> {
            emitter.complete();
            this.emitters.remove(emitter);
        });

        return emitter;
    }

    void send(Object obj) {
        List<SseEmitter> failedEmitters = new ArrayList<>();

        this.emitters.forEach(emitter -> {
            try {
                emitter.send(obj);
            } catch (Exception e) {
                emitter.completeWithError(e);
                failedEmitters.add(emitter);
            }
        });

        this.emitters.removeAll(failedEmitters);
    }

    public List<SseEmitter> getEmitters() {
        return emitters;
    }
}
