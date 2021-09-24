package com.codecadamy.atspekzodi;

import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import android.content.Intent;
import android.widget.Toast;



public class MainActivity extends addTeams {

    private static final String TAG = "MainActivity";
    public int currentWordIndex = 0;
    public int currentRoundIndex = 0;
    public int teamMemberIndex = 0;
    public int globalWordIndex;

    SharedPreferences indexPreferences;

    public ArrayList<String> wrongWordList;
    public ArrayList<String> correctWordList;
    private ArrayList<String> wordsList;

    int roundPoints;
    int roundPointsTeam1 = 0;
    int roundPointsTeam2 = 0;
    int totalPointsTeam1 = 0;
    int totalPointsTeam2 = 0;


    private TextView wordWindow;
    private TextView teamMemberTurn;
    private Button wrongAnswer;
    private Button correctAnswer;
    private Button nextRound;

    private TextView countDownTimerTime;
    private Button countDownTimerButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 60000;
    private boolean isTimerRunning;
    private boolean teamTurn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordWindow = findViewById(R.id.guess_the_word_window);
        wrongAnswer = findViewById(R.id.skip_word_button);
        correctAnswer = findViewById(R.id.correct_word_button);
        //nextRound XML visible only tik testavimui;
        nextRound = findViewById(R.id.next_round_button);
        teamMemberTurn = findViewById(R.id.team_member_turn);

        countDownTimerTime = findViewById(R.id.countdown_timer_time);
        countDownTimerButton = findViewById(R.id.countdown_timer_button);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        toolbar.setTitle("Atspėk Žodį!");
        setSupportActionBar(toolbar);
        // toolbar'o back icon;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.dwight_the_office);


        // on back press;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlertDialog();
            }
        });


        countDownTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startStop();
            }
        });
        correctAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected(0);
            }
        });
        wrongAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSelected(1);
            }
        });
        nextRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        goToRoundEndActivityViaIntent();
            }
        });

        // gaunamas indexai iš RoundEndActivity, kad neprasidėtų nuo 0;
        currentWordIndex = getIntent().getIntExtra("currentwordindexfromREA", 0);
        currentRoundIndex = getIntent().getIntExtra("currentRoundIndexFromREA", 0);
        teamMemberIndex = getIntent().getIntExtra("teamMemberIndexFromREA", 0);
        Log.d(TAG, "currentindexmainactivityreceive " + currentWordIndex);

        if (currentRoundIndex == 0) {
            // jeigu raundas pirmas, gaunami komandų ir narių pavadinimai iš addTeams
            teamOneName = getIntent().getStringExtra("teamOneName");
            teamOneMemOne = getIntent().getStringExtra("teamOneMemOne");
            teamOneMemTwo = getIntent().getStringExtra("teamOneMemTwo");
            teamTwoName = getIntent().getStringExtra("teamTwoName");
            teamTwoMemOne = getIntent().getStringExtra("teamTwoMemOne");
            teamTwoMemTwo = getIntent().getStringExtra("teamTwoMemTwo");
        } else {
            // jeigu raundas > 1, gaunami komandų ir narių pavadinimai iš RoundEncActivity. išvengiama 0 values onCreate.
            teamOneName = getIntent().getStringExtra("teamOneNameFromREA");
            teamOneMemOne = getIntent().getStringExtra("teamOneMemOneFromREA");
            teamOneMemTwo = getIntent().getStringExtra("teamOneMemTwoFromREA");
            teamTwoName = getIntent().getStringExtra("teamTwoNameFromREA");
            teamTwoMemOne = getIntent().getStringExtra("teamTwoMemOneFromREA");
            teamTwoMemTwo = getIntent().getStringExtra("teamTwoMemTwoFromREA");

        }

        //gaunami total points iš RoundEndActivity.
        totalPointsTeam1 = getIntent().getIntExtra("currentTotalPointsTeamOneFromREA", 0);
        totalPointsTeam2 = getIntent().getIntExtra("currentTotalPointsTeamTwoFromREA", 0);

        Log.d(TAG, "teamonename " + team1.teamName);

        //TODO. addTeams.onClick metode negaliu set'inti objekto values. čia objekto negaliu kurti,nes kiekvieną round'ą initializins iš naujo. Tas pats su RoundEndActivity.
        //TODO. sharedPReferences? būtų tada galima perkelti ir currentWordIndex į global value, kuris išsisaugo, ne atsinaujina.
        //TODO. perdaryti Serializable objektą? Tačiau ar išsaugos values? Values vistiek atsinaujins per intent.
    team1.setWholeTeamValues(teamOneName, teamOneMemOne, teamOneMemTwo, roundPointsTeam1, totalPointsTeam1);
    team2.setWholeTeamValues(teamTwoName, teamTwoMemOne, teamTwoMemTwo, roundPointsTeam2, totalPointsTeam2);

        //TODO. WELL THIS WAS EASY IR USEFUL. REDO team/member names ir Intent spam'ą. turėtų būti galima objektą laikyti. Check object SP first.
        //TODO. Išsiaiškint, kodėl nereikia MainActivity naudoti getWordListFromSharedPrefs. kodėl dabar jis mato tą arraylist'ą????
    indexPreferences = getSharedPreferences("GlobalIndexes", Context.MODE_PRIVATE);
    globalWordIndex = indexPreferences.getInt("GlobalWordIndex", 0);
    this.currentWordIndex = globalWordIndex;

    startNewGame();
    startNewRound();
        Log.d(TAG, "wordlistas MA dydis" + globalWordList.size());
    }


        //pagrindinė fukcija, priskiriami žodžiai į arraylist'us, kurie bus siunčiami į RoundEndActivity arrayadapterius.
    private void onAnswerSelected(int answerSelected) {
     String currentWord = getCurrentWord();
       switch (answerSelected) {
            case 0:
                correctWordList.add(currentWord);
                roundPoints += 1;
                break;
            case 1:
                wrongWordList.add(currentWord);
                roundPoints -= 1;
                break;
        }
        currentWordIndex += 1;
       globalWordIndex += 1;
        Log.d(TAG, "global word index onanswersubmission " + globalWordIndex);
        //
        if (isTimerRunning) {
            displayWord(chooseNewWord());
        } else {
            goToRoundEndActivityViaIntent();
        }
         /*   for (int i = 0; i < globalWordList.size(); i++) {
            Log.d(TAG, "onAnswerSelected:name " + globalWordList.get(i) + globalWordList.size());
        }*/
    }

    private void startNewGame() {

        wordsList = new ArrayList<>();
        wordsList.add("Katė");
        wordsList.add("Šuo");
        wordsList.add("Tėtis");
        wordsList.add("Mama");
        wordsList.add("Gaidys");
        wordsList.add("Akmuo");
        wordsList.add("Draugas");
        wordsList.add("Dantys");
        wordsList.add("Nagas");
        wordsList.add("Šeima");
        wordsList.add("Stalas");
        wordsList.add("Vienas");
        wordsList.add("Du");
        wordsList.add("Trys");
        correctAnswer.setVisibility(View.INVISIBLE);
        wrongAnswer.setVisibility(View.INVISIBLE);
        Log.d(TAG, "firstteamname " + team1.teamName);
    }

    private void editSharedGlobalWordIndex() {
        SharedPreferences.Editor editor = indexPreferences.edit();
        editor.putInt("GlobalWordIndex", globalWordIndex);
        editor.apply();
        Log.d(TAG, "Global word index onclick  " + globalWordIndex);
    }

    private void startNewRound() {
        currentRoundIndex += 1;
        teamMemberIndex += 1;
        whichTeamIsRunning();
        wrongWordList = new ArrayList<>();
        correctWordList = new ArrayList<>();

        switch (teamMemberIndex) {

            case 1:
                teamMemberTurn.setText("It's " + teamOneName + " turn. " + teamOneMemOne + " explains.");
                break;
            case 2:
                teamMemberTurn.setText("It's " + teamTwoName + " turn. " + teamTwoMemOne + " explains.");
                break;
            case 3:
                teamMemberTurn.setText("It's " + teamOneName + " turn. " + teamOneMemTwo + " explains.");
                break;
            case 4:
                teamMemberTurn.setText("It's " + teamTwoName + " turn. " + teamTwoMemTwo + " explains.");
                break;
        }


    }

    private void calculateAndSetTotalPoints() {
        if (teamTurn) {
            totalPointsTeam1 += roundPoints < 0? 0 : roundPoints;
            this.roundPointsTeam1 = roundPoints < 0? 0 : roundPoints;
        } else {
            totalPointsTeam2 += roundPoints < 0? 0 : roundPoints;
            this.roundPointsTeam2 = roundPoints < 0? 0 : roundPoints;
        }
    }

    // kuriai komandai bus skaičiuojami taškai calculateAndSetTotalPoints metode
    private boolean whichTeamIsRunning() {
        if (currentRoundIndex % 2 == 1) {
            teamTurn = true;
        } else {
            teamTurn = false;
        }
        return teamTurn;
    }



    private String getCurrentWord() {
        String currentWord = globalWordList.get(currentWordIndex);
        return currentWord;
    }

    private void displayWord(String word) {

        wordWindow.setText(word);
    }
// išnaudojus žodžius, arraylist'as iteration anew.
    private String chooseNewWord() {
        if (currentWordIndex != globalWordList.size()) {
            return globalWordList.get(currentWordIndex);
        } else {
            Toast.makeText(getApplicationContext(), "Congratulations, you played all " +globalWordList.size() + "words. Here we go again, from the top!", Toast.LENGTH_LONG).show();
            currentWordIndex = 0;
            return globalWordList.get(currentWordIndex);
        }

    }


    // currentwordindexfromMA = MA reiškia iš MAIN ACTIVITY.java. REA reiškia iš ROUNDENDACTIVITY.java.
    private void goToRoundEndActivityViaIntent() {
        editSharedGlobalWordIndex();
        calculateAndSetTotalPoints();
        Intent intent = new Intent(MainActivity.this, RoundEndActivity.class);
        // pereinama į kitą rezultatų langą ir išsiunčiami arraylistai
        intent.putStringArrayListExtra("correctWordList", correctWordList);
        intent.putStringArrayListExtra("incorrectWordList", wrongWordList);
        //išsiunčiamas currentindex, kad kitą round'ą static arraylist neprasidėtų nuo 0. jis bus grąžinamas iš RoundEndActivity grįžus į šį activity(naujo round pradžią).
        intent.putExtra("currentwordindexfromMA", currentWordIndex);
        // pagal currentRoundIndex nustatoma, kuriai komandai skaičiuoti taškus whichTeamIsRunning metode ir nustato iš kurio activity priimami komandų/narių vardai.
        intent.putExtra("currentRoundIndexFromMA", currentRoundIndex);
        intent.putExtra("currentRoundPointsTeamOneFromMA", roundPointsTeam1);
        intent.putExtra("currentRoundPointsTeamTwoFromMA", roundPointsTeam2);
        intent.putExtra("currentTotalPointsTeamOneFromMA", totalPointsTeam1);
        intent.putExtra("currentTotalPointsTeamTwoFromMA", totalPointsTeam2);
        intent.putExtra("teamOneName", teamOneName);
        intent.putExtra("teamOneMemOne", teamOneMemOne);
        intent.putExtra("teamOneMemTwo", teamOneMemTwo);
        intent.putExtra("teamTwoName", teamTwoName);
        intent.putExtra("teamTwoMemOne", teamTwoMemOne);
        intent.putExtra("teamTwoMemTwo", teamTwoMemTwo);
        intent.putExtra("teamMemberIndex", teamMemberIndex);
        Log.d(TAG, "currentindexmainactivitysend " + currentWordIndex);
        startActivity(intent);
        finish();
    }

    public void openAlertDialog() {
        AlertDialog alertDialog =new AlertDialog();
        alertDialog.show(getSupportFragmentManager(), "back to new game");
    }
    // timer metodai
        private void startStop() {
                if (isTimerRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        private void startTimer() {
            displayWord(chooseNewWord());
            countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
                @Override
                public void onTick(long l) {
                    timeLeftInMilliseconds = l;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                countDownTimerTime.setText("DONE!");
                isTimerRunning = false;
                countDownTimerButton.setVisibility(View.INVISIBLE);
                }
            }.start();
            countDownTimerButton.setText("PAUSE");
            correctAnswer.setVisibility(View.VISIBLE);
            wrongAnswer.setVisibility(View.VISIBLE);
            teamMemberTurn.setVisibility(View.INVISIBLE);
            isTimerRunning = true;
        }

        private void stopTimer () {
            countDownTimer.cancel();
            countDownTimerButton.setText("RESUME");
            isTimerRunning = false;
            correctAnswer.setVisibility(View.INVISIBLE);
            wrongAnswer.setVisibility(View.INVISIBLE);

        }
        private void updateTimer() {
       // int minutes = (int) timeLeftInMilliseconds / 600000;
        int seconds = (int) timeLeftInMilliseconds % 600000 / 1000;
        String timeLeftText;
        timeLeftText = "" + seconds;
        countDownTimerTime.setText(timeLeftText);
        }

    }