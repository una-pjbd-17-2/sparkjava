package br.una.sparkjava;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.regex;
import static spark.Spark.get;

public class HelloSpark {
    public static void main(String[] args) {

        //conexão com o mongo
        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
        //escolhendo o banco de dados
        MongoDatabase database = mongoClient.getDatabase("local");

        //buscando uma coleção
        MongoCollection<Document> filmes = database.getCollection("filmes");

        //Caso o banco esteja vazio insere alguns registros
        if (filmes.count() == 0) {
            filmes.insertOne(criaFilme("Triplo X"));
            filmes.insertOne(criaFilme("Matrix"));
            filmes.insertOne(criaFilme("Entrando numa fria"));
        }
git
        get("/filmes", (req, res) -> listaNomeFilmes(filmes));

        get("/filme/:nome", (request, response) -> buscaFilme(request.params(":nome"), filmes));

    }

    private static List<String> buscaFilme(String nome, MongoCollection<Document> filmes) {
        Pattern pattern = Pattern.compile(".*" + Pattern.quote(nome) + ".*", Pattern.CASE_INSENSITIVE);
        return formataFilmes(filmes.find(regex("nome", pattern)));
    }

    private static List<String> formataFilmes(FindIterable<Document> filmes) {
        List<String> nomeDosFilmes = new ArrayList<>();
        for (Document filme : filmes) {
            nomeDosFilmes.add(filme.getString("nome"));
        }
        return nomeDosFilmes;
    }

    private static Document criaFilme(String titulo) {
        Document filme = new Document("nome", titulo);
        filme.append("data_cadastro", new Date());
        return filme;

    }

    private static List<String> listaNomeFilmes(MongoCollection<Document> filmes) {
        List<String> nomeDosFilmes = new ArrayList<>();
        for (Document filme : filmes.find()) {
            nomeDosFilmes.add(filme.getString("nome"));
        }
        return nomeDosFilmes;
    }
}
