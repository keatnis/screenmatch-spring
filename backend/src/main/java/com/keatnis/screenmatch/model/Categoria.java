package com.keatnis.screenmatch.model;

public enum Categoria {
    // lo que esta entre parentesis escomo viene del API
    ACCION("Action","Accion"),
    ROMANCE("Romance","Romance"),
    COMEDIA("Comedy","Comedia"),
    DRAMA("Drama","Drama"),
    CRIMEN("Crime","Crimen");

    private String categoriaOmdb;
    //agregamos las categorias en espaniol para que el usuario lo busque en espaniol
    private String categoriaEspaniol;

    Categoria(String categoriaOmdb,String  categoriaEspaniol) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEspaniol = categoriaEspaniol;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
    public static Categoria fromEspaniol(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEspaniol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
