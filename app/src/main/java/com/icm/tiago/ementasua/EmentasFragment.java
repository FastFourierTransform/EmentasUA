package com.icm.tiago.ementasua;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icm.tiago.ementasua.dataAPI.MealCourse;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tiago Almeida on 10/10/2016.
 */

public class EmentasFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Debug_Main","OnCreateView ArticleFragment");
        return inflater.inflate(R.layout.ementas_view,container,false);

    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args =getArguments();


        if (args!=null) {
            int index = args.getInt("index");
            Log.d("Debug_Main","onStart load args "+index);
            updateView(index);
        }
    }

    // usado para manter a consistencia da ementa que está a ser vista entre os diferente layouts
    @Override
    public void onResume() {
        super.onResume();
        int position = ((MainActivity)getActivity()).getCurrentPosition();
        Log.d("Debug_Main","onResume "+position);
        if ( position!=-1 ){
            updateView(position);
        }
    }

    /**
     * altera o conteudo da TextView
     * @param position index da ementa a visualizar
     */
    public void updateView(int position) {
        TextView almocoJantar=(TextView) getView().findViewById(R.id.almocoJantar);
        TextView showDia=(TextView) getView().findViewById(R.id.showdia);
        TextView sopaDisplay=(TextView) getView().findViewById(R.id.sopadisplay);
        TextView pratoOne=(TextView) getView().findViewById(R.id.prato1);
        TextView pratoTwo=(TextView) getView().findViewById(R.id.prato2);

        //reset display
        almocoJantar.setText("");
        showDia.setText("");
        sopaDisplay.setText("");
        pratoOne.setText("");
        pratoTwo.setText("");

        List<MealCourse> pratos = MainActivity.listDailyMealCourse.get(position);
        //replace da informação caso exista
        SimpleDateFormat dateParser = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);

        showDia.setText(dateParser.format(MainActivity.cantinas.get(position).getDate()));
        almocoJantar.setText(MainActivity.cantinas.get(position).getDailyMeal());
        sopaDisplay.setText(pratos.get(0).getFoodOption());
        if (!pratos.get(1).getFoodOption().equals("Prato normal carne")) {
            pratoOne.setText(pratos.get(1).getFoodOption());
            if (!pratos.get(2).getFoodOption().equals("Prato normal peixe"))
                pratoTwo.setText(pratos.get(2).getFoodOption());
        }
        else if (!pratos.get(2).getFoodOption().equals("Prato normal peixe"))
            pratoOne.setText(pratos.get(2).getFoodOption());


    }
}
