package com.itsulution.www.comptabilite.fonction;

import android.app.Activity;
import android.os.StrictMode;
import android.widget.Toast;

import com.itsulution.www.comptabilite.modele.Caisse;
import com.itsulution.www.comptabilite.modele.Parametre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrateur on 13/12/2016.
 */
public class outils implements Serializable {

    public static Activity app=null;
    public static String lien = "http://localhost:8083/?";


    public static String getJsonFromServerNouveau(String url) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println(lien+ url);
        InputStream is = (InputStream) new URL(lien+url).getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String result, line = reader.readLine();
        result = line;
        while ((line = reader.readLine()) != null) {
            result += line;
        }
        return result;
    }

    public static Parametre getParametre(String nomUser,String password) throws IOException{
        JSONArray json = null;
        int qte=0;
        try {
            String url="page=connect&NomUser="+nomUser+"&Password="+password;
            json = new JSONArray(getJsonFromServerNouveau(url));
            JSONObject ob =null;
            if(json.length()>0) {
                ob = (JSONObject) json.get(0);
                ArrayList<Caisse> lcaisse = listeCaisseServeur();
                Caisse c = null;
                if (ob.getInt("id_parametre") != 0) {
                    for (int i = 0; i < lcaisse.size(); i++)
                        if (lcaisse.get(i).getCa_no() == ob.getInt("CA_No"))
                            c = lcaisse.get(i);
                    return new Parametre(ob.getInt("DE_No"), ob.getString("CT_Num"), ob.getInt("CO_No"), ob.getInt("DO_Souche"),
                            ob.getString("Affaire"), ob.getString("NumDoc"), ob.getString("Vehicule"),
                            nomUser, password, c, ob.getString("Date_Facture"), ob.getInt("R_Facture"), ob.getInt("ID_Facture"));
                } else {
                    Toast.makeText(app, "Login ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Caisse> listeCaisseServeur(){
        JSONArray json = null;
        ArrayList<Caisse> lart=null;
        try {
            String url="page=caisse";
            json = new JSONArray(getJsonFromServerNouveau(url));
            lart= new ArrayList<Caisse>();
            for(int i=0; i<json.length(); i++){
                JSONObject json_data = json.getJSONObject(i);
                lart.add(new Caisse(json_data.getString("JO_Num"),json_data.getString("CA_Intitule"),json_data.getInt("CA_No"),json_data.getInt("CO_NoCaissier")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lart;
    }

    public static Parametre connexion(String login,String mdp) {
        try{
            return getParametre(login,mdp);
        }catch (IOException e ){
        }
        return null;
    }
}
