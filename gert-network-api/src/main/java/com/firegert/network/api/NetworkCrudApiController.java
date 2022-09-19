package com.firegert.network.api;

import com.firegert.network.data.entity.Network;
import com.firegert.network.service.NetworkCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/networks")
public class NetworkCrudApiController {

    private final NetworkCrudService networkCrudService;

    @PostMapping("/{name}")
    public Network create(@PathVariable String name) {
        return networkCrudService.createNetwork(name);
    }

    @GetMapping("/{name}")
    public Network fetchByName(@PathVariable String name) {
        return networkCrudService.findByName(name);
    }
}
