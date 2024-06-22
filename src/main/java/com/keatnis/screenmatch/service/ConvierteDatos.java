package com.keatnis.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements ICovierteDatos{
    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public <T> T obtenerDatos(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json,tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
