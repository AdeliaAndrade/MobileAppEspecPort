package com.example.mobile_espec_port;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

/**
 * Atividade inicial responsável por mostrar o logo do instituto federal
 * */
public class MainActivity extends AppCompatActivity {

    /*Create da tela*/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Handler handler = new Handler();
        setContentView(R.layout.ifsp_intro);

        /*Função de chamar outra tela após um determinado tempo*/
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, InitialActivity.class);

                /*A tela inicial é carregada com um efeito de transição*/
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                ActivityCompat.startActivity(MainActivity.this, intent, activityOptionsCompat.toBundle());
            }
        }, 2000);
    }
}