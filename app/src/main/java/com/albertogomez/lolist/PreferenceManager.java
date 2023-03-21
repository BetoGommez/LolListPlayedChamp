package com.example.lolist;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

public class PreferenceManager {

    private SharedPreferences sharedPreferences;

    public PreferenceManager(MainActivity main) {
        sharedPreferences = main.getSharedPreferences("lolList",MODE_PRIVATE);


    }

    public String[] loadList(){
        String listString = sharedPreferences.getString("checked",null);
        if(listString==null){
            sharedPreferences.edit().putString("checked","-1").commit();

        }

        return sharedPreferences.getString("checked",null).split(",");
    }

    public void addId(int idToSaved){
        sharedPreferences.edit().putString("checked", sharedPreferences.getString("checked",null)+","+String.valueOf(idToSaved)).commit();
    }

    public void removeId(int idToRemove){
        String[] list = loadList();
        String finalList=list[0];
        for (int i = 1; i < list.length; i++) {
            if(!list[i].equals(String.valueOf(idToRemove))){
                finalList+=","+list[i];
            }
        }
        sharedPreferences.edit().putString("checked",finalList).commit();

    }
}
