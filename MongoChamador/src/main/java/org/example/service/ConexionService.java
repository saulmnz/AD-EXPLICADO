package org.example.service;

import org.example.model.Peliculas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class ConexionService {

    @Autowired
    private RestTemplate restTemplate;

    // CONSTANTES CON LAS URLS DEL OTRO MICROSERVICIO (PUERTO 8085)
    private static final String URL_BASE = "http://localhost:8085/postgres/peliculas";

    // OBTENER UNA PELICULA POR ID (GET)
    public Peliculas getPeliculasById(Long id) {
        try {
            String url = URL_BASE + "/" + id;
            ResponseEntity<Peliculas> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, Peliculas.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("ERROR CONECTANDO A POSTGRES (ID " + id + "): " + e.getMessage());
            return null;
        }
    }

    // OBTENER PELICULAS POR TITULO (GET) - DEVUELVE LISTA
    public List<Peliculas> getPeliculasByTitulo(String titulo) {
        try {
            String url = URL_BASE + "/titulo/" + titulo;
            ResponseEntity<List<Peliculas>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Peliculas>>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("ERROR CONECTANDO A POSTGRES (TITULO " + titulo + "): " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // CREAR UNA PELICULA NUEVA EN POSTGRES (POST)
    public Peliculas createPeliculas(Peliculas pelicula) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Peliculas> request = new HttpEntity<>(pelicula, headers);

            ResponseEntity<Peliculas> response = restTemplate.exchange(
                    URL_BASE, HttpMethod.POST, request, Peliculas.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("ERROR AL CREAR EN POSTGRES: " + e.getMessage());
            return null;
        }
    }
}