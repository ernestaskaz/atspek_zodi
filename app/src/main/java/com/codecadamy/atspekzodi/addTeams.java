package com.codecadamy.atspekzodi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class addTeams extends AppCompatActivity {
    protected Teams team1;
    protected Teams team2;
    protected String teamOneName;
    protected String teamOneMemOne;
    protected String teamOneMemTwo;
    protected String teamTwoName;
    protected String teamTwoMemOne;
    protected String teamTwoMemTwo;
    public ArrayList<String> globalWordList = new ArrayList<>();
    public boolean alreadyDone = true;


    private static final String TAG = "addTeams";
    private EditText setFirstTeamName;
    private EditText setFirstTeamFirstMemberName;
    private EditText setFirstTeamSecondMemberName;
    private EditText setSecondTeamName;
    private EditText setSecondTeamFirstMemberName;
    private EditText setSecondTeamSecondMemberName;
    private Button goToMainActivity;
    SharedPreferences alreadyDonePrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teams);

        setFirstTeamName = findViewById(R.id.set_first_team_name);
        setFirstTeamFirstMemberName = findViewById(R.id.set_first_team_firstmember_name);
        setFirstTeamSecondMemberName = findViewById(R.id.set_first_team_secondmember_name);
        setSecondTeamName = findViewById(R.id.set_second_team_name);
        setSecondTeamFirstMemberName = findViewById(R.id.set_second_team_firstmember_name);
        setSecondTeamSecondMemberName = findViewById(R.id.set_second_team_secondmember_name);
        goToMainActivity = findViewById(R.id.go_to_main_activity);

        team1 = new Teams(teamOneName, teamOneMemOne, teamOneMemTwo, 0, 0, 1);
        team2 = new Teams(teamTwoName, teamTwoMemOne, teamTwoMemTwo, 0, 0, 2);

        Log.d(TAG, "wordlistas BO already done pries get" + alreadyDone);
        alreadyDonePrefs = getSharedPreferences("AlreadyDonePrefs", Context.MODE_PRIVATE);
       /*SharedPreferences.Editor editor = alreadyDonePrefs.edit();
       editor.clear();
       editor.commit();*/
        alreadyDone = alreadyDonePrefs.getBoolean("AlreadyDonePrefs", true);
        Log.d(TAG, "wordlistas BO already done po get" + alreadyDone);


// start of bot crawler, iš Wiki parse'inami LT žodžiai (~2000), atrenkami tinkami pagal galūnes (lengvesni) ir dydį (be ir iš jis ji etc.)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Log.d(TAG, "wordlistas BO already done pries if " + alreadyDone);

                        // kadangi runnable veikia backgrounde(matyt, bet bet kokiu atveju, botas suveikia ne vieną kartą), apriboju bot'ą, kad veiktų max vieną kartą su boolean sharedpref.

                        if (alreadyDone) {

                            Document doc = Jsoup.connect("https://lt.wiktionary.org/wiki/S%C4%85ra%C5%A1as:Da%C5%BEniausi_lietuvi%C5%A1ki_%C5%BEod%C5%BEiai").timeout(6000).get();
                            Elements body = doc.select("div.mw-parser-output");
                            //System.out.println(body);
                            //Log.d(TAG, "body a atributas zodziai " + body.select("a").size());
                            //Log.d(TAG, "body p atributas para " + body.select("p").size());


                            String replace = "(puslapis neegzistuoja)";

                            for (int i = 0; i < body.select("a").size(); i++) {
                                String word = body.select("a").get(i).attr("title");
                                word = word.replaceAll(replace, "");
                                word = word.replaceAll("[\\()]", "");
                                word = word.trim();

                                if (word.length() > 4 && (word.endsWith("as") || word.endsWith("ti") || word.endsWith("ė") || word.endsWith("a") || word.endsWith("is") || word.endsWith("us") || word.endsWith("i"))) {
                                    globalWordList.add(word);

                                }
                            }
                            Log.d(TAG, "wordlistas logd dydis" + globalWordList.size());
                            Log.d(TAG, "wordlistas logd visas listas" + globalWordList.toString());


                            // išsaugo arraylistą
                            saveWordListToSharedPrefs(globalWordList, "globallist");
                            // boolean alreadyDone = false ir išsaugoma į sharedpref. kitą oncreate botas skipinamas.
                            saveAlreadyDoneToSharedPrefs();
                            Log.d(TAG, "wordlistas BO already done if1"  + alreadyDone);

                        } else {
                            globalWordList = getWordListFromSharedPrefs("globallist");
                            Log.d(TAG, "wordlistas inside try dydis" + globalWordList.size());


                        }

                    } catch (Exception ex) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }
            }).start();


        goToMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                teamOneName = setFirstTeamName.getText().toString();
                teamOneMemOne = setFirstTeamFirstMemberName.getText().toString();
                teamOneMemTwo = setFirstTeamSecondMemberName.getText().toString();
                teamTwoName = setSecondTeamName.getText().toString();
                teamTwoMemOne = setSecondTeamFirstMemberName.getText().toString();
                teamTwoMemTwo = setSecondTeamSecondMemberName.getText().toString();

                if (teamOneName.isEmpty()) {
                    teamOneName = "Einsteins";
                }
                if (teamOneMemOne.isEmpty()) {
                    teamOneMemOne = "Meredith";
                }
                if (teamOneMemTwo.isEmpty()) {
                    teamOneMemTwo = "Kevin";
                }
                if (teamTwoName.isEmpty()) {
                    teamTwoName = "Geniuses";
                }
                if (teamTwoMemOne.isEmpty()) {
                    teamTwoMemOne = "Galileo";
                }
                if (teamTwoMemTwo.isEmpty()) {
                    teamTwoMemTwo = "Da Vinci";
                }


                if (teamOneName.length() > 15 || teamOneMemOne.length() > 15 || teamOneMemTwo.length() > 15 || teamTwoName.length() > 15 || teamTwoMemOne.length() > 15 || teamTwoMemTwo.length() > 15) {
                    Toast.makeText(getApplicationContext(), "Too long! Maximum 15 characters allowed", Toast.LENGTH_SHORT).show();
                } else {

                    // siunčiame teamnames ir teammate names į MainActivity
                    Intent letsPlay = new Intent(addTeams.this, MainActivity.class);
                    letsPlay.putExtra("teamOneName", teamOneName);
                    letsPlay.putExtra("teamOneMemOne", teamOneMemOne);
                    letsPlay.putExtra("teamOneMemTwo", teamOneMemTwo);
                    letsPlay.putExtra("teamTwoName", teamTwoName);
                    letsPlay.putExtra("teamTwoMemOne", teamTwoMemOne);
                    letsPlay.putExtra("teamTwoMemTwo", teamTwoMemTwo);
                    startActivity(letsPlay);


                }
            }
        });


    }

    public void saveWordListToSharedPrefs(ArrayList<String> list, String key) {
        SharedPreferences globalWordListPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = globalWordListPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.commit();
    }

    public ArrayList<String> getWordListFromSharedPrefs(String key){
        SharedPreferences globalWordListPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = globalWordListPrefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void saveAlreadyDoneToSharedPrefs() {
        Log.d(TAG, "wordlistas BO already done metode pries " + alreadyDone);
        alreadyDone = false;
        SharedPreferences.Editor editor = alreadyDonePrefs.edit();
        editor.putBoolean("AlreadyDonePrefs", alreadyDone);
        editor.apply();


    }

}


