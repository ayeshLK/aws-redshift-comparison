package io.sample.redshift.dataapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(
            path = {"hello"}
    )
    public Mono<String> hello() {
        return Mono.just("Hello, World..!");
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<List<User>> listUsers() {
        return Flux.range(1, 20)
                .parallel(20)
                .runOn(Schedulers.boundedElastic())
                .flatMap(e -> Flux.fromIterable(this.userRepository.listUsers()))
                .sequential()
                .collectList();
    }
}
