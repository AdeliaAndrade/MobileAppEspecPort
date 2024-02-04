package com.example.mobile_espec_port;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Atividade responsável pelos comandos enviados para
 * */

public class ControlActivity extends AppCompatActivity {
    RelativeLayout layout_joystick;

    int lightStatus = 0;

    ConnectionThread connect;

    String connection_bluetooth;

    BluetoothAdapter btAdapter ;

    int lastDirection= 0;

    static TextView txtResult;
    static LinearLayout linearAnalisando;
    static LinearLayout linearStart;
    static LinearLayout linearButtonBlue;
    static LinearLayout linearButtonPlay;
    static TextView btnConnection;

    static int CountItems = 0;

    static String datas = "";

    static ProcessAnalysesItemsObject PAI = new ProcessAnalysesItemsObject();

    final static String url = "https://especportfunctionapp20230813132636.azurewebsites.net/api/ProcessAnalyzes?code=DAAOijRddiEsUXWWFeIAj0dzhSBgXoNrCdDmLVcb8JYHAzFuC5brWA==";

    static RequestQueue queue;

    /*Create da tela*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_activity);

        PAI.ProcessAnalysesItems = new ArrayList<ProcessAnalysesItem>();
        queue = Volley.newRequestQueue(this);

        Connect();

        /*Carrega a tela anterior*/
        Intent intent = this.getIntent();

        /*Pega a string de conexão passada na outra tela*/
        connection_bluetooth = intent.getStringExtra("connection_bluetooth");

        /*Botão responsável por reconectar ao bluetooth*/
        ImageButton btnReconnect = (ImageButton) findViewById(R.id.btn_reconnect_again);
        btnReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connect();
            }
        });

        /*Botão responsável por começar a análise*/
        ImageButton btnStartRequest = (ImageButton) findViewById(R.id.btn_start_request_01);
        btnStartRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Write("btnStart;");
            }
        });

        /*Botão responsável por começar a análise*/
        /*ImageButton btnStartRequest2 = (ImageButton) findViewById(R.id.btn_start_request_02);
        btnStartRequest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Write("btnStart;");
            }
        });*/

        txtResult = (TextView) findViewById(R.id.textView2);
        linearAnalisando = (LinearLayout) findViewById(R.id.linear_analisando);
        linearStart = (LinearLayout) findViewById(R.id.linear_start);
        linearButtonBlue = (LinearLayout) findViewById(R.id.linear_button_blue);
        linearButtonPlay = (LinearLayout) findViewById(R.id.linear_button_play);
        btnConnection = (TextView) findViewById(R.id.btn_conecction);
    }

    /**
     * Método que envia uma instrução para o módulo bluetooth do hardware
     * */
    public void Write(String param){
        connect.write(param.getBytes());

        if(param == "btnStart;"){
            txtResult.setVisibility(View.GONE);
            linearAnalisando.setVisibility(View.VISIBLE);

            linearStart.setVisibility(View.GONE);
            linearButtonBlue.setVisibility(View.GONE);
            linearButtonPlay.setVisibility(View.GONE);
        }
    }

    /**
     * Método responsável por conectar ao bluetooth
     * */
    @SuppressLint("MissingPermission")
    public void Connect(){
        /*Teste Bluetooth e conexão*/
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Intent intent_erro = new Intent(ControlActivity.this, InitialActivity.class);

            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
            ActivityCompat.startActivity(ControlActivity.this, intent_erro, activityOptionsCompat.toBundle());

            Toast.makeText(getApplicationContext(), "Não conectado.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Conectado.", Toast.LENGTH_SHORT).show();
        }

        btAdapter.enable();

        //Mac do módulo bluetooth
        if(connection_bluetooth == null || connection_bluetooth.isEmpty())
            connect = new ConnectionThread( "00:21:13:00:26:5F" );
        else
            connect = new ConnectionThread( connection_bluetooth );

        connect.start();
        //connect.run();

        try {
            Thread.sleep(200);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@org.jetbrains.annotations.NotNull Message msg) {

            /* Esse método é invocado na Activity principal sempre que a thread de conexão Bluetooth recebe uma mensagem*/
            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString= new String(data);

            System.out.println(dataString);

            ProcessDataReceived(dataString);
        }
    };

    static double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public static void ProcessDataReceived(String dataString){
        if(dataString.equals("---N"))
            txtResult.setText("Ocorreu um erro durante a conexão D:");
        else if(dataString.equals("---S"))
            txtResult.setText("Conectado :D");
        else {

                /* Se a mensagem não for um código de status,
                    então ela deve ser tratada pelo aplicativo
                    como uma mensagem vinda diretamente do outro
                    lado da conexão. Nesse caso, simplesmente
                    atualizamos o valor contido no TextView do
                    contador.
                 */

            //datas += "Lux: " + dataString + "\n";
            dataString = dataString.replace(" ", "");
            if(!TextUtils.isEmpty(dataString) && !dataString.contains("end")){
                CountItems ++;

                ProcessAnalysesItem item = new ProcessAnalysesItem();
                item.Index = CountItems;
                item.Value = Double.parseDouble(dataString);

                PAI.ProcessAnalysesItems.add(item);

                //txtResult.setText(datas);
                double waiting = (((CountItems * 100.0)/50.0));
                if(waiting <= 100)
                    btnConnection.setText("     Analisando: " + waiting + "%...     ");
            }
        }

        if(dataString.contains("end")){
            //Enviar para api de processamento
            //Configura a requisicao
            Log.d("json", PAI.toJson().toString());
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, PAI.toJson(), new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response) {
                    // mostra a resposta
                    Log.d("Response", response.toString());

                    Gson gson = new Gson();
                    ResultAnalyze resultAnalyze = gson.fromJson(String.valueOf(response), ResultAnalyze.class);

                    String resultString = "\n\n\nResultado da Análise\n\n\n";
                    resultString += "Qtd Caratenóides: " + (resultAnalyze.amountOfCarotenoids) + "\n\n";
                    resultString += "Desvio Padrão: " + (resultAnalyze.standardDeviation) + "\n\n";
                    resultString += "Ratio: " + (resultAnalyze.ratio) + "\n\n";
                    resultString += "Nível de Maturação: " + (resultAnalyze.maturationLevel == 0 ? "Fruta Verde" : (resultAnalyze.maturationLevel == 1 ? "Fruta Madura" : "Fruta Passada")) + "\n\n";

                    resultString += "\n\n\nTabela de resultado\n\n\n";

                    resultString += "  663nm ";
                    resultString += "  647nm ";
                    resultString += "  470nm ";
                    resultString += "   Ca   ";
                    resultString += "   Cb   ";
                    resultString += "   Ct   ";
                    resultString += "R(ug/mL)\n\n";

                    for (TableAnalyzes item: resultAnalyze.tableAnalyzes) {
                        resultString += "     " + (item.absorbanceAt663nm)
                                + "     " + (item.absorbanceAt647nm)
                                + "     " + (item.absorbanceAt470nm)
                                + "     " + (item.ca)
                                + "     " + (item.cb)
                                + "     " + (item.r)
                                + "\n\n";
                    }

                    txtResult.setVisibility(View.VISIBLE);
                    txtResult.setText(resultString);
                    Log.d("Result:", resultString);

                    linearAnalisando.setVisibility(View.GONE);
                    linearButtonPlay.setVisibility(View.VISIBLE);
                    datas = "";
                    PAI.ProcessAnalysesItems = new ArrayList<ProcessAnalysesItem>();
                    CountItems = 0;
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", String.valueOf(error));
                    datas = "";
                    PAI.ProcessAnalysesItems = new ArrayList<ProcessAnalysesItem>();
                    CountItems = 0;
                }
            });

            // Adiciona a Fila de requisicoes
            queue.add(getRequest);
        }
    }
}