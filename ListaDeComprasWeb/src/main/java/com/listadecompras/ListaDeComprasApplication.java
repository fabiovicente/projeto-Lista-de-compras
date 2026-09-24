package com.listadecompras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação Spring Boot.
 *
 * @SpringBootApplication é uma anotação "atalho" que ativa três coisas:
 *   1. @Configuration       → esta classe pode definir beans Spring
 *   2. @EnableAutoConfiguration → Spring configura tudo automaticamente
 *   3. @ComponentScan       → Spring procura por @Controller, @Service, @Repository, etc.
 *                             no pacote atual e nos subpacotes
 *
 * Comparação com a versão console:
 * - Antes: public static void main() era onde todo o programa rodava.
 * - Agora: public static void main() apenas inicia o servidor Spring Boot.
 *          O servidor fica rodando e esperando por requisições HTTP.
 */
@SpringBootApplication
public class ListaDeComprasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListaDeComprasApplication.class, args);
    }
}
