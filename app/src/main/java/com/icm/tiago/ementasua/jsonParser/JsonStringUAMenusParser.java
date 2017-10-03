package com.icm.tiago.ementasua.jsonParser;

import android.text.format.Time;
import android.util.Log;

import com.icm.tiago.ementasua.dataAPI.DailyOption;
import com.icm.tiago.ementasua.dataAPI.MealCourse;
import com.icm.tiago.ementasua.dataAPI.UAMenus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Tiago on 07/10/2016.
 */

public class JsonStringUAMenusParser implements JsonStringParser<UAMenus> {

    /**
     * logica para a transformação da string Json em um objeto do tipo UAMenus
     * codigo retirado do repositorio do professor
     * @param jsonString jsonString obtida por um url eventualmente
     * @return Objeto resoltante do parsing da string jason
     * @throws JSONException
     */
    @Override
    public UAMenus parser(String jsonString) throws JSONException {
        UAMenus menusToReturn = new UAMenus();  // return object

        JSONArray menuOptionsArray, jsonArray2;
        JSONObject workingJsonObject;
        DailyOption dailyOption;
        SimpleDateFormat dateParser = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);

        try {
            workingJsonObject = new JSONObject(jsonString);    // access root object
            workingJsonObject = workingJsonObject.getJSONObject("menus"); // access menus within root
            menuOptionsArray = workingJsonObject.getJSONArray("menu"); // options for a zone

            // go through the several entries (day,canteen)
            for (int dailyEntry = 0; dailyEntry < menuOptionsArray.length(); dailyEntry++) {
                //String[] whiteSpaceDate = menuOptionsArray.getJSONObject(dailyEntry).getJSONObject("@attributes").getString("date").split(" ");

                //StringBuilder sb = new StringBuilder();
                //for (int i=0;i<N;i++) {
                //    sb.append(whiteSpaceDate[i] + " ");
                //}
                //String data = sb.toString();
                //data = data.substring(0,data.length()-1);
                //Log.i("EMENTAS",data);
                dailyOption = new DailyOption(
                        dateParser.parse(menuOptionsArray.getJSONObject(dailyEntry).getJSONObject("@attributes").getString("date")),
                        menuOptionsArray.getJSONObject(dailyEntry).getJSONObject("@attributes").getString("canteen"),
                        menuOptionsArray.getJSONObject(dailyEntry).getJSONObject("@attributes").getString("meal"),
                        menuOptionsArray.getJSONObject(dailyEntry).getJSONObject("@attributes").getString("disabled").compareTo("0") == 0);

                if (dailyOption.isAvailable()) {
                    jsonArray2 = menuOptionsArray.getJSONObject(dailyEntry).getJSONObject("items").getJSONArray("item");
                    // get the several meals in a canteen, in a day
                    dailyOption.addMealCourse(new MealCourse(UAMenus.COURSE_ORDER_SOUP, parseForObjectOrString(jsonArray2, UAMenus.COURSE_ORDER_SOUP) ));
                    dailyOption.addMealCourse(new MealCourse(UAMenus.COURSE_ORDER_MEAT_NRM, parseForObjectOrString(jsonArray2, UAMenus.COURSE_ORDER_MEAT_NRM) ));
                    dailyOption.addMealCourse(new MealCourse(UAMenus.COURSE_ORDER_FISH_NRM, parseForObjectOrString(jsonArray2, UAMenus.COURSE_ORDER_FISH_NRM) ));
                    //// TODO: complete with more meal courses
                }
                menusToReturn.add(dailyOption);
            }
        } catch (JSONException | ParseException ex) {
            ex.printStackTrace();
        }
        return menusToReturn;
    }


    /**
     * parses the meal course options; the JSON is not coherent, and can be an object or a string
     * @param array array with the meal options
     * @param index position to retrieve
     * @return the meal course option
     * @throws JSONException
     */

    private String parseForObjectOrString(JSONArray array, int index) throws JSONException {
        JSONObject tempJsonObject = array.optJSONObject(index);
        if( null == tempJsonObject ) {
            // no json object, treat as string
            return array.getString(index);
        } else {
            return array.getJSONObject(index).getJSONObject("@attributes").getString("name");
        }

    }
}
