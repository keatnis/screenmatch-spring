package com.keatnis.screenmatch.service;

public interface ICovierteDatos {
    // metodos genericos

    <T> T obtenerDatos(String json, Class<T> tClass);

}
