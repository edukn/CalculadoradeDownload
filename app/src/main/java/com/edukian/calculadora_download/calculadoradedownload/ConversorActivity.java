package com.edukian.calculadora_download.calculadoradedownload;

import android.content.DialogInterface;
import android.os.Bundle;
/*import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;*/
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

public class ConversorActivity extends AppCompatActivity {

    Spinner spinner;
    EditText velocidade;
    Button converter;
    float velocidadeCon;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversor);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if(getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.conversor, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        velocidade = (EditText) findViewById((R.id.editText3));

        converter = (Button)findViewById(R.id.button2);

        converter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tipo = 0;
                if(!(velocidade.getText().toString().equals("")))
                {
                    velocidadeCon = Float.valueOf(velocidade.getText().toString());
                    switch (spinner.getSelectedItemPosition()) //Radio group tamanho do arquivo
                    {
                        case 0: //Mbps
                            tipo = 1;
                            break;
                        case 1: //Kbps
                            tipo = 2;
                            break;
                        case 2: //Gbps
                            tipo = 3;
                            break;
                    }
                    String resultado = String.valueOf(conversor(velocidadeCon, tipo));
                    showLocationDialog(resultado);
                }
            }
        });

        velocidade.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    velocidade.setText("");
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String conversor(float velocidade, int tipo)
    {
        float resultado;
        String texto = "";
        if(tipo == 1 && velocidade < 8)
        {
            resultado = (velocidade * 1024) / 8;
            texto = (int)resultado + " KB/s";
        }
        else if(tipo == 1 && velocidade >= 8)
        {
            resultado = (velocidade * 1024) / 8;
            texto = resultado / 1024 + " MB/s";
        }
        if(tipo == 2)
        {
            resultado = velocidade / 8;
            if(velocidade >= 8192)
                texto = resultado + " KB/s ou \n"+ resultado / 1024 + " MB/s";
            else
                texto = resultado + " KB/s";
        }
        if(tipo == 3)
        {
            resultado = ((velocidade * 1024) * 1024) / 8;
            if(resultado < 1048576)
                texto = resultado / 1024 + " MB/s";
            else
                texto = (resultado / 1024) / 1024 + " GB/s";
        }
        return  texto;
    }

    private void showLocationDialog(String string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ConversorActivity.this);
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
