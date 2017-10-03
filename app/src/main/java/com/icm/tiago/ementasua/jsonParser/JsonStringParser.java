package com.icm.tiago.ementasua.jsonParser;

import org.json.JSONException;

/**
 * Created by Tiago Almeida on 07/10/2016.
 */

/**
 * Interface que deve ser implementada por uma classe que se especialize no parsing de uma string Json para um dado Objeto
 * @param <T> objeto correspondente da string Json
 */
public interface JsonStringParser<T> {
    T parser(String jsonString) throws JSONException;
}
