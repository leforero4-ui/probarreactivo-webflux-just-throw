package com.example.probarreactivo.service;

import com.example.probarreactivo.dto.Metodo;
import com.example.probarreactivo.dto.MiTestObject;
import com.example.probarreactivo.utility.MiTestUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MiTestServiceImpl implements MiTestService {
    private final MiTestUtility miTestUtility;

    @Override
    public Mono<MiTestObject> getMiTestObject(String nombre, Metodo metodo) {
        return miTestUtility.getMiTestObject(nombre, metodo)
                .doFirst(() -> log.info("entró MiTestService " + nombre + " " + metodo))
                .doOnError(throwable -> log.error("falló MiTestService " + nombre + " " + metodo));
    }
}
