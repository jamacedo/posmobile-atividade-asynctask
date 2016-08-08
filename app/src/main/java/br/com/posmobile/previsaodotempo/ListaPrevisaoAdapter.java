package br.com.posmobile.previsaodotempo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alexandre on 06/08/16.
 */

public class ListaPrevisaoAdapter extends ArrayAdapter<Previsao> {

    public ListaPrevisaoAdapter(Context context, List<Previsao> previsoes) {
        super(context, 0, previsoes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Previsao previsao = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_previsao, parent, false);
        }

        TextView tvPeriodo = (TextView) convertView.findViewById(R.id.textViewPeriodo);
        TextView tvTemperatura = (TextView) convertView.findViewById(R.id.textViewTemperatura);
        TextView tvDescricao = (TextView) convertView.findViewById(R.id.textViewDescricao);
        ImageView ivIcone = (ImageView) convertView.findViewById(R.id.imageViewTempo);


        tvPeriodo.setText(previsao.getPeriodo());
        tvTemperatura.setText(previsao.getTemperatura());
        tvDescricao.setText(previsao.getDescricao());

        //todo Buscar icone utilizando Glide em conjunto com URL base de icones

        return convertView;
    }
}


