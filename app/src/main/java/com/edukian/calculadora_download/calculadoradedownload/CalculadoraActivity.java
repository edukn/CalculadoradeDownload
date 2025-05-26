package com.edukian.calculadora_download.calculadoradedownload;

import android.content.DialogInterface;
import android.os.Bundle;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class CalculadoraActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button calcular;
    private AdView mAdView;
    private EditText tamanho, velocidade;
    Spinner spinner, spinner2;
    private String tamanho_arq1, velocidade1, tipo_tam1, tipo_vel1, tempo1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        //add set 23
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //ad set 23 ^^
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       //*********SPINNER COMBO BOX:
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.formato_arquivo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.velocidade, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        //*************

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Lógica da calculadora e eventos do radioButton e botão:*******************

        tamanho = (EditText) findViewById((R.id.editText1));
        velocidade = (EditText) findViewById((R.id.editText2));

        calcular = (Button)findViewById(R.id.button);

        calcular.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                float tamanho_arq, velocidade_net;
                int tipo_arq = 0, tipo_vel = 0;
                if(!(tamanho.getText().toString().equals("") || velocidade.getText().toString().equals("")))
                {
                    tamanho_arq = Float.valueOf(tamanho.getText().toString());
                    velocidade_net = Float.valueOf(velocidade.getText().toString());
                    switch (spinner.getSelectedItemPosition()) //Spinner tamanho do arquivo
                    {
                        case 0: //GigaByte
                            tipo_arq = 1;
                            break;
                        case 1: //MegaByte
                            tipo_arq = 2;
                            break;
                        case 2: //TeraByte
                            tipo_arq = 3;
                            break;
                    }
                    switch (spinner2.getSelectedItemPosition()) //Spinner velocidade internet
                    {
                        case 1: //MBs
                            tipo_vel = 1;
                            break;
                        case 2: //KBs
                            tipo_vel = 2;
                            break;
                        case 0: //Mbps
                            tipo_vel = 3;
                            break;
                        case 3: //Gbps
                            tipo_vel = 4;
                            break;
                    }
                   String resultado = String.valueOf(calcular(tamanho_arq, velocidade_net, tipo_arq, tipo_vel));
                    //  Toast.makeText(getApplicationContext(),
                    //    teste, Toast.LENGTH_LONG).show();
                    //exibe.setText(teste);
                    tamanho_arq1 = Float.toString(tamanho_arq);
                    velocidade1 = Float.toString(velocidade_net);
                    tipo_tam1 = Integer.toString(tipo_arq);
                    tipo_vel1 = Integer.toString(tipo_vel);
                    tempo1 = resultado;

                    showLocationDialog("", resultado);

                    Salvar();
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

        tamanho.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    tamanho.setText("");
                }
                return false;
            }
        });

    }

    public void Salvar()
    {
        ContextoDados db = new ContextoDados(this);
        db.InserirCalculo(tamanho_arq1, velocidade1, tipo_tam1, tipo_vel1, tempo1);
    }

    //******************************************************************************************

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calculadora, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_segundo_layout) {
            Intent thirdActivity = new Intent(this, ConversorActivity.class);
            startActivity(thirdActivity);

        } else if (id == R.id.nav_share) {
             Intent sendIntent = new Intent();
             sendIntent.setAction(Intent.ACTION_SEND);
             sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.edukian.calculadora_download.calculadoradedownload");
             sendIntent.setType("text/plain");
             startActivity(sendIntent);

         } else if (id == R.id.teste_velocidade) {
             Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.fast.com"));
             startActivity(browserIntent);

         } else if (id == R.id.historico) {
             Intent thirdActivity = new Intent(this,ExibeHistorico.class);
             startActivity(thirdActivity);

       /* } else if (id == R.id.nav_send) {
             showLocationDialog("Em breve novas funcionalidades!", "Enviar");*/

         } else if (id == R.id.info) {
             showLocationDialog("Calculadora de Download desenvolvida por Eduardo K. Neto.\n\nO tempo de download é" +
                     " baseado em velocidades constantes.", "Versão 4.6");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String calcular(float tamanho2, float velocidade2, int tipoArq, int tipoVelocidade)
    {
        String tempo;
        float result = 0, KBsize, KBspeed;
        if(tipoArq == 2) //tipo MB
            KBsize = tamanho2 * 1024;
        else if(tipoArq == 1)//tipo GB
            KBsize = (tamanho2 * 1024) * 1024;
        else
            KBsize = ((tamanho2 * 1024) * 1024) * 1024;
        if(tipoVelocidade == 2) //tipo KBs
            KBspeed = velocidade2;
        else if(tipoVelocidade == 1) //tipo MBs
            KBspeed = (velocidade2 * 1024);
        else if(tipoVelocidade == 4)
            KBspeed = ((velocidade2 * 1024) * 1024) / 8; //tipo Gbps
        else
            KBspeed = (velocidade2 * 1024) / 8; //tipo Mbps
        result = KBsize / KBspeed;
        tempo = resultadoFinal(result);
        return tempo;
    }

    public String resultadoFinal(float t)
    {
        String tempo = "";
        float min, resto, hora, dia, aux, mes, ano;
        if(t < 60)
        {
            if(t < 1)
                tempo = " Menos de 1 segundo!";
            else {
                if(t > 1)
                    tempo = (int) t + " Segundos";
                else
                    tempo = (int) t + " Segundos";
            }
        }
        else if(t>=60 && t < 3600)
        {
            min = t / 60;
            resto = t % 60;
            if((int)min > 1) {
                if((int)resto > 1)
                    tempo = (int) min + " Minutos e " + (int) resto + " Segundos";
                else
                    tempo = (int) min + " Minutos e " + (int) resto + " Segundos";
            }
            else {
                if((int)resto > 1)
                    tempo = (int) min + " Minuto e " + (int) resto + " Segundos";
                else
                    tempo = (int) min + " Minuto e " + (int) resto + " Segundos";
            }
        }else if(t >= 3600 && t < 86400)
        {
            hora = t / 60;
            aux = hora;
            hora = hora / 60;
            min = aux % 60;
            if((int)hora > 1) {
                if((int)min > 1)
                    tempo = (int) hora + " Horas e " + (int) min + " Minutos";
                else
                    tempo = (int) hora + " Horas e " + (int) min + " Minuto";
            }
            else {
                if((int)min > 1)
                    tempo = (int) hora + " Horas e " + (int) min + " Minutos";
                else
                    tempo = (int) hora + " Horas e " + (int) min + " Minuto";
            }
        }
        else if(t >= 86400 && t < 2592000)
        {
            dia =  t / 86400;
            hora = (t % 86400)/3600;
            aux = (t % 86400) % 3660;
            min = aux / 60;
            if((int)dia > 1)
            {
                if((int)hora > 1) {
                    if((int)min > 1)
                        tempo = (int) dia + " Dias, " + (int) hora + " Horas e " + (int) min + " Minutos";
                    else
                        tempo = (int)dia + " Dias, " + (int)hora + " Horas e " + (int)min + " Minuto";
                }
                else
                {
                    if((int)min > 1)
                        tempo = (int) dia + " Dias, " + (int) hora + " Hora e " + (int) min + " Minutos";
                    else
                        tempo = (int)dia + " Dias, " + (int)hora + " Hora e " + (int)min + " Minuto";
                }
            }
            else
            {
                if((int)hora > 1) {
                    if((int)min > 1)
                        tempo = (int) dia + " Dia, " + (int) hora + " Horas e " + (int) min + " Minutos";
                    else
                        tempo = (int)dia + " Dia, " + (int)hora + " Horas e " + (int)min + " Minuto";
                }
                else
                {
                    if((int)min > 1)
                        tempo = (int) dia + " Dia, " + (int) hora + " Hora e " + (int) min + " Minutos";
                    else
                        tempo = (int)dia + " Dia, " + (int)hora + " Hora e " + (int)min + " Minuto";
                }
            }
        }
        else if(t >= 2592000 && t < 31536000)
        {
            mes = t / 2592000;
            dia = (t % 2592000)/86400;
            aux = (t % 2592000) % 86400;
            hora = aux / 3600;
            min = (aux % 3600)/60;
            if((int)mes > 1)
                tempo = (int)mes + " Meses, " + (int)dia + " Dias, " + (int)hora + " Horas e " + (int)min + " Minutos";
            else
                tempo = (int)mes + " Mês, " + (int)dia + " Dias, " + (int)hora + " Horas e " + (int)min + " Minutos";
        }
        else if(t >= 31536000)
        {
            ano = t / 31536000;
            mes = (t % 31536000)/2592000;
            dia = ((t % 31536000) % 2592000) / 86400;
            hora = (((t % 31536000) % 2592000) % 86400) / 3600;
            min =  ((((t % 31536000) % 2592000) % 86400) % 3600) / 60;
            if((int)ano > 1)
                if((int)mes > 1)
                    tempo = (int)ano + " Anos, " + (int)mes + " Meses, " + (int)dia + " Dias, " + (int)hora + " Horas e " + (int)min + " Minutos";
                else
                    tempo = (int)ano + " Anos, " + (int)mes + " Mês, " + (int)dia + " Dias, " + (int)hora + " Horas e " + (int)min + " Minutos";
            else
                if((int)mes > 1)
                    tempo = (int)ano + " Ano, " + (int)mes + " Meses, " + (int)dia + " Dias, " + (int)hora + " Horas e " + (int)min + " Minutos";
                else
                    tempo = (int)ano + " Ano, " + (int)mes + " Mês, " + (int)dia + " Dias, " + (int)hora + " Horas e " + (int)min + " Minutos";
        }
        return tempo;
    }


    private void showLocationDialog(String string, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CalculadoraActivity.this);
        builder.setTitle(title);
        if(string != "")
        builder.setMessage(string);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // logica botao positivo
            }
        });


        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

}
