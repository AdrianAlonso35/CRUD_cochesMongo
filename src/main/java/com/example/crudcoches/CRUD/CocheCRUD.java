package com.example.crudcoches.CRUD;

import com.example.crudcoches.clases.Coche;
import com.example.crudcoches.utils.ConnectionDB;
import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class CocheCRUD {
    MongoCollection<Document> collection = null;

    String json;
    Document doc;

    public int insertarCoche(Coche coche) {
        Gson gson = new Gson();
        int num = 0;
        if (coche != null) {
            json = gson.toJson(coche);
            doc = Document.parse(json);
            collection.insertOne(doc);
            num = 1;
        }
        return num;
    }


    public void crearBaseDatos(){
        MongoClient con = ConnectionDB.conectar();
        if (con != null) {
            MongoDatabase database = con.getDatabase("Concesionario");
            try {
                database.createCollection("Coches");
                collection = database.getCollection("Coches");
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public boolean comprobarMatricula(String matricula) {
        ArrayList<Coche>lista = getCoches();
        for (Coche coche1:lista){
            if (coche1.getMatricula().equals(matricula)){
                return true;
            }
        }
        return false ;
    }

    public ArrayList<Coche> getCoches() {
        ArrayList<Coche> coches = new ArrayList<>();
        Gson gson = new Gson();
        for (Document doc : collection.find()) {
            Coche coche = gson.fromJson(doc.toJson(), Coche.class);
            coches.add(coche);
        }
        return coches;
    }

    public boolean eliminarCoche(Coche coche) {
        long eliminar=collection.deleteOne(new Document("matricula", coche.getMatricula())).getDeletedCount();
        return eliminar>0;
    }

    public void editarCoche(Coche coche) {
        Document doc = new Document("marca", coche.getMarca())
                .append("modelo", coche.getModelo())
                .append("tipo", coche.getTipo());
        collection.updateOne(Filters.eq("matricula", coche.getMatricula()), new Document("$set", doc));
    }
}
