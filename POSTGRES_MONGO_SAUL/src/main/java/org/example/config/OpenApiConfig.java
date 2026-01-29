package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // INDICA QUE ESTA CLASE CONTIENE CONFIGURACIONES DE BEANS QUE SPRING DEBE CARGAR EN EL CONTEXTO AL ARRANCAR LA APLICACIÓN
public class OpenApiConfig {

    @Bean // DEFINE QUE EL MÉTODO RETORNA UN OBJETO QUE SERÁ GESTIONADO POR SPRING; EN ESTE CASO, LA CONFIGURACIÓN DE LA DOCUMENTACIÓN SWAGGER
    public OpenAPI customOpenAPI() {
        return new OpenAPI() // INSTANCIA EL OBJETO PRINCIPAL DE OPENAPI QUE CONSTRUIRÁ LA INTERFAZ VISUAL DE NUESTROS ENDPOINTS
                .info(new Info() // DEFINE LA SECCIÓN DE INFORMACIÓN GENERAL QUE APARECERÁ EN LA CABECERA DE LA PÁGINA DE DOCUMENTACIÓN
                        .title("API de Películas y Actores")
                        .version("1.0")
                        .description("API para gestionar películas y actores"));
    }
}