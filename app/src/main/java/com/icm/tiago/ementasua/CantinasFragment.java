package com.icm.tiago.ementasua;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.icm.tiago.ementasua.dataAPI.DailyOption;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tiago Almeida on 10/10/2016.
 */

public class CantinasFragment extends ListFragment {

    private OnTitlesSelectedListener mCallback;

    /**
     * interface que é implementa pela class Main para poder ser notificada de quando a lista for usada
     */
    public interface OnTitlesSelectedListener{
        void onTitleSelected(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnTitlesSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1));
    }

    @Override
    public void onStart() {
        super.onStart();
        //inserçao dos valores na lista
        if (!MainActivity.cantinas.isEmpty())
            updateList(MainActivity.cantinas);
        // apesar de no modo de 1 plano não fazer muito sentido deixei porque da ao utilizador a informação da ultima ementa que visitou
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        int position = ((MainActivity)getActivity()).getCurrentPosition();
        if (position!=-1)
            getListView().setItemChecked(position, true); //marca visivel a opção da lista

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.onTitleSelected(position); //chama o metodo que está implementa na main com a logica
        getListView().setItemChecked(position, true);//marca visivel a opção da lista
    }


    public void updateList(List<DailyOption> optionsList) {
        SimpleDateFormat dateParser = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);
        ((ArrayAdapter<String>) getListView().getAdapter()).clear();
        //.getCanteenSite() + "\n" + dateParser.format(result.getDailyMenus().get(i).getDate())
        String temp;
        for (DailyOption dOp:optionsList) {
            temp=dOp.getCanteenSite()+ " - "+dOp.getDailyMeal()+" - " + "\n" + dateParser.format(dOp.getDate());
            ((ArrayAdapter<String>) getListView().getAdapter()).add(temp);
        }
    }
}
