package com.example.probarreactivo.utility;

import com.example.probarreactivo.dto.Metodo;
import com.example.probarreactivo.dto.MiTestObject;
import reactor.core.publisher.Mono;

public interface MiTestUtility {
    Mono<MiTestObject> getMiTestObject(String nombre, Metodo metodo);
}
