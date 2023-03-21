package com.example.lolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adaptor extends RecyclerView.Adapter<Adaptor.vHolder> {


    ArrayList<Champion> champsToShow;

    private ColorMatrix matrix;

    private ColorMatrixColorFilter filter;

    String[] checkedList;

    private int totalChampions;

    private MainActivity mainActivity;

    private PreferenceManager preferenceManager;

    public Adaptor(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.champsToShow = mainActivity.champsToShow;
        this.preferenceManager = mainActivity.getPreferenceManager();
        loadCheckedList();
        totalChampions = mainActivity.getChampionsLength();
        matrix = new ColorMatrix();
        matrix.setSaturation(0);
        filter = new ColorMatrixColorFilter(matrix);
    }

    private void loadCheckedList() {
        checkedList = preferenceManager.loadList();
        mainActivity.setTitle("Champions: " +(checkedList.length-1)+"/"+mainActivity.getChampionsLength());
    }

    public void updateList(ArrayList<Champion> champsToShow) {
        this.champsToShow = champsToShow;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public vHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.celda, parent, false);
        return new vHolder(vista);

    }


    @Override
    public void onBindViewHolder(@NonNull vHolder holder, int position) {
        int[] imagesID = champsToShow.get(position).getImage();
        holder.champImage.setImageDrawable(holder.champImage.getContext().getDrawable(imagesID[0]));



        for (int i = 0; i < checkedList.length; i++) {
            if (checkedList[i].equals(String.valueOf(imagesID[0]))) {
                holder.champImage.setImageDrawable(holder.champImage.getContext().getDrawable(imagesID[1]));
            }
        }


        holder.champImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog;


                boolean contained = false;


                for (int i = 0; i < checkedList.length; i++) {
                    if (checkedList[i].equals(String.valueOf(imagesID[0]))) {
                        contained = true;
                    }
                }
                if (contained) {
                    dialog = new AlertDialog
                            .Builder(mainActivity)
                            .setPositiveButton("Yes, remove", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    holder.champImage.setImageResource(imagesID[0]);
                                    preferenceManager.removeId(imagesID[0]);
                                    loadCheckedList();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Confirm")
                            .setMessage("¿Do you want to remove this champion?")
                            .create();



                } else {
                    dialog = new AlertDialog
                            .Builder(mainActivity)
                            .setPositiveButton("Yes, add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    preferenceManager.addId(imagesID[0]);
                                    holder.champImage.setImageResource(imagesID[1]);
                                    loadCheckedList();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Confirm")
                            .setMessage("¿Do you want to add this champion?")
                            .create();
                }
                dialog.show();

            }
        });

    }


    @Override
    public int getItemCount() {
        return champsToShow.size();
    }

    public class vHolder extends RecyclerView.ViewHolder {

        private ImageView champImage;

        public vHolder(@NonNull View itemView) {
            super(itemView);
            this.champImage = itemView.findViewById(R.id.champImage);


        }
    }
}
