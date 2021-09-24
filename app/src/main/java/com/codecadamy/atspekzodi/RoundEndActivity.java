package com.codecadamy.atspekzodi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class RoundEndActivity extends MainActivity {


private static final String TAG = "RoundEndActivity";
private ListView correctWordListView, incorrectWordListView;
private TextView firstTeamName, secondTeamName, firstTeamRoundPoints, secondTeamRoundPoints, firstTeamTotalPoints, secondTeamTotalPoints, lastMemberNextMember;
private Button goNextRoundButton;
private int currentWordIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_end);

        goNextRoundButton = findViewById(R.id.next_round_button_round_window);
        correctWordListView = findViewById(R.id.list);
        incorrectWordListView = findViewById(R.id.round_window_incorrect_words_list);
        firstTeamName = findViewById(R.id.first_team_name);
        secondTeamName = findViewById(R.id.second_team_name);
        firstTeamRoundPoints = findViewById(R.id.first_team_round_points);
        secondTeamRoundPoints = findViewById(R.id.second_team_round_points);
        firstTeamTotalPoints = findViewById(R.id.first_team_total_points);
        secondTeamTotalPoints = findViewById(R.id.second_team_total_points);
        lastMemberNextMember = findViewById(R.id.whos_turn_will_be_next);


        goNextRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent roundEnd = new Intent(RoundEndActivity.this, MainActivity.class);
                // perduodama visa informacija atgal į MainActivity, kad onCreate nenumuštų į 0 visos info.
                roundEnd.putExtra("currentwordindexfromREA", currentWordIndex);
                roundEnd.putExtra("currentRoundIndexFromREA", currentRoundIndex);
                roundEnd.putExtra("currentTotalPointsTeamOneFromREA", totalPointsTeam1);
                roundEnd.putExtra("currentTotalPointsTeamTwoFromREA", totalPointsTeam2);
                roundEnd.putExtra("teamOneNameFromREA", teamOneName);
                roundEnd.putExtra("teamOneMemOneFromREA", teamOneMemOne);
                roundEnd.putExtra("teamOneMemTwoFromREA", teamOneMemTwo);
                roundEnd.putExtra("teamTwoNameFromREA", teamTwoName);
                roundEnd.putExtra("teamTwoMemOneFromREA", teamTwoMemOne);
                roundEnd.putExtra("teamTwoMemTwoFromREA", teamTwoMemTwo);
                roundEnd.putExtra("teamMemberIndexFromREA", teamMemberIndex);
                Log.d(TAG, "currentindexroundendactivitysend " + currentWordIndex);
                startActivity(roundEnd);
                finish();
            }
        });

// gaunami teisingų/neteisingų žodžių arraylist'ai iš MainActivity, paverčiami į listView ir pateikiami kaip list'ai žmogui susipažinti su (ne)sėkmėmis.
        ArrayList correctArrayListUpdated = getIntent().getExtras().getStringArrayList("correctWordList");
        ArrayList incorrectArrayListUpdated = getIntent().getExtras().getStringArrayList("incorrectWordList");
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.adapter_view_layout, correctArrayListUpdated);
        ArrayAdapter incorrectAdapter = new ArrayAdapter<String>(this, R.layout.adapter_view_layout, incorrectArrayListUpdated);


        // gaunama informacija iš MainActivity, poto persiunčiama atgal su goNextRoundButton.
        currentWordIndex = getIntent().getIntExtra("currentwordindexfromMA", 0);
        currentRoundIndex = getIntent().getIntExtra("currentRoundIndexFromMA", 0);
        roundPointsTeam1 = getIntent().getIntExtra("currentRoundPointsTeamOneFromMA", 0);
        roundPointsTeam2 = getIntent().getIntExtra("currentRoundPointsTeamTwoFromMA", 0);
        totalPointsTeam1 = getIntent().getIntExtra("currentTotalPointsTeamOneFromMA", 0);
        totalPointsTeam2 = getIntent().getIntExtra("currentTotalPointsTeamTwoFromMA", 0);
        teamOneName = getIntent().getStringExtra("teamOneName");
        teamOneMemOne = getIntent().getStringExtra("teamOneMemOne");
        teamOneMemTwo = getIntent().getStringExtra("teamOneMemTwo");
        teamTwoName = getIntent().getStringExtra("teamTwoName");
        teamTwoMemOne = getIntent().getStringExtra("teamTwoMemOne");
        teamTwoMemTwo = getIntent().getStringExtra("teamTwoMemTwo");
        teamMemberIndex = getIntent().getIntExtra("teamMemberIndex", 0);


        Log.d(TAG, "currentRoundIndexRoundEndActivity " + currentRoundIndex);
        Log.d(TAG, "currentindexroundendactivityreceive " + currentWordIndex);

        firstTeamName.setText(team1.teamName);
        secondTeamName.setText(team2.teamName);
        firstTeamRoundPoints.setText(String.valueOf(roundPointsTeam1));
        secondTeamRoundPoints.setText(String.valueOf(roundPointsTeam2));
        firstTeamTotalPoints.setText(String.valueOf(totalPointsTeam1));
        secondTeamTotalPoints.setText(String.valueOf(totalPointsTeam2));

        Log.d(TAG, "team member index " + teamMemberIndex);

        switch (teamMemberIndex) {

            case 1:
                lastMemberNextMember.setText("Good job, " + teamOneMemOne + "! " + "It's your turn next, " + teamTwoMemOne + ". Are you ready?");
                break;
            case 2:
                lastMemberNextMember.setText("Good job, " + teamTwoMemOne + "! " + "It's your turn next, " + teamOneMemTwo + ". Are you ready?");
                break;
            case 3:
                lastMemberNextMember.setText("Good job, " + teamOneMemTwo + "! " + "It's your turn next, " + teamTwoMemTwo + ". Are you ready?");
                break;
            case 4:
                lastMemberNextMember.setText("Good job, " + teamTwoMemTwo + "! " + "It's your turn next, " + teamOneMemOne + ". Are you ready?");
                break;
        }

        if (teamMemberIndex == 4) {
            teamMemberIndex = 0;
        }
        determineWinner();

        correctWordListView.setAdapter(adapter);
        incorrectWordListView.setAdapter(incorrectAdapter);
        setListViewHeightBasedOnItems(correctWordListView);
        setListViewHeightBasedOnItems(incorrectWordListView);
        Log.d(TAG, "wordlistas RAE dydis" + globalWordList.size());
    }

    private void determineWinner() {
        if (this.totalPointsTeam1 > 50 || this.totalPointsTeam2 > 50) {
            goNextRoundButton.setText("New Game");
            goNextRoundButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newGameAfterWinner = new Intent(RoundEndActivity.this, addTeams.class);
                    startActivity(newGameAfterWinner);
                    finish();
                }
            });
        }
        if (this.totalPointsTeam1 > 50) {
            lastMemberNextMember.setText("Good job, " + teamOneMemOne + " , "+ teamOneMemTwo + ". Your team won! Play again?");
        } else if (this.totalPointsTeam2 > 50) {
            lastMemberNextMember.setText("Good job, " + teamTwoMemOne + " , "+ teamTwoMemTwo + ". Your team won! Play again?");
        }
    }


// metodas gautas iš http://web.archive.org/web/20160513164104/http://blog.lovelyhq.com/setting-listview-height-depending-on-the-items/

    private static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
}