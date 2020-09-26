package com.zanhd.trivia.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }
    public void saveHighScore(int currScore){
        int lastScore = preferences.getInt("high_score",0);
        if(currScore > lastScore){
            //we have a new highest and we save it!
            preferences.edit().putInt("high_score",currScore).apply();
        }
    }
    public int getHighScore(){
        return preferences.getInt("high_score",0);
    }

    public void setState(int index){
        preferences.edit().putInt("index_state",index).apply();
    }

    public int getState(){
        return preferences.getInt("index_state",0);
    }
}
