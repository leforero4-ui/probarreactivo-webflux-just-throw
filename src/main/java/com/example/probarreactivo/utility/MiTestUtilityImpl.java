package com.example.probarreactivo.utility;

import com.example.probarreactivo.dto.Metodo;
import com.example.probarreactivo.dto.MiTestObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MiTestUtilityImpl implements MiTestUtility {
    private static final String FALLAR = "miFallaUtility";
    private final Map<String, MiTestObject> miTestObjectList = new HashMap<>();

    {
        miTestObjectList.put("ok", new MiTestObject("miOk", 1, true));
        miTestObjectList.put("falla", new MiTestObject(FALLAR, 1, true));
    }
    
    // la regla es que dentro del just no puede llegar un throw se soluciona con un defer,
    // pasa cuando se consumen servicios no reactivos que no usan webclient
    // después del just pueden ocurrir todos los errores throw y mono.error que quieras
    // dentro del just no puede ir mono entonces no va a llegar un mono.error
    @Override
    public Mono<MiTestObject> getMiTestObject(String nombre, Metodo metodo) {
        var metodoSwith = switch (metodo) {
            case A -> metodoA(nombre);
            case B -> metodoB(nombre);
            case C -> metodoC(nombre);
            case D -> metodoD(nombre);
            case E -> metodoE(nombre);
        };
        return metodoSwith
                .doOnError(throwable -> log.error("falló MiTestUtility " + nombre + " " + metodo));
    }


    //si falla dentro del just corta todo el flujo inclusive el onError
    private Mono<MiTestObject> metodoA(String nombre) {
        return Mono.just(getMiTestObjectPrivate(nombre))
                .doFirst(() -> log.info("entró MiTestUtility " + nombre));
    }

    //funciona con defer
    private Mono<MiTestObject> metodoB(String nombre) {
        return Mono.defer(() -> {
            log.info("entró MiTestUtility " + nombre);
            return Mono.just(getMiTestObjectPrivate(nombre));
        });
    }

    //si falla dentro del just pero lanza Mono.error si deja continuar el flujo
    private Mono<MiTestObject> metodoC(String nombre) {
        return Mono.just(this.miTestObjectList.get(nombre))
                .flatMap(miTestObject -> {
                    if(miTestObject.nombre().equalsIgnoreCase(FALLAR)) {
                        var a = 1/0;
                        return Mono.error(new NumberFormatException());
                    } else {
                        return Mono.just(miTestObject);
                    }
                });
    }

    //si no se lanza ningún throw y no es capturado en un just tampoco pasa nada
    private Mono<MiTestObject> metodoD(String nombre) {
        return Mono.just(this.miTestObjectList.get(nombre))
                .map(miTestObject -> {
                    if(miTestObject.nombre().equalsIgnoreCase(FALLAR)) {
                        throw new NumberFormatException();
                    } else {
                        return miTestObject;
                    }
                });
    }

    //si falla se lanza el throw dentro de un flujo no pasa nada
    private Mono<MiTestObject> metodoE(String nombre) {
        return Mono.just(this.miTestObjectList.get(nombre))
                .flatMap(miTestObject -> {
                    if(miTestObject.nombre().equalsIgnoreCase(FALLAR)) {
                        //var a = 1/0;
                        throw new NumberFormatException();
                    }
                    return Mono.just(miTestObject);
                });
    }
    
    
    private MiTestObject getMiTestObjectPrivate(String nombre) {
        var miTestObject = this.miTestObjectList.get(nombre);
        if(miTestObject.nombre().equalsIgnoreCase(FALLAR)) {
            var a = 1/0;
            //throw new NumberFormatException();
            return miTestObject;
        } else {
            return miTestObject;
        }
    }
}
