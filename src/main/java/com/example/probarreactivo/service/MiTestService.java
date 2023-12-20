package com.example.probarreactivo.service;

import com.example.probarreactivo.dto.Metodo;
import com.example.probarreactivo.dto.MiTestObject;
import reactor.core.publisher.Mono;

public interface MiTestService {
    Mono<MiTestObject> getMiTestObject(String nombre, Metodo metodo);
}
