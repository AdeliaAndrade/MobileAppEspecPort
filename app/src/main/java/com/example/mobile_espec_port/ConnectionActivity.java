package com.example.mobile_espec_port;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

/**
 * Atividade responsável por mostrar uma tela de carregamento entre a conexão do celular e do hardware
 * */
public class ConnectionActivity extends AppCompatActivity {

    static TextView statusMessage;

    /*Create da tela*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        statusMessage = (TextView) findViewById(R.id.btn_conecction);

        /*Carrega a tela anterior para pegar a string de conexão passada*/
        Intent intent_1 = this.getIntent();

        String connection_bluetooth = intent_1.getStringExtra("connection_bluetooth");

        Intent intent = new Intent(ConnectionActivity.this, ControlActivity.class);

        /*A string de conexão será passada para outra tela para que seja possivel mandar comandos para o hardware*/
        intent.putExtra("connection_bluetooth", connection_bluetooth);

        /*A tela de start da análise é carregada*/
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
        ActivityCompat.startActivity(ConnectionActivity.this, intent, activityOptionsCompat.toBundle());
    }
}