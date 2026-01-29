package org.example;

import jakarta.annotation.PostConstruct;
import org.example.service.Secuencia;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    private final Secuencia secuencia;

    // INYECCION DE DEPENDENCIAS POR CONSTRUCTOR
    public Main(Secuencia secuencia) {
        this.secuencia = secuencia;
    }

    // SE EJECUTA AUTOMATICAMENTE AL LEVANTAR SPRING
    @PostConstruct
    public void executar() {
        secuencia.executar();
        System.out.println("FINALIZANDO APLICACION CON EXIT CODE 200");
        System.exit(200);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}