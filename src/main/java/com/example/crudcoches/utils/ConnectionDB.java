package com.example.crudcoches.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.io.FileInputStream;
import java.lang.module.Configuration;
import java.util.Properties;

public class ConnectionDB {

    public static MongoClient conectar() {
        Properties configuration= new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/configuration/database.properties");
            configuration.load(fis);
            String username = configuration.getProperty("username");
            String password = configuration.getProperty("password");
            String host = configuration.getProperty("host");
            String port = configuration.getProperty("port");
            String author = configuration.getProperty("author");

            MongoClient conexion = MongoClients.create("mongodb://" + username + ":" + password + "@" + host + ":" + port + "/?authSource=" + author);
            System.out.println("Conectado");
            return conexion;
        } catch (Exception e) {
            System.out.println("Conexión Fallida");
            System.out.println(e);
            return null;
        }
    }

    public static void desconectar(MongoClient con) {
        if (con != null) {
            con.close();
            System.out.println("Conexión cerrada");
        }
    }
}

