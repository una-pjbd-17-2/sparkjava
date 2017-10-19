package br.una.sparkjava;

import static spark.Spark.*;

public class HelloSpark {
    public static void main(String[] args) {

        get("/hello", (req, res) -> "Hello World");

        
    }
}
