package com.icm.tiago.ementasua.jsonParser;


import android.util.Log;


import org.json.JSONException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Tiago on 06/10/2016.
 */


/**
 * a utilização desta class deve ser feita de forma assincrona
 * @param <T> estrutura de dados para retorno do parsing da string json
 */
public class JsonParser<T> {

    /**
     * acede ao serviço e obtem a string json
     * @param stringUrl
     * @return
     */
    public String getJsonStringFromURL(String stringUrl) {
        //utilizado do exemplo da openWeather

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are available at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            URL url = new URL(stringUrl);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                forecastJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                forecastJsonStr = null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            forecastJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return forecastJsonStr;
    }

    /**
     * O parsing da string json é feito por uma class especifica que sabes como fazer o parsing da respetiva string
     *
     * @param jsonStringParser class responsabel pelo parsing
     * @param forecastJsonStr string json
     * @return array string com os dados no formato pretendido
     * @throws JSONException
     */
    public T parsingJsonString(JsonStringParser<T> jsonStringParser,String forecastJsonStr) throws JSONException {
        return jsonStringParser.parser(forecastJsonStr);
    }

}
