package br.com.kafkaspring.controller;

import br.com.kafkaspring.producer.PeopleProducer;
import br.com.springkafka.People;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/peoples")
@AllArgsConstructor
public class PeopleController {

    @Autowired
    private final PeopleProducer peopleProducer;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendMessage(@RequestBody PeopleDto peopleDto){

        var id = UUID.randomUUID().toString();

        var message   = People.newBuilder().
                setId(id)
                .setName(peopleDto.getName())
                .setCpf(peopleDto.getCpf())
                .setBooks(peopleDto.getBooks().stream().map(p -> (CharSequence) p).collect(Collectors.toList()))
                .build();

        peopleProducer.sendMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
