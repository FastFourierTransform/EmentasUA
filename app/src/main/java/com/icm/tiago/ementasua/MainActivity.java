package com.icm.tiago.ementasua;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.icm.tiago.ementasua.dataAPI.DailyOption;
import com.icm.tiago.ementasua.dataAPI.MealCourse;
import com.icm.tiago.ementasua.dataAPI.UAMenus;
import com.icm.tiago.ementasua.jsonParser.JsonParser;
import com.icm.tiago.ementasua.jsonParser.JsonStringUAMenusParser;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements CantinasFragment.OnTitlesSelectedListener {
    /**
     * variavel responsável por guardar o index atual da ementa a ver
     * permite manter a consistencia da ementa visualizada entre os 2 layouts
     */
    private int currentPosition=-1;

    /**
     * variavel responsável por fazer o tracking do layout
     */
    private boolean dualMode=false;

    /**
     * variavel contem a lista de cantinas abertas
     */
    public static List<DailyOption> cantinas = new ArrayList<>();
    /**
     * ementas das respetivas cantinas
     */
    public static List<List<MealCourse>> listDailyMealCourse=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * recupero estado (index) da ementa que estáva a ver
         */
        if (savedInstanceState!=null) {
            currentPosition = savedInstanceState.getInt("estado");
            Log.d("Debug_Main","Load estado "+currentPosition);
        }
        //se fragment não existir estou no layout-w600dp senão estou no default
        dualMode = findViewById(R.id.fragment_Layout)==null;

        Log.d("Debug_Main","dualMode "+dualMode);
        Log.d("Debug_Main","Main currentPosition "+currentPosition);
        if (!dualMode)
        {
            //adicionar o fragment se ele não existe
            Log.d("Debug_Main","fragment portrant na Main "+getFragmentManager().findFragmentById(R.id.fragment_Layout));
            if (getFragmentManager().findFragmentById(R.id.fragment_Layout)==null)
            {
                CantinasFragment fragment = new CantinasFragment();
                getFragmentManager().beginTransaction().add(R.id.fragment_Layout,fragment).commit();
            }
        }

        //caso tenha conexão há internet executo o pedido, e enquanto isso uso a informação anterior
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            new FetchEmentasTask().execute("http://services.web.ua.pt/sas/ementas?date=week&format=json");
        }


    }

    /**
     * função chamada pelos fragments
     * @return index atual da ementa que está a ser visto
     */
    public int getCurrentPosition()
    {
        return currentPosition;
    }

    /**
     * guarda o estado atual (index) da ementa que está a ser vista
     * @param outState estado atual (index) da ementa que está a ser vista
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        //guarda o estado da aplicação
        Log.d("Debug_Main","Save estado "+currentPosition);
        outState.putInt("estado",currentPosition);

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onTitleSelected(int position) {
        //atualizo o meu index que estou a ver
        currentPosition=position;

        if (dualMode)
        {
            // se estiver no 2 planos apanho o fragment responsável pela visualização e chamo metodo para alterar a ementa atraves do index
            EmentasFragment articleFrag = (EmentasFragment) getFragmentManager().findFragmentById(R.id.ementas_fragment);
            Log.d("Debug_Main","AriticlesFragment "+articleFrag);
            articleFrag.updateView(position);


        }else{
            // estou em um só plano
            EmentasFragment newFragment = new EmentasFragment();
            Bundle args = new Bundle();
            args.putInt("index", position);
            //passo a index em que o ementa deve mostrar
            newFragment.setArguments(args);
            //substituio o fragment atual, pelo novo (ementa) e guardo no backStack para poder voltar atras
            getFragmentManager().beginTransaction().replace(R.id.fragment_Layout, newFragment).addToBackStack(null).commit();
        }
    }

    private class FetchEmentasTask extends AsyncTask<String, Void, UAMenus> {

        protected UAMenus doInBackground(String... params)
        {
            Log.d("Debug_Main","Inicio da tarefa assincrona");
            JsonParser<UAMenus> jParser = new JsonParser<>();

            String json = jParser.getJsonStringFromURL(params[0]);

            UAMenus jsonParsed=null;
            try {
                if (json!=null)
                    jsonParsed = jParser.parsingJsonString(new JsonStringUAMenusParser(),json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonParsed;
        }

        protected void onPostExecute(UAMenus result)
        {
            if (result!=null)
            {
                cantinas.clear();
                listDailyMealCourse.clear();
                for (int i=0;i<result.getDailyMenus().size();i++) {
                    if (result.getDailyMenus().get(i).isAvailable()) {
                        cantinas.add(result.getDailyMenus().get(i));
                        listDailyMealCourse.add(result.getDailyMenus().get(i).getMealCourseList());
                    }
                }
                if (dualMode)
                    ((CantinasFragment)getFragmentManager().findFragmentById(R.id.cantinas_fragment)).updateList(cantinas);
                else if (getFragmentManager().findFragmentById(R.id.fragment_Layout) instanceof CantinasFragment)
                    ((CantinasFragment)getFragmentManager().findFragmentById(R.id.fragment_Layout)).updateList(cantinas);
            }
            Log.d("Debug_Main","Fim da tarefa assincrona");
        }
    }

}
