package com.example.probarreactivo.controller;

import com.example.probarreactivo.dto.Metodo;
import com.example.probarreactivo.dto.MiTestObject;
import com.example.probarreactivo.service.MiTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MiTestController {
    private final MiTestService miTestService;
    
    @GetMapping("test")
    public Mono<MiTestObject> getMiTestObject(@RequestParam String nombre, @RequestParam Metodo metodo) {
        return miTestService.getMiTestObject(nombre, metodo)
                .doFirst(() -> log.info("entró MiTestController " + nombre + " " + metodo))
                .doOnError(throwable -> log.error("falló MiTestController " + nombre + " " + metodo));
    }
}
