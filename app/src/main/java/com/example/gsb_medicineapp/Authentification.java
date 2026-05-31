package com.example.gsb_medicineapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.SecureRandom;

public class Authentification extends AppCompatActivity {
//Ici je déclare mes attributs en privé
    private EditText codeV,codeS;
    private LinearLayout layoutCle;
    String myRandomKey;

    private static final String PREF_NAME = "UserPrefs"; //fichier installé en local pour vérifier si le user est connecté
    private static final String KEY_USER_STATUS = "userStatus"; //statut de connexion du visiteur
    private static final String SECURETOKEN = "Euroforma"; //token permettant la connexion  sécurisée

    @Override
    //constructeur de la classe
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        codeV = findViewById(R.id.edit_text_code_visiteur);
        codeS = findViewById(R.id.edit_text_code_securite); //ici je relie les composants de ma vue avec les attributs déclarés.
        layoutCle = findViewById(R.id.layout_cle);
        layoutCle.setVisibility(View.INVISIBLE);
    } //ici se ferme le constructeur

    private void afficherMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
    public void afficherLayout(View v){
        layoutCle.setVisibility(View.VISIBLE);
        String codeVisiteur =codeV.getText().toString();
        myRandomKey = genererChaineAleatoire(5);
        SendKeyTask sendKeyTask = new SendKeyTask(getApplicationContext());
        sendKeyTask.execute(codeVisiteur,myRandomKey,SECURETOKEN);
    }

    private String genererChaineAleatoire(int longueur){
        String caracteresPermis = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder chaineAleatoire = new StringBuilder();

        SecureRandom random = new SecureRandom();

        for (int i = 0; i < longueur; i++) {
            int index = random.nextInt(caracteresPermis.length());
            char caractereAleatoire = caracteresPermis.charAt(index);
            chaineAleatoire.append(caractereAleatoire);
        }

        return chaineAleatoire.toString();
    }
    //ici je déclare mes méthodes

    public void comparerChaine(View v) {
        String codeSecurite = codeS.getText().toString().trim();
        if (myRandomKey.equals(codeSecurite)){
            afficherMessage("Connexion valide");
            setUserStatus("authentification=OK");
            Intent authIntent = new Intent(this, MainActivity.class);
            startActivity(authIntent); //redirection vers la page main activity
            finish();

        }else{
            afficherMessage("La clé de sécurité saisie est incorrecte");
            setUserStatus("authentification=KO");
        }
    }

    public void setUserStatus(String status){
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER_STATUS,status);
        editor.apply();
    }

}