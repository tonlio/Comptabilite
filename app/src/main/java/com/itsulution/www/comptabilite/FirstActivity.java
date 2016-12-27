package com.itsulution.www.comptabilite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.itsulution.www.comptabilite.fonction.outils;

public class FirstActivity extends AppCompatActivity {

    TextView adresse;
    Button valide;
    outils ou = new outils();
    private String data = "";
    private static final String PREFS_NAME = "YOUR_TAG";
    private static final String DATA_TAG = "DATA_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ou.app=FirstActivity.this;
        adresse = (TextView) findViewById(R.id.serveur_ip);
        valide = (Button) findViewById(R.id.serveur_suivant);
        SharedPreferences mSettings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = mSettings.edit();

        if(data!=null){
            ou.lien=data;
            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
            intent.putExtra("outils", ou);
            startActivity(intent);
        }
        valide.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                ou.lien = "http://" + adresse.getText().toString() + ":8083/?";
                data = ou.lien;
                editor.putString(DATA_TAG, data);
                editor.commit();
                System.out.println(ou.lien);
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                intent.putExtra("outils", ou);
                startActivity(intent);
            }
        });
    }
}
