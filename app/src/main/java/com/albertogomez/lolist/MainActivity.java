package com.example.lolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private PreferenceManager preferenceManager;
    private Adaptor adaptor;
    private ArrayList<Champion> champions;
    private GridLayoutManager glm;

    private SeekBar countBar;

    ArrayList<Champion> champsToShow = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        preferenceManager = new PreferenceManager(this);
        champsToShow = loadChamps();
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        adaptor = new Adaptor(this);
        rv.setAdapter(adaptor);
        glm = new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false);
        rv.setLayoutManager(glm);
        countBar = (SeekBar) findViewById(R.id.seekBar);
        setCountBarListener();

    }

    private void setCountBarListener(){
       countBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               glm.setSpanCount(seekBar.getProgress()+4);
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        MenuItem search = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                champsToShow = new ArrayList<Champion>();

                for (int i = 0; i < champions.size(); i++) {
                    if(champions.get(i).getName().contains(s)){
                        champsToShow.add(champions.get(i));
                    }
                }

                adaptor.updateList(champsToShow);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.buttonCount){
            if(countBar.getVisibility()==View.GONE){
                countBar.setVisibility(View.VISIBLE);
            }else{
                countBar.setVisibility(View.GONE);
            }
        }

        return super.onOptionsItemSelected(item);

    }

    public ArrayList<Champion> loadChamps() {
        boolean pos = true;
        champions = new ArrayList<Champion>();
        int champCount = -1;
        for (int identifier = R.drawable.aatrox; identifier < R.drawable.zyrab + 1; identifier++) {

            try{
                String name = getResources().getResourceEntryName(identifier);

                if (name.split("_").length < 2) {
                    if(pos){
                        champions.add(new Champion(identifier, name, new int[]{identifier, 0}));
                        champCount++;
                        Log.i("CHAMP COUNT: ",champCount+"");
                    }else{
                        champions.get(champCount).setImage(new int[]{champions.get(champCount).getImage()[0],identifier});

                    }
                    Log.i("NOMBRE Y ID:", name + " " + identifier);
                    pos=!pos;
                }
            }catch (Exception e){

            }

        }

        return  champions;
    }

    public PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    public int getChampionsLength() {
        return champions.size();
    }
}