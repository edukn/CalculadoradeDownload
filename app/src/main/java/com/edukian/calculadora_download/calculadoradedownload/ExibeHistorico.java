package com.edukian.calculadora_download.calculadoradedownload;

import android.content.Context;
import android.content.DialogInterface;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ExibeHistorico extends AppCompatActivity {

    ContextoDados db = new ContextoDados(this);
    ListView listView;
    ArrayList<String> historico = new ArrayList<String>();
    Button limpar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibe_historico);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        exibir();

        limpar = (Button)findViewById(R.id.button3);

        listView = (ListView) findViewById(R.id.lista1);
        listView.setClickable(true);

        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               excluir();
               listView.setAdapter(null);
            }
        });

    }

    public void excluir()
    {
        ContextoDados.ContatosCursor cursor = db.RetornarContatos();
        db.Delete();
    }

    public void exibir()
    {
        CarregarLista(this);
    }

    public void CarregarLista(Context c)
    {
        ContextoDados.ContatosCursor cursor = db.RetornarContatos();

        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            ImprimirLinha(cursor.getTamanho(), cursor.getVelocidade(), cursor.getTipo_Tam(), cursor.getTipo_Vel(), cursor.getTempo());
        }
    }

    public void ImprimirLinha(String arquivo, String velocidade, String tipoA, String tipoV, String tempo)
    {
        String valores = "";
        listView = (ListView) findViewById(R.id.lista1);
        if(tipoA.equals("1")) {
            if(tipoV .equals("1"))
                    valores = arquivo + "GB(s) a " + velocidade + "MB/s:\n" + tempo;
            else if (tipoV .equals("2"))
                    valores = arquivo + "GB(s) a " + velocidade + "KB/s:\n" + tempo;
            else if (tipoV .equals("3"))
                valores = arquivo + "GB(s) a " + velocidade + "Mbps:\n" + tempo;
            else if (tipoV .equals("4"))
                valores = arquivo + "GB(s) a " + velocidade + "Gbps:\n" + tempo;
        }
         if( tipoA.equals("2")) {
            if (tipoV .equals("1"))
                valores = arquivo + "MB(s) a " + velocidade + "MB/s:\n" + tempo;
            if (tipoV .equals("2"))
                valores = arquivo + "MB(s) a " + velocidade + "KB/s:\n" + tempo;
            if (tipoV .equals("3"))
                valores = arquivo + "MB(s) a " + velocidade + "Mbps:\n" + tempo;
             if (tipoV .equals("4"))
                 valores = arquivo + "MB(s) a " + velocidade + "Gbps:\n" + tempo;
        }
         if( tipoA.equals("3")) {
            if (tipoV .equals("1"))
                valores = arquivo + "TB(s) a " + velocidade + "MB/s:\n" + tempo;
            else if (tipoV .equals("2"))
                valores = arquivo + "TB(s) a " + velocidade + "KB/s:\n" + tempo;
            else if (tipoV .equals("3"))
                valores = arquivo + "TB(s) a " + velocidade + "Mbps:\n" + tempo;
            else if (tipoV .equals("4"))
                valores = arquivo + "TB(s) a " + velocidade + "Gbps:\n" + tempo;
        }

        historico.add(valores);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historico);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }

//COLOCAR MEU ID DE ANUNCIO

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLocationDialog(String string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExibeHistorico.this);
        builder.setTitle(string);
        //builder.setMessage(string);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // positive button logic
            }
        });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

}
