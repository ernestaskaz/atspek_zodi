package com.codecadamy.atspekzodi;

import java.io.Serializable;

public class Teams {
    String teamName, playerOne, playerTwo;
    int pointsAfterRound, pointsInTotal, teamNumber;


    public Teams(String name, String player1, String player2, int roundPoints, int totalPoints, int teamIndex) {
        teamName = name;
        playerOne = player1;
        playerTwo = player2;
        pointsAfterRound = roundPoints;
        pointsInTotal = totalPoints;
        teamNumber = teamIndex;

    }

    public void setWholeTeamValues(String teamname, String member1, String member2, int roundPoints, int totalPoints) {
        this.teamName = teamname;
        this.playerOne = member1;
        this.playerTwo = member2;
        this.pointsAfterRound = roundPoints;
        this.pointsInTotal = totalPoints;
    }
}

