package com.mcode.tempsigna;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcode.temperaturasigna.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by MH on 06/10/2015.
 */
public class AdapterCentros extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Centro> items;
    protected String temp;

    public AdapterCentros(Activity activity, ArrayList<Centro> items) {
        this.activity = activity;
        this.items = items;


    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getIdCentro();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemlist, null);
        }

        Centro centro = items.get(position);

        TextView nombrecentro = (TextView) v.findViewById(R.id.CentroTxt);
        nombrecentro.setText(centro.getNombreCentro());

        TextView datoscentro = (TextView) v.findViewById(R.id.DatosCentroTxt);
        datoscentro.setText(centro.getTemperatura_s1());
        TextView datoscentro_2 = (TextView) v.findViewById(R.id.DatosCentroTxt2);
        datoscentro_2.setText(centro.getTemperatura_s2());

        TextView fechacentro = (TextView) v.findViewById(R.id.DatosFechaTxt);

        fechacentro.setText(centro.getFecha());
         temp= new String(" ");

        TextView energiaTextView = (TextView) v.findViewById(R.id.energia);

        temp = temp.concat( "Energia:  " + getState(Integer.parseInt(centro.energia)));
        energiaTextView.setText(temp);
        TextView bombaTextView = (TextView) v.findViewById(R.id.bomba);
        temp="Bomba: " +  getState(Integer.parseInt(centro.bomba));
        bombaTextView.setText(temp);
        TextView compTextView = (TextView) v.findViewById(R.id.compresor);
        temp="Compresor: "+   getState(Integer.parseInt(centro.compresor));
        compTextView.setText(temp);

        ImageView btn = (ImageView) v.findViewById(R.id.imageView2);
        if (centro.control == 1) {

            btn.setImageResource(R.drawable.projo);

        } else if (centro.control == 0) {
            btn.setImageResource(R.drawable.pverde);
        }

        return v;
    }

    private String getState (int ST)
    {
        switch (ST) {
            case 1:
                return "OK";


            default:
                return "N/A";

        }
    }

}
