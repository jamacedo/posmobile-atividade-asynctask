package br.com.posmobile.previsaodotempo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrincipalActivity extends AppCompatActivity {


    ListView listaPrevisoes;
    List<Previsao> previsoes = new ArrayList<Previsao>();

    TextView tvTemperaturaHoje;
    TextView tvPeriodoHoje;
    ImageView ivIconeHoje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        listaPrevisoes      = (ListView) findViewById(R.id.previsoesListView);
        tvTemperaturaHoje   = (TextView) findViewById(R.id.textViewTempHoje);
        tvPeriodoHoje       = (TextView) findViewById(R.id.textViewPeriodoHoje);
        ivIconeHoje         = (ImageView) findViewById(R.id.imageViewIconeHoje);

        listaPrevisoes.setAdapter(new ListaPrevisaoAdapter(this, previsoes));

        listaPrevisoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PrincipalActivity.this, "O item " + position + "foi selecionado",
                        Toast.LENGTH_SHORT).show();
            }
        });


        //todo Criar um novo objeto do AsyncTask customizado
        //todo Executar o AsyncTask
        PrevisaoAsyncTask previsaoTask = new PrevisaoAsyncTask();
        previsaoTask.execute();

    }

    private class PrevisaoAsyncTask extends AsyncTask<String, Void, List<Previsao>> {

        @Override
        protected List<Previsao> doInBackground(String... params) {
            try {
                //Utilizando OkHttp para buscar dados de uma URL
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Utils.URL_PREVISOES)
                        .build();
                Response response = client.newCall(request).execute();
                String responseJson = response.body().string();

                //Trabalhando com JSON MANUALMENTE com apis nativas do Android
                JSONObject jsonObject = new JSONObject(responseJson);
                if (jsonObject != null) {
                    List<Previsao> previsoes = new ArrayList<>();
                    Previsao previsaoTemp;
                    JSONArray listaPrevisoes = jsonObject.getJSONArray("list");
                    for (int i = 1; i < listaPrevisoes.length(); i++) {
                        previsaoTemp = new Previsao();
                        previsaoTemp.setPeriodo(listaPrevisoes.getJSONObject(i).getLong("dt") * 1000);
                        previsaoTemp.setTemperatura(listaPrevisoes.getJSONObject(i).getJSONObject("temp").getDouble("day") + "°");
                        previsaoTemp.setIcone(listaPrevisoes.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"));
                        previsaoTemp.setDescricao(listaPrevisoes.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
                        //todo Preencher atributo descricao buscando o dado em weather->description

                        //todo BUG descobrir o que falta aqui
                        previsoes.add(previsaoTemp); //adicionando o objeto à lista
                    }
                    return previsoes;
                } else {
                    return null;
                }
            } catch (IOException ex) {
                Log.e(PrincipalActivity.class.toString(), "IOException " + ex.getMessage());
                ex.printStackTrace();
            } catch (JSONException ex) {
                Log.e(PrincipalActivity.class.toString(), "JSONException " + ex.getMessage());
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Previsao> previsoes) {

            //todo checar se vieram previsões
            //todo atualizar previsoes
            //todo exibir Toast caso não tenham vindo previsões
            //Verificando se foram carregadas previsoes
            if(previsoes.size() > 0)
                atualizaPrevisoes(previsoes);
            else {
                tvPeriodoHoje.setText("Sem dados");
                tvTemperaturaHoje.setText("Sem dados");
                Toast.makeText(PrincipalActivity.this, "Nenhuma previsão disponível!", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void atualizaPrevisoes(List<Previsao> previsoes) {
        this.previsoes.clear();
        Previsao previsaoDestaque = previsoes.remove(0);
        tvTemperaturaHoje.setText(previsaoDestaque.getTemperatura());
        tvPeriodoHoje.setText(previsaoDestaque.getPeriodo());

        //todo Buscar icone do tempo com GLIDE aqui (Após finalizar e testar AsyncTask)
        Glide.with(this).load(previsaoDestaque.getIcone()).into(ivIconeHoje);

        PrincipalActivity.this.previsoes.addAll(previsoes);
        ((ArrayAdapter) PrincipalActivity.this.listaPrevisoes.getAdapter()).notifyDataSetChanged();
    }



}
