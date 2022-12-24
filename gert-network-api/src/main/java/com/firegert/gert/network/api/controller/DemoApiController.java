package com.firegert.gert.network.api.controller;

import com.sun.source.doctree.SeeTree;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/demos")
public class DemoApiController {

    private static final Set<Demo> ALL_DEMOS;

    static {
        ALL_DEMOS = new HashSet<>();
        ALL_DEMOS.add(Demo.builder().id(1L).name("A").build());
        ALL_DEMOS.add(Demo.builder().id(2L).name("B").build());
    }

    @GetMapping("/{id}")
    public Mono<Demo> fetchDemo(@PathVariable Long id) {
        return Mono.justOrEmpty(ALL_DEMOS.stream().findFirst());
    }

    @GetMapping
    public Flux<Demo> allDemos() {
        log.info("Read all demos...");
        return Flux.fromIterable(ALL_DEMOS);
    }
}

@Data
@Builder
class Demo {
    private Long id;
    private String name;
}
