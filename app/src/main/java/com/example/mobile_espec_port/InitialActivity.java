package com.example.mobile_espec_port;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

/**
 * Atividade responsável por mostrar a tela inicial, contendo uma TextView responsável por pegar a string de conexão com o bluetooth
 * */
public class InitialActivity extends AppCompatActivity {

    /*Create da tela*/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_activity);

        Button btnConnection = (Button) findViewById(R.id.btn_conecction);
        TextView connection_bluetooth = (TextView) findViewById(R.id.txt_conection);

        /* Quando o botão é clicado*/
        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InitialActivity.this, ConnectionActivity.class);

                /*A string de conexão será passada para outra tela para que seja possivel mandar comandos*/
                intent.putExtra("connection_bluetooth", connection_bluetooth.getText());

                /*A tela de carregamento é carregada com um efeito de transição*/
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                ActivityCompat.startActivity(InitialActivity.this, intent, activityOptionsCompat.toBundle());
            }
        });
    }
}