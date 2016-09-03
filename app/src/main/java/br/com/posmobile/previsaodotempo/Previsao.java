package br.com.posmobile.previsaodotempo;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alexandre on 06/08/16.
 */

public class Previsao {

    String periodo;
    String temperatura;
    String icone;
    String descricao;
    final static Locale local = new Locale("pt","BR");
    final static SimpleDateFormat dateFormat =
                   new SimpleDateFormat("dd, EEEE", local);


    public Previsao() {
    }

    public Previsao(String temperatura, String periodo) {
        this.temperatura = temperatura;
        this.periodo = periodo;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(long periodo) {
        this.periodo = dateFormat.format(new Date(periodo));
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {

        //this.icone = icone;
        this.icone = String.format(Utils.URL_ICONE, icone);
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
